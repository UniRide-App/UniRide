package com.project.uniride.model;

public class GeoLocation {
    private double latitude;
    private double longitude;

    public GeoLocation() {}
    public GeoLocation(double lat, double lng) { this.latitude = lat; this.longitude = lng; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double lat) { this.latitude = lat; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double lng) { this.longitude = lng; }

    public double distanceTo(GeoLocation other) {
        final double R = 3958.8;
        double dLat = Math.toRadians(other.latitude - this.latitude);
        double dLng = Math.toRadians(other.longitude - this.longitude);
        double a = Math.sin(dLat/2)*Math.sin(dLat/2)
                + Math.cos(Math.toRadians(this.latitude))*Math.cos(Math.toRadians(other.latitude))
                * Math.sin(dLng/2)*Math.sin(dLng/2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    }
}
