package edu.monash.fit3164.womensafety.provider;

public class CameraInfo {
    private String Location;
    private String Suburb;

    public CameraInfo() {
    }

    public CameraInfo(String location, String suburb) {
        Location = location;
        Suburb = suburb;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getSuburb() {
        return Suburb;
    }

    public void setSuburb(String suburb) {
        Suburb = suburb;
    }
}
