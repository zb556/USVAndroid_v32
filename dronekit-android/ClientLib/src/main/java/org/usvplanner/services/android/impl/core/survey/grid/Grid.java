package org.usvplanner.services.android.impl.core.survey.grid;

import org.usvplanner.services.android.impl.core.helpers.geoTools.PolylineTools;
import com.o3dr.services.android.lib.coordinate.LatLong;

import java.util.List;

public class Grid {
    public List<LatLong> gridPoints;
    private List<LatLong> cameraLocations;

    public Grid(List<LatLong> list, List<LatLong> cameraLocations) {
        this.gridPoints = list;
        this.cameraLocations = cameraLocations;
    }

    public double getLength() {
        return PolylineTools.getPolylineLength(gridPoints);
    }

    public int getNumberOfLines() {
        return gridPoints.size() / 2;
    }

    public List<LatLong> getCameraLocations() {
        return cameraLocations;
    }

    public int getCameraCount() {
        return cameraLocations.size();
    }
    public void add(Grid grid) {
        for(int i = 0;i < grid.gridPoints.size();i++) {
            this.gridPoints.add(grid.gridPoints.get(i));
        }
        for(int i = 0;i < grid.cameraLocations.size();i++) {
            this.cameraLocations.add(grid.cameraLocations.get(i));
        }
    }
}