package edu.monash.fit3164.womensafety.provider;

public class User {
    public String username;
    public String email;
    public String mobile;
    public String profileImageUri;
    public String smsMessage;

    public User(){

    }

    public User(String username, String email, String mobile, String profileImageUri, String smsMessage){
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.profileImageUri = profileImageUri;
        this.smsMessage = smsMessage;
    }

}
