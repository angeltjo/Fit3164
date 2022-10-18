package edu.monash.fit3164.womensafety.provider;

public class CrimeStats {

    private String Suburb;
    private int Crimes_Against_Person;
    private int Drug_Off;
    private int JusticeProc_Off;
    private int Other;
    private int Postcode;
    private int Prop_Deception_Off;
    private int PublicOrder_Security_Off;
    private int Total;

    public CrimeStats() {
    }

    public CrimeStats(String suburb, int crimes_Against_Person, int drug_Off, int justiceProc_Off, int other, int postcode, int prop_Deception_Off, int publicOrder_Security_Off, int total) {
        Suburb = suburb;
        Crimes_Against_Person = crimes_Against_Person;
        Drug_Off = drug_Off;
        JusticeProc_Off = justiceProc_Off;
        Other = other;
        Postcode = postcode;
        Prop_Deception_Off = prop_Deception_Off;
        PublicOrder_Security_Off = publicOrder_Security_Off;
        Total = total;
    }

    public String getSuburb() {
        return Suburb;
    }

    public void setSuburb(String suburb) {
        Suburb = suburb;
    }

    public int getCrimes_Against_Person() {
        return Crimes_Against_Person;
    }

    public void setCrimes_Against_Person(int crimes_Against_Person) {
        Crimes_Against_Person = crimes_Against_Person;
    }

    public int getDrug_Off() {
        return Drug_Off;
    }

    public void setDrug_Off(int drug_Off) {
        Drug_Off = drug_Off;
    }

    public int getJusticeProc_Off() {
        return JusticeProc_Off;
    }

    public void setJusticeProc_Off(int justiceProc_Off) {
        JusticeProc_Off = justiceProc_Off;
    }

    public int getOther() {
        return Other;
    }

    public void setOther(int other) {
        Other = other;
    }

    public int getPostcode() {
        return Postcode;
    }

    public void setPostcode(int postcode) {
        Postcode = postcode;
    }

    public int getProp_Deception_Off() {
        return Prop_Deception_Off;
    }

    public void setProp_Deception_Off(int prop_Deception_Off) {
        Prop_Deception_Off = prop_Deception_Off;
    }

    public int getPublicOrder_Security_Off() {
        return PublicOrder_Security_Off;
    }

    public void setPublicOrder_Security_Off(int publicOrder_Security_Off) {
        PublicOrder_Security_Off = publicOrder_Security_Off;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }
}
