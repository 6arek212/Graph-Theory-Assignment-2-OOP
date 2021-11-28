package ui.utils;

public class Range {
    double min;
    double max;

    public Range(double min, double max) {
        this.max = max;
        this.min = min;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    //R -> [0,1]
    public double norm(double d) {

        double x = (d - min) / (max - min);

        return x;
    }


    // [0 , 1] -> [min , max]
    public double getRange(double d) {
        double x = (d * (max - min)) + min;
        return x;
    }


    public double toRange(Range range, double d) {
        return range.getRange(this.norm(d));
    }
}
