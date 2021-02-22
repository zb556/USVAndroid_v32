package org.usvplanner.android.maps.providers.osmdroid_map;

import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.MapTileIndex;

/**
 * Created by acchkr on 2018/2/8.
 */

public class OsmTileSources {
    public static final OnlineTileSourceBase GoogleHybrid = new XYTileSource("Google-Hybrid",
            0, 19, 256, ".png", new String[] {
            "http://mt0.google.cn",
            "http://mt1.google.cn",
            "http://mt2.google.cn",
            "http://mt3.google.cn",

    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/vt/lyrs=y&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };

    public static final OnlineTileSourceBase GoogleSat = new XYTileSource("Google-Sat",
            0, 19, 256, ".png", new String[] {
            "http://mt0.google.cn",
            "http://mt1.google.cn",
            "http://mt2.google.cn",
            "http://mt3.google.cn",

    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/vt/lyrs=s&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };

    public static final OnlineTileSourceBase GoogleRoads = new XYTileSource("Google-Roads",
            0, 19, 256, ".png", new String[] {
            "http://mt0.google.cn",
            "http://mt1.google.cn",
            "http://mt2.google.cn",
            "http://mt3.google.cn",

    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/vt/lyrs=m&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };
}
