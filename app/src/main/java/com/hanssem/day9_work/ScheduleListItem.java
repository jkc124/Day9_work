package com.hanssem.day9_work;

public class ScheduleListItem {
    private String time;
    private String message;
    private String hour;
    private String temp;
    private String wkKor;
    private int icon;


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
}