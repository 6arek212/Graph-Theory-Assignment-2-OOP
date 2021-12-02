package implementation;


import api.GeoLocation;

public class GeoLocationImpl implements GeoLocation {
    private double x;
    private double y;
    private double z;


    public GeoLocationImpl(String g) {
        String[] geos = g.split(",");
        this.x = Double.parseDouble(geos[0]);
        this.y = Double.parseDouble(geos[1]);
        this.z = Double.parseDouble(geos[2]);
    }
    public GeoLocationImpl(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public GeoLocationImpl(GeoLocation g) {
        if (!(g instanceof GeoLocationImpl))
            throw new ClassCastException();
        this.x = ((GeoLocationImpl) g).x;
        this.y = ((GeoLocationImpl) g).y;
        this.z = ((GeoLocationImpl) g).z;
    }

    @Override
    public double x() {

        return this.x;
    }

    @Override
    public double y() {

        return this.y;
    }

    @Override
    public double z() {
        return this.z;
    }

    @Override
    public double distance(GeoLocation g) {

        double dx = Math.pow(this.x - g.x(), 2);
        double dy = Math.pow(this.x - g.y(), 2);
        double dz = Math.pow(this.x - g.z(), 2);


        return Math.sqrt(dx + dy + dz);

    }
    public boolean equals(Object p) {
        if (p == null || !(p instanceof GeoLocation)) {
            return false;
        }
        GeoLocationImpl p2 = (GeoLocationImpl) p;
        return ((this.x == p2.x) && (this.y == p2.y) && (this.z == p2.z));
    }

    @Override
    public String toString() {
        return "GeoLocationImpl{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
