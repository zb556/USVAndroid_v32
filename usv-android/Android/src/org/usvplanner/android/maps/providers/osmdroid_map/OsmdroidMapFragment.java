package org.usvplanner.android.maps.providers.osmdroid_map;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.o3dr.android.client.Drone;
import com.o3dr.services.android.lib.coordinate.LatLong;
import com.o3dr.services.android.lib.coordinate.LatLongAlt;
import com.o3dr.services.android.lib.drone.attribute.AttributeEvent;
import com.o3dr.services.android.lib.drone.attribute.AttributeType;
import com.o3dr.services.android.lib.drone.property.FootPrint;
import com.o3dr.services.android.lib.drone.property.Gps;

import org.usvplanner.android.DroidPlannerApp;
import org.usvplanner.android.R;
import org.usvplanner.android.fragments.SettingsFragment;
import org.usvplanner.android.graphic.map.GraphicHome;
import org.usvplanner.android.maps.DPMap;
import org.usvplanner.android.maps.MarkerInfo;
import org.usvplanner.android.maps.PolylineInfo;
import org.usvplanner.android.maps.providers.DPMapProvider;
import org.usvplanner.android.maps.providers.google_map.tiles.mapbox.offline.MapDownloader;
import org.usvplanner.android.utils.MapUtils;
import org.usvplanner.android.utils.prefs.AutoPanMode;
import org.usvplanner.android.utils.prefs.DroidPlannerPrefs;
import org.jetbrains.annotations.NotNull;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class OsmdroidMapFragment extends Fragment implements DPMap, LocationListener {

    private static final String TAG = OsmdroidMapFragment.class.getSimpleName();

    private MapView mapView;
    private final TileSystem tileSystem = MapView.getTileSystem();
    private MyLocationNewOverlay mLocationOverlay;

    private static final int GET_DRAGGABLE_FROM_MARKER_INFO = -1;
    private static final int IS_DRAGGABLE = 0;
    private static final int IS_NOT_DRAGGABLE = 1;
    private static final double DEFAULT_MIN_ZOOM = 3f;
    private static final double DEFAULT_MAX_ZOOM = 21f;
//    private static final float GO_TO_MY_LOCATION_ZOOM = 17f;

    private static final IntentFilter mEventFilter = new IntentFilter();

    private final Map<Marker, MarkerInfo> markersMap = new HashMap<>();
    private final Map<Polyline, PolylineInfo> polylinesMap = new HashMap<>();

    private DroidPlannerPrefs mAppPrefs;
    private final AtomicReference<AutoPanMode> mPanMode = new AtomicReference<AutoPanMode>(AutoPanMode.DISABLED);

    public static final int LEASH_PATH = 0;
    public static final int MISSION_PATH = 1;
    public static final int FLIGHT_PATH = 2;
    private static final int BOARDER = 200;

    private Marker userMarker = null;
    private Location currentLocation;

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        if (currentLocation == null)
            return;

        mapView.invalidate();
        if (mPanMode.get() == AutoPanMode.USER) {
            Log.d(TAG, "User location changed.");
            updateCamera(MapUtils.locationToCoord(currentLocation));
            //Update the user location icon.
            if (userMarker == null) {
                Log.d(TAG, "onLocationChanged null");
                userMarker = new Marker(mapView);
                userMarker.setPosition(new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude()));
                userMarker.setDraggable(false);
                userMarker.setFlat(true);
                userMarker.setVisible(true);
                userMarker.setAnchor(0.5f, 0.5f);
                userMarker.setIcon(getResources().getDrawable(R.drawable.user_location));
                mapView.getOverlays().add(userMarker);
            } else {
                userMarker.setPosition(new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude()));
            }
        } else {
            if (userMarker != null) {
                clearUserMarker();
            }
        }
        if (mLocationListener != null) {
            mLocationListener.onLocationChanged(currentLocation);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    @IntDef({LEASH_PATH, MISSION_PATH, FLIGHT_PATH})
    @Retention(RetentionPolicy.SOURCE)
    private @interface PolyLineType {
    }

    private boolean showFlightPath;
    private List<GeoPoint> flightPathPoints = new LinkedList<>();
    private Polyline mFlightPath;
    private Polyline mMissionPath;
    private Polyline mDroneLeashPath;

    private DPMap.OnMapClickListener mMapClickListener;
    private DPMap.OnMapLongClickListener mMapLongClickListener;
    private DPMap.OnMarkerClickListener mMarkerClickListener;
    private DPMap.OnMarkerDragListener mMarkerDragListener;
    private android.location.LocationListener mLocationListener;
//    private Location currentLocation = null;

    protected DroidPlannerApp mDpApp;
    private Polygon mFootprintPoly;

    private List<Polygon> mPolygonsPaths = new ArrayList<Polygon>();

    static {
        mEventFilter.addAction(AttributeEvent.GPS_POSITION);
        mEventFilter.addAction(SettingsFragment.ACTION_MAP_ROTATION_PREFERENCE_UPDATED);
    }

    private final BroadcastReceiver mEventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            switch (action) {
                case AttributeEvent.GPS_POSITION:
                    if (mPanMode.get() == AutoPanMode.DRONE) {
                        final Drone drone = getDroneApi();
                        if (!drone.isConnected())
                            return;

                        final Gps droneGps = drone.getAttribute(AttributeType.GPS);
                        if (droneGps != null && droneGps.isValid()) {
                            final LatLong droneLocation = droneGps.getPosition();
                            updateCamera(droneLocation);
                        }
                    }
                    break;
                case SettingsFragment.ACTION_MAP_ROTATION_PREFERENCE_UPDATED:
                    RotationGesture();
                    break;
            }
        }
    };

    private Drone getDroneApi() {
        return mDpApp.getDrone();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDpApp = (DroidPlannerApp) context.getApplicationContext();  ///TO BE CONFIRM
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        Log.d(TAG, "onCreateView");
        setHasOptionsMenu(true);
        Configuration.getInstance().setOsmdroidBasePath(new File(Environment.getExternalStorageDirectory(), "XLUAV"));
        Configuration.getInstance().setOsmdroidTileCache(new File(Environment.getExternalStorageDirectory(), "XLUAV/tiles"));
        final Context context = getActivity().getApplicationContext();

        //map init
        //important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        mapView = new MapView(context);

        //base UI
        mAppPrefs = DroidPlannerPrefs.getInstance(context);

        final Bundle args = getArguments();
        if (args != null) {
            showFlightPath = args.getBoolean(EXTRA_SHOW_FLIGHT_PATH);
        }
        setupMap();
        return mapView;
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext())
                .registerReceiver(mEventReceiver, mEventFilter);
        RotationGesture();
        setupMapEvent();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext())
                .unregisterReceiver(mEventReceiver);
    }

    private void setupMap() {
        if (mapView == null) return;
        setupMapUI();
        setupMapOverlay();
    }

    private void setupMapUI() {
        mapView.setMaxZoomLevel(DEFAULT_MAX_ZOOM);
        mapView.setMinZoomLevel(DEFAULT_MIN_ZOOM);
        mapView.setBuiltInZoomControls(false);
        mapView.setMultiTouchControls(true);

        final OsmdroidMapPrefFragment provider = (OsmdroidMapPrefFragment) (getProvider().getMapProviderPreferences());
        final Context context = getActivity().getApplicationContext();
        OnlineTileSourceBase mapType = provider.getMapType(context);
        mapView.setTileSource(mapType);
    }

    private void setupMapOverlay() {
        Log.d(TAG, "setupMapOverlay");
        //My Location
        final Context context = getActivity().getApplicationContext();
        GpsMyLocationProvider provider = new GpsMyLocationProvider(context);
        provider.addLocationSource(LocationManager.NETWORK_PROVIDER);
        mLocationOverlay = new MyLocationNewOverlay(provider, mapView);
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.setDrawAccuracyEnabled(false);
        mLocationOverlay.setPersonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.my_user_location));
        mapView.getOverlays().add(mLocationOverlay);
        mapView.invalidate();
    }

    private void setupMapEvent() {
        MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint point) {
                Log.d(TAG, "singleTapConfirmedHelper");
                if (mMapClickListener != null) {
                    mMapClickListener.onMapClick(MapUtils.osmdroidGeoPointToCoord(point));
                }
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint point) {
                Log.d(TAG, "longPressHelper");
                if (mMapLongClickListener != null) {
                    mMapLongClickListener.onMapLongClick(MapUtils.osmdroidGeoPointToCoord(point));
                }
                return false;
            }
        };
        final MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(mapEventsReceiver);
        mapView.getOverlays().add(mapEventsOverlay);
    }

    private void RotationGesture() {
        final RotationGestureOverlay rotationOverlay = new RotationGestureOverlay(mapView);
        rotationOverlay.setEnabled(mAppPrefs.isMapRotationEnabled());
        mapView.setMultiTouchControls(true);
        mapView.getOverlays().add(rotationOverlay);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        try {
            //on API15 AVDs,network provider fails. no idea why
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) this);
        }
        catch (Exception ex){}
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy");
        super.onDestroy();
        mapView.onDetach();
//        currentLocation=null;
    }

    @Override
    public void addFlightPathPoint(LatLongAlt coord) {
        final GeoPoint position = MapUtils.coordToOsmdroidGeoPoint(coord);

        if (showFlightPath) {
            flightPathPoints.add(position);
            if (flightPathPoints.size() >= 2) {
                if (mFlightPath == null) {
                    mFlightPath = new Polyline();
                    mFlightPath.getOutlinePaint().setColor(FLIGHT_PATH_DEFAULT_COLOR);
                    mFlightPath.getOutlinePaint().setStrokeWidth(FLIGHT_PATH_DEFAULT_WIDTH);
                    mFlightPath.setPoints(flightPathPoints);
                    mapView.getOverlays().add(mFlightPath);
                    mapView.invalidate();
                } else {
                    mFlightPath.setPoints(flightPathPoints);
                    mapView.invalidate();
                }
            } else {
                if (mFlightPath != null) {
                    mapView.getOverlays().remove(mFlightPath);
                    mapView.invalidate();
                    mFlightPath = null;
                }
            }
        }
    }

    @Override
    public void addCameraFootprint(FootPrint footprintToBeDraw) {
        if (mapView == null) return;

        Polygon polygon = new Polygon();

        final List<GeoPoint> pathPoints = new ArrayList<GeoPoint>(footprintToBeDraw.getVertexInGlobalFrame().size());
        for (LatLong coord : footprintToBeDraw.getVertexInGlobalFrame()) {
            pathPoints.add(MapUtils.coordToOsmdroidGeoPoint(coord));
        }
        polygon.setStrokeColor(FOOTPRINT_DEFAULT_COLOR);
        polygon.setStrokeWidth(FOOTPRINT_DEFAULT_WIDTH);
        polygon.setFillColor(FOOTPRINT_FILL_COLOR);
        polygon.setPoints(pathPoints);
        mapView.getOverlays().add(polygon);
        mapView.invalidate();
    }

    @Override
    public void addMarker(final MarkerInfo markerInfo) {

        if (markerInfo == null || (markerInfo.isOnMap() && markerInfo.isVisible()) || markerInfo.getPosition() == null || mapView == null) {
            return;
        }
//        final FolderOverlay folderOverlayMarker = new FolderOverlay();
        try {
            final Marker marker = new Marker(mapView);
            markerInfo.setProxyMarker(new ProxyMapMarker(marker));
            if (markerInfo.getIcon(getResources()) != null) {
                marker.setIcon(new BitmapDrawable(getResources(), markerInfo.getIcon(getResources())));
            }
            marker.setAnchor(0.5f, 0.5f);
            marker.setPosition(MapUtils.coordToOsmdroidGeoPoint(markerInfo.getPosition()));
            marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    if (mMarkerClickListener != null) {
                        final MarkerInfo markerInfo = markersMap.get(marker);
                        if (markerInfo != null)
                            return mMarkerClickListener.onMarkerClick(markerInfo);
                    }
                    return true;
                }
            });
            marker.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
                @Override
                public void onMarkerDrag(Marker marker) {
                    if (mMarkerDragListener != null) {
                        final MarkerInfo markerInfo = markersMap.get(marker);
                        if (!(markerInfo instanceof GraphicHome)) {
                            markerInfo.setPosition(MapUtils.osmdroidGeoPointToCoord(marker.getPosition()));
                            mMarkerDragListener.onMarkerDrag(markerInfo);
                        }
                    }
                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    if (mMarkerDragListener != null) {
                        final MarkerInfo markerInfo = markersMap.get(marker);
                        markerInfo.setPosition(MapUtils.osmdroidGeoPointToCoord(marker.getPosition()));
                        mMarkerDragListener.onMarkerDragEnd(markerInfo);
                    }
                }

                @Override
                public void onMarkerDragStart(Marker marker) {
                    if (mMarkerDragListener != null) {
                        final MarkerInfo markerInfo = markersMap.get(marker);
                        if (!(markerInfo instanceof GraphicHome)) {
                            markerInfo.setPosition(MapUtils.osmdroidGeoPointToCoord(marker.getPosition()));
                            mMarkerDragListener.onMarkerDragStart(markerInfo);
                        }
                    }
                }
            });
            mapView.getOverlays().add(marker);
            mapView.invalidate();
            markersMap.put(marker, markerInfo);
        }
        catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void addMarkers(List<MarkerInfo> markerInfoList) {
        addMarkers(markerInfoList, GET_DRAGGABLE_FROM_MARKER_INFO);
    }

    @Override
    public void addMarkers(List<MarkerInfo> markerInfoList, boolean isDraggable) {
        addMarkers(markerInfoList, isDraggable ? IS_DRAGGABLE : IS_NOT_DRAGGABLE);
    }

    private void addMarkers(final List<MarkerInfo> markerInfoList, int draggableType){
        if(markerInfoList == null || markerInfoList.isEmpty())
            return;

        final int infoCount = markerInfoList.size();
        for(int i = 0; i < infoCount; i++){
            MarkerInfo markerInfo = markerInfoList.get(i);
            boolean isDraggable = draggableType == GET_DRAGGABLE_FROM_MARKER_INFO
                    ? markerInfo.isDraggable()
                    : draggableType == IS_DRAGGABLE;
            if((markerInfo.isOnMap() && markerInfo.isVisible()) || markerInfo.getPosition() == null){
                continue;
            }
            Marker marker = new Marker(mapView);
            markerInfo.setProxyMarker(new ProxyMapMarker(marker));
            marker.setDraggable(isDraggable);
            if(markerInfo.getIcon(getResources()) != null){
                marker.setIcon(new BitmapDrawable(getResources(),markerInfo.getIcon(getResources())));
            }
            marker.setAnchor(0.5f, 0.5f);
            marker.setPosition(MapUtils.coordToOsmdroidGeoPoint(markerInfo.getPosition()));
            marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
//                    Log.d(TAG,"onMarkerClick");
                    if (mMarkerClickListener != null) {
//                        Log.d(TAG,"onMarkerClick not null");
                        final MarkerInfo markerInfo = markersMap.get(marker);
                        if (markerInfo != null)
//                            Log.d(TAG,"onMarkerClick markerInfo");
                            return mMarkerClickListener.onMarkerClick(markerInfo);
                    }
                    return true;
                }
            });
            marker.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
                @Override
                public void onMarkerDrag(Marker marker) {
                    if (mMarkerDragListener != null) {
                        final MarkerInfo markerInfo = markersMap.get(marker);
                        if(!(markerInfo instanceof GraphicHome)) {
                            markerInfo.setPosition(MapUtils.osmdroidGeoPointToCoord(marker.getPosition()));
                            mMarkerDragListener.onMarkerDrag(markerInfo);
                        }
                    }
                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    if (mMarkerDragListener != null) {
                        final MarkerInfo markerInfo = markersMap.get(marker);
                        markerInfo.setPosition(MapUtils.osmdroidGeoPointToCoord(marker.getPosition()));
                        mMarkerDragListener.onMarkerDragEnd(markerInfo);
                    }
                }

                @Override
                public void onMarkerDragStart(Marker marker) {
                    if (mMarkerDragListener != null) {
                        final MarkerInfo markerInfo = markersMap.get(marker);
                        if(!(markerInfo instanceof GraphicHome)) {
                            markerInfo.setPosition(MapUtils.osmdroidGeoPointToCoord(marker.getPosition()));
                            mMarkerDragListener.onMarkerDragStart(markerInfo);
                        }
                    }
                }
            });
            mapView.getOverlays().add(marker);
            mapView.invalidate();
            markersMap.put(marker, markerInfo);
        }
    }

    @Override
    public void addPolyline(PolylineInfo polylineInfo) {
        if (polylineInfo == null || (polylineInfo.isOnMap() && polylineInfo.isVisible()) || mapView == null) {
            return;
        }

        Polyline polyline = new Polyline();
        polylineInfo.setProxyPolyline(new ProxyMapPolyline(polyline));
        polylinesMap.put(polyline, polylineInfo);
    }

    @Override
    public void clearAll() {
        clearMarkers();
        clearUserMarker();
        clearPolylines();
        clearFlightPath();
        clearMissionPath();
        clearFootPrints();
        clearPolygonPaths();
        clearDroneLeashPath();
        if(mapView != null)
        {
            mapView.getOverlays().clear();
        }
    }

    public void zqtUpdate() {
        mapView.invalidate();
    }

    @Override
    public void clearMarkers() {
        for (MarkerInfo marker : markersMap.values()) {
            marker.removeProxyMarker();
            mapView.getOverlays().remove(marker);
            mapView.invalidate();
        }

        markersMap.clear();
    }
    private void clearUserMarker(){
        if(userMarker != null){
            mapView.getOverlays().remove(userMarker);
            mapView.invalidate();
            userMarker = null;
        }
    }
    @Override
    public void clearFlightPath() {
        flightPathPoints.clear();
        if (mFlightPath != null && mapView != null) {
//            mFlightPath.remove();
            mapView.getOverlays().remove(mFlightPath);
            mapView.invalidate();
            mFlightPath = null;
        }
    }

    @Override
    public void clearPolylines() {
        for(PolylineInfo info: polylinesMap.values()){
            info.removeProxy();
        }
        polylinesMap.clear();
    }

    private void clearMissionPath() {
        if(mMissionPath != null){
            mapView.getOverlays().remove(mMissionPath);
            mMissionPath = null;
        }
    }
    private void clearFootPrints() {
        if(mFootprintPoly != null){
            mapView.getOverlays().remove(mFootprintPoly);
            mFootprintPoly = null;
        }
    }
    private void clearPolygonPaths() {
        for(Polygon polygon: mPolygonsPaths){
            mapView.getOverlays().remove(polygon);
        }
        mPolygonsPaths.clear();
    }
    private void clearDroneLeashPath() {
        if(mDroneLeashPath != null){
            mapView.getOverlays().remove(mDroneLeashPath);
            mDroneLeashPath = null;
        }
    }
    @Override
    public void downloadMapTiles(MapDownloader mapDownloader, VisibleMapArea mapRegion, int minimumZ, int maximumZ) {
    }

    @Override
    public LatLong getMapCenter() {
        return MapUtils.osmdroidIGeoPointToCoord(mapView.getMapCenter());
    }

    @Override
    public float getMapZoomLevel() {
        return (float) mapView.getZoomLevelDouble();
    }

    @Override
    public float getMaxZoomLevel() {
        return (float) mapView.getMaxZoomLevel();
    }

    @Override
    public float getMinZoomLevel() {
        return (float) mapView.getMinZoomLevel();
    }

    @Override
    public DPMapProvider getProvider() {
        return DPMapProvider.GOOGLE_CHINA_MAP;
    }

    @Override
    public VisibleMapArea getVisibleMapArea() {
        if(mapView == null)
            return null;
        BoundingBox boundingBox = mapView.getProjection().getBoundingBox();
        return new VisibleMapArea(
                new LatLong(boundingBox.getLatNorth(),boundingBox.getLonWest()),
                new LatLong(boundingBox.getLatNorth(),boundingBox.getLonEast()),
                new LatLong(boundingBox.getLatSouth(),boundingBox.getLonWest()),
                new LatLong(boundingBox.getLatSouth(),boundingBox.getLonEast()));
    }

    @Override
    public void goToDroneLocation() {
        Drone dpApi = getDroneApi();
        if (!dpApi.isConnected())
            return;

        Gps gps = dpApi.getAttribute(AttributeType.GPS);
        if (!gps.isValid()) {
            Toast.makeText(getActivity().getApplicationContext(),
                    R.string.drone_no_location, Toast.LENGTH_SHORT).show();
            return;
        }

        final LatLong droneLocation = gps.getPosition();
        updateCamera(droneLocation);
    }

    @Override
    public void goToMyLocation() {
        if(currentLocation == null)
            return;
        updateCamera(MapUtils.locationToCoord(currentLocation));
    }

    @Override
    public void addDroneLocation() {
        Drone dpApi = getDroneApi();
        if (!dpApi.isConnected())
            return;

        Gps gps = dpApi.getAttribute(AttributeType.GPS);
        if (!gps.isValid()) {
            Toast.makeText(getActivity().getApplicationContext(),
                    R.string.drone_no_location, Toast.LENGTH_SHORT).show();
            return;
        }

        final LatLong droneLocation = gps.getPosition();
        mMapClickListener.onMapClick(droneLocation);
    }

    @Override
    public void loadCameraPosition() {
        if (mapView == null) return;
        final SharedPreferences settings = mAppPrefs.prefs;

        mapView.setMapOrientation(settings.getFloat(PREF_BEA, DEFAULT_BEARING),false);
        mapView.getController().setZoom(settings.getFloat(PREF_ZOOM, DEFAULT_ZOOM_LEVEL));
        mapView.setExpectedCenter(new GeoPoint(settings.getFloat(PREF_LAT, DEFAULT_LATITUDE), settings.getFloat(PREF_LNG, DEFAULT_LONGITUDE)));
//        Log.d(TAG,String.format(Locale.US,"%f %f",settings.getFloat(PREF_LAT, DEFAULT_LATITUDE),settings.getFloat(PREF_LNG, DEFAULT_LONGITUDE)));
    }

    @Override
    public void saveCameraPosition() {
        if (mapView == null) return;
//        final SharedPreferences settings = mAppPrefs.prefs;
        mAppPrefs.prefs.edit()
                .putFloat(PREF_BEA,  mapView.getMapOrientation())
                .putFloat(PREF_ZOOM, (float) mapView.getZoomLevelDouble())
                .putFloat(PREF_LAT,(float) mapView.getMapCenter().getLatitude())
                .putFloat(PREF_LNG, (float) mapView.getMapCenter().getLongitude()).apply();

//        Log.d(TAG,String.format(Locale.US,"%f %f",settings.getFloat(PREF_LAT, DEFAULT_LATITUDE),settings.getFloat(PREF_LNG, DEFAULT_LONGITUDE)));

    }

    @Override
    public List<LatLong> projectPathIntoMap(List<LatLong> path) {
        if(mapView == null)
            return  null;
        List<LatLong> coords = new ArrayList<LatLong>();
        Projection projection = mapView.getProjection();
        final Point unrotatedPoint = new Point();

        for (LatLong point : path) {
            projection.unrotateAndScalePoint((int) point.getLatitude(), (int) point.getLongitude(), unrotatedPoint);
            GeoPoint iGeoPoint = (GeoPoint) projection.fromPixels(unrotatedPoint.x, unrotatedPoint.y);
            coords.add(MapUtils.osmdroidGeoPointToCoord(iGeoPoint));
        }

        return coords;
    }

    @Override
    public void removeMarker(MarkerInfo markerInfo) {
        if(markerInfo == null || !markerInfo.isOnMap() || mapView == null)
            return;

        ProxyMapMarker proxyMarker = (ProxyMapMarker) markerInfo.getProxyMarker();
        markerInfo.removeProxyMarker();
        mapView.getOverlays().remove(proxyMarker.marker);
        mapView.invalidate();
        markersMap.remove(proxyMarker.marker);
    }

    @Override
    public void removeMarkers(Collection<MarkerInfo> markerInfoList) {
        if (markerInfoList == null || markerInfoList.isEmpty()) {
            return;
        }

        for (MarkerInfo markerInfo : markerInfoList) {
            removeMarker(markerInfo);
        }
    }

    @Override
    public void removePolyline(PolylineInfo polylineInfo) {
        if(polylineInfo == null || !polylineInfo.isOnMap() || mapView == null)
            return;

        ProxyMapPolyline proxy = (ProxyMapPolyline) polylineInfo.getProxyPolyline();
        polylineInfo.removeProxy();
        mapView.getOverlays().remove(proxy.polyline);
        mapView.invalidate();
        polylinesMap.remove(proxy.polyline);
    }

    @Override
    public void selectAutoPanMode(AutoPanMode target) {
        final AutoPanMode currentMode = mPanMode.get();
        if (currentMode == target)
            return;
        mPanMode.compareAndSet(currentMode, target);
//        if(currentMode == AutoPanMode.USER){
//            if(mapView != null && !mLocationOverlay.isFollowLocationEnabled()){
//                mLocationOverlay.enableFollowLocation();
//                mapView.invalidate();
//            }
//        }
//        else {
//            if(mapView != null && mLocationOverlay.isFollowLocationEnabled()){
//                mLocationOverlay.disableFollowLocation();
//                mapView.invalidate();
//            }
//        }
    }

    @Override
    public void setMapPadding(int left, int top, int right, int bottom) {
        mapView.layout(left,top,right,bottom);
    }

    @Override
    public void setOnMapClickListener(OnMapClickListener listener) {
        mMapClickListener = listener;
    }

    @Override
    public void setOnMapLongClickListener(OnMapLongClickListener listener) {
        mMapLongClickListener = listener;
    }

    @Override
    public void setOnMarkerClickListener(OnMarkerClickListener listener) {
        mMarkerClickListener = listener;
    }

    @Override
    public void setOnMarkerDragListener(OnMarkerDragListener listener) {
        mMarkerDragListener = listener;
    }

    @Override
    public void setLocationListener(LocationListener listener) {
        mLocationListener = listener;
    }

    private void updateCamera(final LatLong coord) {
        if ( mapView != null && coord != null) {
//            final float zoomLevel = mapView.getZoomLevel();
            mapView.getController().animateTo(MapUtils.coordToOsmdroidGeoPoint(coord));
        }
    }

    @Override
    public void updateCamera(LatLong coord, float zoomLevel) {
        if ( mapView != null && coord != null)
        {
            mapView.getController().setZoom(zoomLevel);
            mapView.getController().animateTo(MapUtils.coordToOsmdroidGeoPoint(coord));
        }
    }

    @Override
    public void updateCameraBearing(float bearing) {
        Log.d(TAG,"updateCameraBearing");
        mapView.setMapOrientation(bearing);
    }

    private Polyline updatePath(Polyline polyLine, PathSource pathSource, @PolyLineType int polyLineType) {
        if (mapView == null || pathSource == null) return polyLine;
//        Log.d(TAG,"updatePath");
//        Polyline pl = new Polyline(mapView);
        List<LatLong> pathCoords = pathSource.getPathPoints();
        final List<GeoPoint> pathPoints = new ArrayList<GeoPoint>(pathCoords.size());
        for (LatLong coord : pathCoords) {
            pathPoints.add(MapUtils.coordToOsmdroidGeoPoint(coord));
        }
        if (pathPoints.size() < 2) {
            if (polyLine != null) {
//                polyLine.remove();
                mapView.getOverlays().remove(polyLine);
                mapView.invalidate();
                polyLine = null;
            }
            return polyLine;
        }
        if (polyLine == null) {
            polyLine = new Polyline();
            if (polyLineType == LEASH_PATH) {
                polyLine.getOutlinePaint().setColor(DRONE_LEASH_DEFAULT_COLOR);
                polyLine.getOutlinePaint().setStrokeWidth(DRONE_LEASH_DEFAULT_WIDTH);
                polyLine.setPoints(pathPoints);
            }else if (polyLineType == MISSION_PATH) {
                polyLine.getOutlinePaint().setColor(MISSION_PATH_DEFAULT_COLOR);
                polyLine.getOutlinePaint().setStrokeWidth(MISSION_PATH_DEFAULT_WIDTH);
                polyLine.setPoints(pathPoints);
            }
            mapView.getOverlays().add(polyLine);
            mapView.invalidate();
        }
        else {
            polyLine.setPoints(pathPoints);
        }
        return polyLine;
    }

    @Override
    public void updateDroneLeashPath(PathSource pathSource) {
        Log.d(TAG,"updateDroneLeashPath");
        @PolyLineType int PolyLineType = LEASH_PATH;
        mDroneLeashPath = updatePath(mDroneLeashPath, pathSource, PolyLineType);
    }

    @Override
    public void updateMissionPath(PathSource pathSource) {
//        Log.d(TAG,"updateMissionPath");
        @PolyLineType int PolyLineType = MISSION_PATH;
        mMissionPath = updatePath(mMissionPath, pathSource, PolyLineType);
    }

    @Override
    public void updatePolygonsPaths(List<List<LatLong>> paths) {
//        Log.d(TAG,"updatePolygonsPaths");
        if (mapView == null) return;

        for (Polygon poly : mPolygonsPaths) {
            if(poly != null){
                mapView.getOverlays().remove(poly);
                mapView.invalidate();
            }
        }

        for (List<LatLong> contour : paths) {
            Polygon polygon = new Polygon();
//            pathOptions.fillColor(POLYGONS_PATH_DEFAULT_COLOR);
            final List<GeoPoint> pathPoints = new ArrayList<GeoPoint>(contour.size());
            for (LatLong coord : contour) {
                pathPoints.add(MapUtils.coordToOsmdroidGeoPoint(coord));
            }
//            pathOptions.points(pathPoints);
//            mPolygonsPaths.add((Polygon)map.addOverlay(pathOptions));
            polygon .setStrokeColor(POLYGONS_PATH_DEFAULT_COLOR);
            polygon.getOutlinePaint().setStrokeWidth(POLYGONS_PATH_DEFAULT_WIDTH);
            polygon.setPoints(pathPoints);
            mapView.getOverlays().add(polygon);
            mapView.invalidate();
            mPolygonsPaths.add(polygon);
        }
    }

    @Override
    public void zoomToFit(List<LatLong> coords) {
        if(mapView == null) return;
        if (!coords.isEmpty()) {
            Log.d(TAG,"zoomToFit");
            if(coords.size() < 2) {
//                mapView.getController().setZoom(8.0);
                mapView.getController().animateTo(MapUtils.coordToOsmdroidGeoPoint(coords.get(0)));
            }
            else
            {
                final List<GeoPoint> points = new ArrayList<GeoPoint>();
                for (LatLong coord : coords)
                    points.add(MapUtils.coordToOsmdroidGeoPoint(coord));

                final int height = mapView.getHeight();
                final int width = mapView.getWidth();
                Log.d(TAG, String.format(Locale.US, "Screen W %d, H %d", width, height));

                if (height > 0 && width > 0) {
                    BoundingBox boundingBox = BoundingBox.fromGeoPoints(points);
                    final double zoom = tileSystem.getBoundingBoxZoom(boundingBox, mapView.getWidth() - 2 * BOARDER, mapView.getHeight() - 2 * BOARDER);
                    if(zoom >= mapView.getMinZoomLevel() && zoom <= mapView.getMaxZoomLevel()) {
                        mapView.invalidate();
                        mapView.zoomToBoundingBox(boundingBox, true,BOARDER);
                    }
                }
            }
        }
    }

    @Override
    public void zoomToFitMyLocation(List<LatLong> coords) {
        this.zoomToFit(coords);
    }

    @Override
    public void updateRealTimeFootprint(FootPrint footprint) {
        Log.d(TAG,"updateRealTimeFootprint");
        List<LatLong> pathPoints = footprint == null
                ? Collections.<LatLong>emptyList()
                : footprint.getVertexInGlobalFrame();

        if (pathPoints.isEmpty()) {
            if (mFootprintPoly != null) {
                mapView.getOverlays().remove(mFootprintPoly);
                mapView.invalidate();
                mFootprintPoly = null;
            }
        } else {
            if (mFootprintPoly == null) {
                mFootprintPoly = new Polygon();

                List<GeoPoint> list = new ArrayList<GeoPoint>();
                for (LatLong vertex : pathPoints) {
                    list.add(MapUtils.coordToOsmdroidGeoPoint(vertex));
                }
                mFootprintPoly.setStrokeWidth(FOOTPRINT_FILL_COLOR);
                mFootprintPoly.setStrokeColor(FOOTPRINT_DEFAULT_WIDTH);
                mFootprintPoly.setFillColor(FOOTPRINT_FILL_COLOR);
                mFootprintPoly.setPoints(list);

                mapView.getOverlays().add(mFootprintPoly);
                mapView.invalidate();
            } else {
                List<GeoPoint> list = new ArrayList<GeoPoint>();
                for (LatLong vertex : pathPoints) {
                    list.add(MapUtils.coordToOsmdroidGeoPoint(vertex));
                }
                mFootprintPoly.setPoints(list);
                mapView.invalidate();
            }
        }
    }
    private static class ProxyMapPolyline implements PolylineInfo.ProxyPolyline {

        private final Polyline polyline;

        private ProxyMapPolyline(Polyline polyline) {
            this.polyline = polyline;
        }

        @Override
        public void setPoints(@NotNull List<? extends LatLong> points) {
            polyline.setPoints(MapUtils.coordToOsmdroidGeoPoint(points));
        }

        @Override
        public void clickable(boolean clickable) {
        }

        @Override
        public void color(int color) {
            polyline.getOutlinePaint().setColor(color);
        }

        @Override
        public void geodesic(boolean geodesic) {
        }

        @Override
        public void visible(boolean visible) {
            polyline.setVisible(visible);
        }

        @Override
        public void width(float width) {
            polyline.getOutlinePaint().setStrokeWidth((int) width);
        }

        @Override
        public void zIndex(float zIndex) {
//            polyline.setStrokeWidth((int) zIndex);
        }

        @Override
        public void remove() {
//            polyline.remove();
            polyline.setVisible(false);
        }

        public void remove(MapView mapView) {
//            polyline.remove();
            mapView.getOverlays().remove(polyline);
            mapView.invalidate();
        }
    }
    private static class ProxyMapMarker implements MarkerInfo.ProxyMarker {

        private final Marker marker;

        ProxyMapMarker(Marker marker){
            this.marker = marker;
        }

        @Override
        public void setAlpha(float alpha) {
            marker.setAlpha(alpha);
        }

        @Override
        public void setAnchor(float anchorU, float anchorV) {
            marker.setAnchor(anchorU, anchorV);
        }

        @Override
        public void setDraggable(boolean draggable) {
            marker.setDraggable(draggable);
        }

        @Override
        public void setFlat(boolean flat) {
            marker.setFlat(flat);
        }

        @Override
        public void setIcon(Bitmap icon) {
            if(icon != null) {
//                int w = icon.getWidth();
//                int h = icon.getHeight();
//                Drawable drawable = new BitmapDrawable(Resources.getSystem(), icon);
//                int width = drawable.getIntrinsicWidth();
//                int height = drawable.getIntrinsicHeight();
//                Matrix matrix = new Matrix();
//                float scaleWidth = (float)w/width;
//                float scaleHeight = (float)h/height;
//                matrix.postScale(scaleWidth, scaleHeight);
//                Bitmap bitmap = Bitmap.createBitmap(icon,0,0,w,h,matrix,true);
                marker.setIcon(new BitmapDrawable(Resources.getSystem(), icon));
            }
        }

        @Override
        public void setInfoWindowAnchor(float anchorU, float anchorV) {
            marker.setInfoWindowAnchor(anchorU,anchorV);
        }

        @Override
        public void setPosition(LatLong coord) {
            if(coord != null) {
                marker.setPosition(MapUtils.coordToOsmdroidGeoPoint(coord));
            }
        }

        @Override
        public void setRotation(float rotation) {
            marker.setRotation(rotation);
        }

        @Override
        public void setSnippet(String snippet) {
            marker.setSnippet(snippet);
        }

        @Override
        public void setTitle(String title) {
            marker.setTitle(title);
        }

        @Override
        public void setVisible(boolean visible) {
            marker.setVisible(visible);
        }

        @Override
        public void removeMarker() {
            marker.setVisible(false);
        }

        public void removeMarker(MapView mapView){
            marker.remove(mapView);
        }

        @Override
        public boolean equals(Object other){
            if(this == other)
                return true;

            if(!(other instanceof ProxyMapMarker))
                return false;

            return this.marker.equals(((ProxyMapMarker) other).marker);
        }

        @Override
        public int hashCode(){
            return this.marker.hashCode();
        }
    }
}