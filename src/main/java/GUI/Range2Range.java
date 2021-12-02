package GUI;

import api.GeoLocation;
import implementation.GeoLocationImpl;

public class Range2Range {
    private Range2D world, frame;

    public Range2Range(Range2D w, Range2D f) {
        world = new Range2D(w);
        frame = new Range2D(f);
    }


    public GeoLocation worldToframe(GeoLocation p) {
        GeoLocationImpl d = world.getPortion(p);
        GeoLocationImpl ans = frame.fromPortion(d);
        return ans;
    }
    public Range2D getFrame() {
        return frame;
    }
}