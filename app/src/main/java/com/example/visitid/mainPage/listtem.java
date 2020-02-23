package com.example.visitid.mainPage;


public class listtem {

    String nameOrg;
    String startTime;

    String endTime;


    String key;
    String location;
    String nameEvent;
    String bitm;

    public listtem(String nameEvent, String nameOrg, String startTime, String endTime, String location, String bitm, String key){

        this.endTime = endTime;
        this.nameEvent = nameEvent;
        this.nameOrg = nameOrg;
        this.bitm = bitm;
        this.startTime = startTime;
        this.location = location;
        this.key = key;
    }
    public String getNameEvent() {
        return nameEvent;
    }
    public String getKey() {
        return key;
    }


    public String getNameOrg() {
        return nameOrg;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getBitm(){ return bitm; }

    public String getEndTime() {
        return endTime;
    }



    public String getLocation() {
        return location;
    }

}