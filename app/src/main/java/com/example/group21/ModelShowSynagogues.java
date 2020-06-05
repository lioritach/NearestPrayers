package com.example.group21;

public class ModelShowSynagogues {

    private String synId, synName, city, country, fullAddress, category, shacharit, minha, arvit,
            negishut_nehim, negishut_nashim, events, synImage, timestamp, uid, negishotNote, negishutAvailable;


    public ModelShowSynagogues() {
    }

    public ModelShowSynagogues(String synId, String synName, String city, String country, String fullAddress,
                               String category, String shacharit, String minha, String arvit, String negishut_nehim,
                               String negishut_nashim, String events, String synImage, String timestamp, String uid, String negishutAvailable,
                               String negishotNote) {
        this.synId = synId;
        this.synName = synName;
        this.city = city;
        this.country = country;
        this.fullAddress = fullAddress;
        this.category = category;
        this.shacharit = shacharit;
        this.minha = minha;
        this.arvit = arvit;
        this.negishut_nehim = negishut_nehim;
        this.negishut_nashim = negishut_nashim;
        this.events = events;
        this.synImage = synImage;
        this.timestamp = timestamp;
        this.uid = uid;
        this.negishotNote = negishotNote;
        this.negishutAvailable = negishutAvailable;
    }

    public String getNegishutAvailable() {
        return negishutAvailable;
    }

    public void setNegishutAvailable(String negishutAvailable) {
        this.negishutAvailable = negishutAvailable;
    }

    public String getNegishotNote() {
        return negishotNote;
    }

    public void setNegishotNote(String negishotNote) {
        this.negishotNote = negishotNote;
    }



    public String getSynId() {
        return synId;
    }

    public void setSynId(String synId) {
        this.synId = synId;
    }

    public String getSynName() {
        return synName;
    }

    public void setSynName(String synName) {
        this.synName = synName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getShacharit() {
        return shacharit;
    }

    public void setShacharit(String shacharit) {
        this.shacharit = shacharit;
    }

    public String getMinha() {
        return minha;
    }

    public void setMinha(String minha) {
        this.minha = minha;
    }

    public String getArvit() {
        return arvit;
    }

    public void setArvit(String arvit) {
        this.arvit = arvit;
    }

    public String getNegishut_nehim() {
        return negishut_nehim;
    }

    public void setNegishut_nehim(String negishut_nehim) {
        this.negishut_nehim = negishut_nehim;
    }

    public String getNegishut_nashim() {
        return negishut_nashim;
    }

    public void setNegishut_nashim(String negishut_nashim) {
        this.negishut_nashim = negishut_nashim;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public String getSynImage() {
        return synImage;
    }

    public void setSynImage(String synImage) {
        this.synImage = synImage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
