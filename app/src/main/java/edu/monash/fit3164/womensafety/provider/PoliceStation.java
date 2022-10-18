package edu.monash.fit3164.womensafety.provider;

public class PoliceStation {
    private String Address;
    private String Email;
    private String Fax;
    private String Latitude;
    private String Longitude;
    private String Name;
    private String Opening_Hours;
    private String Phone;
    private Long Postcode;
    private String Specialty_Services;
    private String Suburb;
    private String Suburb_Cap;

    public PoliceStation() {
    }

    public PoliceStation(String address, String email, String fax, String latitude, String longitude, String name, String opening_Hours, String phone, Long postcode, String specialty_Services, String suburb, String suburb_Cap) {
        Address = address;
        Email = email;
        Fax = fax;
        Latitude = latitude;
        Longitude = longitude;
        Name = name;
        Opening_Hours = opening_Hours;
        Phone = phone;
        Postcode = postcode;
        Specialty_Services = specialty_Services;
        Suburb = suburb;
        Suburb_Cap = suburb_Cap;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFax() {
        return Fax;
    }

    public void setFax(String fax) {
        Fax = fax;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getOpening_Hours() {
        return Opening_Hours;
    }

    public void setOpening_Hours(String opening_Hours) {
        Opening_Hours = opening_Hours;
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

    public String getSpecialty_Services() {
        return Specialty_Services;
    }

    public void setSpecialty_Services(String specialty_Services) {
        Specialty_Services = specialty_Services;
    }

    public String getSuburb() {
        return Suburb;
    }

    public void setSuburb(String suburb) {
        Suburb = suburb;
    }

    public String getSuburb_Cap() {
        return Suburb_Cap;
    }

    public void setSuburb_Cap(String suburb_Cap) {
        Suburb_Cap = suburb_Cap;
    }
}
