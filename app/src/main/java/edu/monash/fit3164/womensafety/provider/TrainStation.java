package edu.monash.fit3164.womensafety.provider;

public class TrainStation {

    private String Code;
    private String Date_opened;
    private String Distance_from_Southern_Cross;
    private Float Latitude;
    private String Line;
    private Float Longitude;
    private String Name;


    public TrainStation() {
    }

    public TrainStation(String code, String date_opened, String distance_from_Southern_Cross, Float latitude, String line, Float longitude, String name) {
        Code = code;
        Date_opened = date_opened;
        Distance_from_Southern_Cross = distance_from_Southern_Cross;
        Latitude = latitude;
        Line = line;
        Longitude = longitude;
        Name = name;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getDate_opened() {
        return Date_opened;
    }

    public void setDate_opened(String date_opened) {
        Date_opened = date_opened;
    }

    public String getDistance_from_Southern_Cross() {
        return Distance_from_Southern_Cross;
    }

    public void setDistance_from_Southern_Cross(String distance_from_Southern_Cross) {
        Distance_from_Southern_Cross = distance_from_Southern_Cross;
    }

    public Float getLatitude() {
        return Latitude;
    }

    public void setLatitude(Float latitude) {
        Latitude = latitude;
    }

    public String getLine() {
        return Line;
    }

    public void setLine(String line) {
        Line = line;
    }

    public Float getLongitude() {
        return Longitude;
    }

    public void setLongitude(Float longitude) {
        Longitude = longitude;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
