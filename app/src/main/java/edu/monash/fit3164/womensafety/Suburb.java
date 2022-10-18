package edu.monash.fit3164.womensafety;

public class Suburb {
    private String name;
    private Long postcode;

    public Suburb() {
    }

    public Suburb(String name, Long postcode) {
        this.name = name;
        this.postcode = postcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPostcode() {
        return postcode;
    }

    public void setPostcode(Long postcode) {
        this.postcode = postcode;
    }
}
