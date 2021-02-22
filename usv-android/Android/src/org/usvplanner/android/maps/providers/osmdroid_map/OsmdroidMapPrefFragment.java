package org.usvplanner.android.maps.providers.osmdroid_map;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;

import org.usvplanner.android.R;
import org.usvplanner.android.maps.providers.DPMapProvider;
import org.usvplanner.android.maps.providers.MapProviderPreferences;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;

/**
 * This is the osmdroid map provider preferences. It stores and handles all
 * preferences related to osmdroid map.
 */
public class OsmdroidMapPrefFragment extends MapProviderPreferences {
	// Common defines
	private static final String PREF_MAP_TYPE = "pref_osmdroid_map_type";
	private static final String MAP_TYPE_SATELLITE = "satellite";
	private static final String MAP_TYPE_HYBRIDE = "hybrid";
	private static final String MAP_TYPE_NORMAL = "normal";
	private static final String DEFAULT_MAP_TYPE = MAP_TYPE_SATELLITE;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_osmdroid_maps);
		setupPreferences();
	}

    @Override
    public DPMapProvider getMapProvider() {
        return DPMapProvider.GOOGLE_CHINA_MAP;
    }
    private void setupPreferences() {
		final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
		setupMapTypePreferences(sharedPref);
	}

	public OnlineTileSourceBase getMapType(Context context) {
		final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		String mapType = sharedPref.getString(PREF_MAP_TYPE, DEFAULT_MAP_TYPE);

		if (mapType.equalsIgnoreCase(MAP_TYPE_SATELLITE)) {
			return OsmTileSources.GoogleSat;
		}
		else if (mapType.equalsIgnoreCase(MAP_TYPE_NORMAL)) {
			return OsmTileSources.GoogleSat;
		}
		else if (mapType.equalsIgnoreCase(MAP_TYPE_HYBRIDE)) {
			return OsmTileSources.GoogleSat;
		}
		else {
			return OsmTileSources.GoogleSat;
		}
	}

    private void setupMapTypePreferences(SharedPreferences sharedPref) {
		final String mapTypeKey = PREF_MAP_TYPE;
		final Preference mapTypePref = findPreference(mapTypeKey);
		if(mapTypePref != null) {
			final String mapType = sharedPref.getString(mapTypeKey, DEFAULT_MAP_TYPE);
			mapTypePref.setSummary(mapType);
			mapTypePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					mapTypePref.setSummary(newValue.toString());
					return true;
				}
			});
		}
    }
}
