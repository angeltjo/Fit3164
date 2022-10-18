package edu.monash.fit3164.womensafety.provider;

public class Emergency {
    private String Address;
    private String Fax;
    private Float Latitude;
    private Float Longitude;
    private String Name;
    private String Phone;
    private Long Postcode;
    private String Suburb;

    public Emergency() {
    }

    public Emergency(String address, String fax, Float latitude, Float longitude, String name, String phone, Long postcode, String suburb) {
        Address = address;
        Fax = fax;
        Latitude = latitude;
        Longitude = longitude;
        Name = name;
        Phone = phone;
        Postcode = postcode;
        Suburb = suburb;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getFax() {
        return Fax;
    }

    public void setFax(String fax) {
        Fax = fax;
    }

    public Float getLatitude() {
        return Latitude;
    }

    public void setLatitude(Float latitude) {
        Latitude = latitude;
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

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public Long getPostcode() {
        return Postcode;
    }

    public void setPostcode(Long postcode) {
        Postcode = postcode;
    }

    public String getSuburb() {
        return Suburb;
    }

    public void setSuburb(String suburb) {
        Suburb = suburb;
    }
}
