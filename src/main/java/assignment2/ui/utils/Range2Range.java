package assignment2.ui.utils;


import assignment2.api.GeoLocation;
import assignment2.models.GeoLocationImpl;

public class Range2Range {
    private Range2D world, frame;

    public Range2Range(Range2D w, Range2D f) {
        world = new Range2D(w);
        frame = new Range2D(f);
    }


    public GeoLocation frameToWorld(GeoLocation p) {
        GeoLocationImpl d = frame.getPortion(p);
        return world.fromPortion(d);
    }


    public GeoLocation worldToframe(GeoLocation p) {
        GeoLocationImpl d = world.getPortion(p);
        return frame.fromPortion(d);
    }

    public Range2D getFrame() {
        return frame;
    }

    public Range2D getWorld() {
        return world;
    }

    public void updateFrame(Range2D frame) {
        this.frame = new Range2D(frame);
    }

    public void updateWorld(Range2D world) {
        this.world = new Range2D(world);
    }
}