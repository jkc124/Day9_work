package com.hanssem.day9_work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ScheduleListAdapter extends BaseAdapter {
    private Context mContext;

    // schedule List
    ArrayList<ScheduleListItem> scheduleList;

    public ScheduleListAdapter(Context context) {
        mContext = context;

        scheduleList = new ArrayList<ScheduleListItem>();
    }

    public void clear() {
        scheduleList.clear();
    }

    public void addItem(ScheduleListItem item) {
        scheduleList.add(item);
    }

    public void removeItem(ScheduleListItem item) {
        scheduleList.remove(item);
    }

    public void addAll(ArrayList<ScheduleListItem> items) {
        scheduleList.addAll(items);
    }

    public int getCount() {
        return scheduleList.size();
    }

    public Object getItem(int position) {
        return scheduleList.get(position);
    }

    public boolean isSelectable(int position) {
        return true;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ScheduleListItemView itemView = null;

        if (convertView == null) {
            itemView = new ScheduleListItemView(mContext);
        } else {
            itemView = (ScheduleListItemView) convertView;
        }

        ScheduleListItem curItem = (ScheduleListItem) scheduleList.get(position);
        itemView.setWeather_tmEf(curItem.getTmEf());
        itemView.setWeather_wf(curItem.getWf());
        itemView.setWeather_tmn(curItem.getTmn());
        itemView.setWeather_tmx(curItem.getTmx());
        itemView.setWeather_icon(curItem.getIcon());

        return itemView;
    }
}
