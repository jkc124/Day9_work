package com.hanssem.day9_work;

public class ScheduleListItem {
    private String time;
    private String message;
    private String hour;
    private String temp;
    private String wkKor;
    private int icon;

    private String tmEf;
    private String wf;
    private String tmn;
    private String tmx;



    public ScheduleListItem() {

    }

    public ScheduleListItem(String inTime, String inMessage) {
        time = inTime;
        message = inMessage;
    }

    public ScheduleListItem(String inHour, String inTemp, String inWfKor){
        hour = inHour;
        temp = inTemp;
        wkKor = inWfKor;
    }

    public ScheduleListItem(String inHour, String inTemp, String inWfKor, int inIcon){
        hour = inHour;
        temp = inTemp;
        wkKor = inWfKor;
        icon = inIcon;
    }

    public ScheduleListItem(String inTmEf, String inWf, String inTmn, String inTmx ,int inIcon){
        tmEf = inTmEf;
        wf = inWf;
        tmn = inTmn;
        tmx = inTmx;
        icon = inIcon;
    }




    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    // 날씨정보
    public String getHour(){return hour;}

    public void setHour(String hour){
        this.hour = hour;
    }

    public String getTemp(){return temp;}

    public void setTemp(String temp){
        this.temp = temp;
    }

    public String getWkKor(){return wkKor;}

    public void setWkKor(String wkKor){
        this.wkKor = wkKor;
    }

    public int getIcon(){return icon;}

    public void setIcon(int icon){
        this.icon = icon;
    }

    //new
    public String getTmEf(){return tmEf;}

    public void setTmEf(String tmEf){
        this.tmEf = tmEf;
    }

    public String getWf(){return wf;}

    public void setWf(String wf){
        this.wf = wf;
    }

    public String getTmx(){return tmx;}

    public void setTmn(String tmn){
        this.tmn = tmn;
    }

    public String getTmn(){return tmn;}

    public void setTmx(String tmx){
        this.tmx = tmx;
    }


}