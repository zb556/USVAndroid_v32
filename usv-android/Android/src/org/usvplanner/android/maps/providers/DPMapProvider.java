package org.usvplanner.android.maps.providers;

import org.usvplanner.android.maps.DPMap;
import org.usvplanner.android.maps.GoogleMapFragment;
import org.usvplanner.android.maps.providers.google_map.GoogleMapPrefFragment;

import org.usvplanner.android.maps.providers.baidu_map.BaiduMapFragment;
import org.usvplanner.android.maps.providers.baidu_map.BaiduMapPrefFragment;
import org.usvplanner.android.maps.providers.osmdroid_map.OsmdroidMapFragment;
import org.usvplanner.android.maps.providers.osmdroid_map.OsmdroidMapPrefFragment;

/**
 * Contains a listing of the various map providers supported, and implemented in
 * DroidPlanner.
 */
public enum DPMapProvider {
	/**
	 * Provide access to google map v2. Requires the google play services.
	 */
	GOOGLE_MAP {
		@Override
		public DPMap getMapFragment() {
			return new GoogleMapFragment();
		}

		@Override
		public MapProviderPreferences getMapProviderPreferences() {
			return new GoogleMapPrefFragment();
		}
	},

	BAIDU_MAP {
		@Override
		public DPMap getMapFragment() {	return new BaiduMapFragment(); }

		@Override
		public MapProviderPreferences getMapProviderPreferences() {
			return new BaiduMapPrefFragment();
		}
	},

	GOOGLE_CHINA_MAP {
		@Override
		public DPMap getMapFragment() {	return new OsmdroidMapFragment(); }

		@Override
		public MapProviderPreferences getMapProviderPreferences() {
			return new OsmdroidMapPrefFragment();
		}
	};
	private static DPMapProvider[] ENABLED_PROVIDERS = {
		GOOGLE_CHINA_MAP,
//		BAIDU_MAP,
		GOOGLE_MAP
	};

	/**
	 * @return the fragment implementing the map.
	 */
	public abstract DPMap getMapFragment();

	/**
	 * @return the set of preferences supported by the map.
	 */
	public abstract MapProviderPreferences getMapProviderPreferences();

	/**
	 * Returns the map type corresponding to the given map name.
	 * 
	 * @param mapName
	 *            name of the map type
	 * @return {@link DPMapProvider} object.
	 */
	public static DPMapProvider getMapProvider(String mapName) {
		if (mapName == null) {
			return null;
		}

		try {
			return DPMapProvider.valueOf(mapName);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	public static DPMapProvider[] getEnabledProviders(){
		return ENABLED_PROVIDERS;
	}

	/**
	 * By default, Google Map is the map provider.
	 */
	public static final DPMapProvider DEFAULT_MAP_PROVIDER = GOOGLE_CHINA_MAP;
}
