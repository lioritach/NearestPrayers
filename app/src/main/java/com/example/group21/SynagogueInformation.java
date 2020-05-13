package com.example.group21;

public class SynagogueInformation {

    public String name, lat, lon, street, neighborho;


    public SynagogueInformation() {
    }

    public SynagogueInformation(String name, String lat, String lon, String street, String neighborho) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.street = street;
        this.neighborho = neighborho;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNeighborho() {
        return neighborho;
    }

    public void setNeighborho(String neighborho) {
        this.neighborho = neighborho;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLat() {
        return lat;
    }


    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
