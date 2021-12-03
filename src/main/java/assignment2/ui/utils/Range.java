package assignment2.ui.utils;


public class Range {
    private double _min, _max;


    public Range(double min, double max) {
        set_min(min);
        set_max(max);
    }

    public Range(Range x) {
        this(x._min, x._max);
    }


    public boolean isEmpty() {
        return this.get_min()>this.get_max();
    }
    public double get_max() {
        return _max;
    }
    public double get_length() {
        return _max-_min;
    }

    private void set_max(double _max) {
        this._max = _max;
    }
    public double get_min() {
        return _min;
    }
    private void set_min(double _min) {
        this._min = _min;
    }


    public double getPortion(double d) {
        double d1 = d-_min;
        double ans = d1/get_length();
        return ans;
    }


    public double fromPortion(double p) {
        return _min+p* get_length();
    }
}