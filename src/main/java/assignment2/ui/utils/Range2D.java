package assignment2.ui.utils;

import assignment2.api.GeoLocation;
import assignment2.models.GeoLocationImpl;

public class Range2D {
    private Range _y_range;
    private Range _x_range;


    public Range2D(Range x, Range y) {
        _x_range = new Range(x);
        _y_range = new Range(y);
    }


    public Range2D(Range2D w) {
        _x_range = new Range(w._x_range);
        _y_range = new Range(w._y_range);
    }


    public double getMaxX() {
        return _x_range.get_max();
    }


    public double getMaxY() {
        return _y_range.get_max();
    }


    public GeoLocationImpl getPortion(GeoLocation p) {
        double x = _x_range.getPortion(p.x());
        double y = _y_range.getPortion(p.y());
        return new GeoLocationImpl(x, y, 0);
    }

    public GeoLocationImpl fromPortion(GeoLocation p) {
        double x = _x_range.fromPortion(p.x());
        double y = _y_range.fromPortion(p.y());
        return new GeoLocationImpl(x, y, 0);
    }
}