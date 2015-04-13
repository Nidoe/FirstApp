package com.nidoe.firstapp;

import org.json.JSONObject;

/**
 * Created by luvz on 4/7/2015.
 */
public class Stream {

    private int icon;
    private String test;
    private String displayName;
    private String followers;
    private String gameName;
    public static String names;

    public String getUrl() {
        return url;
    }

    private String url;
    public Stream(JSONObject object) {
        try {
            this.displayName = object.getString("display_name");
            this.followers = object.getString("followers");
            this.gameName = object.getString("game");
            this.url = object.getString("url");
            names+= object.getString("name") + ",";

        }
        catch (Exception e){
            this.gameName = "Error";
            this.followers = "Not found";
            this.displayName = "Error";
        }
    }
    public Stream(String displayName, String gameName, String followers){
        super();
        this.displayName = displayName;
        this.gameName = gameName;
        this.followers = followers;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
