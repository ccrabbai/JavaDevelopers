package com.example.android.javadevelopers;


public class Developers {
    //Developers username
    private String mUsername;

    //Developer's url
    private String mGithubUrl;

    //Developer's activity_profile image
    private String mImageUrl;

    //Constructor for the activity_main.xml
    public Developers(String username, String imageUrl) {
        mUsername = username;
        mImageUrl = imageUrl;
    }

    //Constructor for activity_profile.xmlofile.xml
    public Developers(String username, String githubUrl, String imageUrl) {
        mUsername = username;
        mGithubUrl = githubUrl;
        mImageUrl = imageUrl;
    }

    //Get the username
    public String getUsername() { return mUsername; }

    //Get the url
    public String getGithubUrl() { return mGithubUrl; }

    //Get the activity_profile image
    public String getImageUrl() { return mImageUrl; }
}