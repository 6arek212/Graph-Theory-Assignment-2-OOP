package assignment2.ui.utils;

public class Range {
    private double min;
    private double max;

    public Range(double min, double max) {
        if (min == max)
        {
            this.max = 1;
            this.min = 0;
            return;
        }

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
        double x;
        if (min != max)
            x = (d - min) / (max - min);
        else
            x = 0;
        return x;
    }


    // [0 , 1] -> [min , max]
    public double getRange(double d) {
        double x = (d * (max - min)) + min;

        return x;
    }


    public double toRange(Range range, double d) {
        double x = range.getRange(this.norm(d));
        return x;
    }

    @Override
    public String toString() {
        return "Range{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
