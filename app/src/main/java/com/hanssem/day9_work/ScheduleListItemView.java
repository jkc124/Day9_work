package com.hanssem.day9_work;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScheduleListItemView extends LinearLayout {
    private Context mContext;

    /*private TextView timeText;
    private TextView messageText;*/
    private TextView weather_hour;
    private TextView weather_temp;
    private TextView weather_wfKor;
    private ImageView weather_icon;

    public ScheduleListItemView(Context context) {
        super(context);

        mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.schedule_listitem, this, true);

        weather_hour = (TextView) findViewById(R.id.weather_hour);
        weather_temp = (TextView) findViewById(R.id.weather_temp);
        weather_wfKor = (TextView) findViewById(R.id.weather_wfKor);
        weather_icon = (ImageView) findViewById(R.id.weather_icon);

    }

    public void setWeather_hour(String hourStr) {
        weather_hour.setText(hourStr);
    }

    public void setWeather_temp(String tempStr) {
        weather_temp.setText(tempStr);
    }

    public void setWeather_wfKor(String wfKorStr) {
        weather_wfKor.setText(wfKorStr);
    }

    public void setWeather_icon(int iconInt) {
        weather_icon.setImageResource(iconInt);
    }


}