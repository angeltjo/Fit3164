package edu.monash.fit3164.womensafety.provider;

public class MelSuburbs {
    private Float Lattitude;
    private Float Longtitude;
    private String Regionname;
    private String Suburb;
    private int Postcode;

    public MelSuburbs() {
    }

    public MelSuburbs(Float lattitude, Float longtitude, String regionname, String suburb, int postcode) {
        Lattitude = lattitude;
        Longtitude = longtitude;
        Regionname = regionname;
        Suburb = suburb;
        Postcode = postcode;
    }

    public int getPostcode() {
        return Postcode;
    }

    public void setPostcode(int postcode) {
        Postcode = postcode;
    }

    public Float getLattitude() {
        return Lattitude;
    }

    public void setLattitude(Float lattitude) {
        Lattitude = lattitude;
    }

    public Float getLongtitude() {
        return Longtitude;
    }

    public void setLongtitude(Float longtitude) {
        Longtitude = longtitude;
    }

    public String getRegionname() {
        return Regionname;
    }

    public void setRegionname(String regionname) {
        Regionname = regionname;
    }

    public String getSuburb() {
        return Suburb;
    }

    public void setSuburb(String suburb) {
        Suburb = suburb;
    }
}
