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
    private TextView weather_wf;
    private TextView weather_tmEf;
    private TextView weather_tmx;
    private TextView weather_tmn;
    private ImageView weather_icon;

    public ScheduleListItemView(Context context) {
        super(context);

        mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.schedule_listitem, this, true);

        weather_tmEf = (TextView) findViewById(R.id.weather_tmEf);
        weather_wf = (TextView) findViewById(R.id.weather_wf);
        weather_tmn = (TextView) findViewById(R.id.weather_tmn);
        weather_tmx = (TextView) findViewById(R.id.weather_tmx);
        weather_icon = (ImageView) findViewById(R.id.weather_icon);

    }



    public void setWeather_tmEf(String tmEfStr) {
        weather_tmEf.setText(tmEfStr);
    }

    public void setWeather_wf(String wfStr) {
        weather_wf.setText(wfStr);
    }

    public void setWeather_tmn(String tmnStr) {
        weather_tmn.setText(tmnStr);
    }

    public void setWeather_tmx(String tmxStr) {
        weather_tmx.setText(tmxStr);
    }

    public void setWeather_icon(int iconInt) {
        weather_icon.setImageResource(iconInt);
    }


}