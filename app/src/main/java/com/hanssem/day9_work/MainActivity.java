package com.hanssem.day9_work;

import java.net.URL;
import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class MainActivity extends ActionBarActivity {

    GridView monthView;
    CalendarMonthAdapter monthViewAdapter;

    TextView monthText;

    int curYear;
    int curMonth;

    int curPosition;
    EditText scheduleInput;
    Button saveButton;

    ListView scheduleList;
    ScheduleListAdapter scheduleAdapter;
    ArrayList<ScheduleListItem> outScheduleList;

    public static final int REQUEST_CODE_SCHEDULE_INPUT = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        monthView = (GridView) findViewById(R.id.monthView);
        monthViewAdapter = new CalendarMonthAdapter(this);
        monthView.setAdapter(monthViewAdapter);

        // set listener
        monthView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MonthItem curItem = (MonthItem) monthViewAdapter.getItem(position);
                int day = curItem.getDay();

                monthViewAdapter.setSelectedPosition(position);
                monthViewAdapter.notifyDataSetChanged();

                // set schedule to the TextView
                curPosition = position;

                outScheduleList = monthViewAdapter.getSchedule(position);
                if (outScheduleList == null) {
                    outScheduleList = new ArrayList<ScheduleListItem>();
                }
                scheduleAdapter.scheduleList = outScheduleList;

                scheduleAdapter.notifyDataSetChanged();
            }
        });

        monthText = (TextView) findViewById(R.id.monthText);
        setMonthText();

        Button monthPrevious = (Button) findViewById(R.id.monthPrevious);
        monthPrevious.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                monthViewAdapter.setPreviousMonth();
                monthViewAdapter.notifyDataSetChanged();

                setMonthText();
            }
        });

        Button monthNext = (Button) findViewById(R.id.monthNext);
        monthNext.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                monthViewAdapter.setNextMonth();
                monthViewAdapter.notifyDataSetChanged();

                setMonthText();
            }
        });

        // 날씨저장 버튼 이벤트
        Button weather_add_btn = (Button)findViewById(R.id.weather_save_btn);
        weather_add_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // 날씨 xml파싱
                GetXML xml=new GetXML();
                /*xml.execute("http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=1165059000"); // 방배본동 날씨 주소*/
                xml.execute("http://www.kma.go.kr/weather/forecast/mid-term-rss3.jsp?stnId=109"); // 중부지방 주간날씨
                /*showScheduleInput();*/
            }
        });


        curPosition = -1;

        scheduleList = (ListView)findViewById(R.id.scheduleList);
        scheduleAdapter = new ScheduleListAdapter(this);
        scheduleList.setAdapter(scheduleAdapter);

    }


    private void setMonthText() {
        curYear = monthViewAdapter.getCurYear();
        curMonth = monthViewAdapter.getCurMonth();

        monthText.setText(curYear + "년 " + (curMonth + 1) + "월");
    }


    private void showScheduleInput() {
        Intent intent = new Intent(this, ScheduleInputActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCHEDULE_INPUT);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (intent != null) {
            if (requestCode == REQUEST_CODE_SCHEDULE_INPUT) {
                String time = intent.getStringExtra("time");
                String message = intent.getStringExtra("message");

                if (message != null) {
                    Toast toast = Toast.makeText(getBaseContext(), "result code : " + resultCode + ", time : " + time + ", message : " + message, Toast.LENGTH_LONG);
                    toast.show();

                    ScheduleListItem aItem = new ScheduleListItem(time, message);
                    /*ScheduleListItem wItem = new ScheduleListItem(hour, temp, wfKor);*/


                    if (outScheduleList == null) {
                        outScheduleList = new ArrayList<ScheduleListItem>();
                    }
                    outScheduleList.add(aItem);

                    monthViewAdapter.putSchedule(curPosition, outScheduleList);

                    scheduleAdapter.scheduleList = outScheduleList;
                    scheduleAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public class GetXML extends AsyncTask<String, Void, Document>{

        Document doc;
        @Override
        protected Document doInBackground(String... urls){
            URL url;
            try{
                url = new URL(urls[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder(); //XML 빌더 객체를 생성
                doc = db.parse(new InputSource(url.openStream())); //XML 파싱.
                doc.getDocumentElement().normalize();
            }catch(Exception e){
                e.printStackTrace();
            }
            return doc;
        }

        @Override
        protected void onPostExecute(Document doc) {
            NodeList nodeList = doc.getElementsByTagName("data"); // 파싱된 xml의 data 태그를 가져와 nodeList에 담음

            ArrayList<String> get_tmEf = new ArrayList<String>(); //날짜를 담을 배열
            ArrayList<String> get_wf = new ArrayList<String>(); //날씨정보 담을 배열
            ArrayList<String> get_tmn = new ArrayList<String>(); //최저 온도를 담을 배열
            ArrayList<String> get_tmx = new ArrayList<String>(); //최고 온도를 담을 배열
            ArrayList<Integer> get_icon = new ArrayList<Integer>(); // 아이콘을 담을 배열


            for(int i=0; i<13; i++){
                Node node = nodeList.item(i);
                Element element = (Element)node;

                // 날짜
                NodeList tmEf = element.getElementsByTagName("tmEf");
                Node text1 = (Node)tmEf.item(0).getFirstChild();
                System.out.println("###########################"+text1.getNodeValue().substring(11, 16));
                if(text1.getNodeValue().substring(11, 16).contains("00:00")){
                    String strTmEf = text1.getNodeValue().substring(5, 11)+"오전";
                    get_tmEf.add(strTmEf);
                }else if(text1.getNodeValue().substring(11, 16).contains("12:00")){
                    String strTmEf = text1.getNodeValue().substring(5, 11)+"오후";
                    get_tmEf.add(strTmEf);
                }

                // 날씨정보
                NodeList wf = element.getElementsByTagName("wf");
                Node text2 = (Node)wf.item(0).getFirstChild();
                String strWf = text2.getNodeValue();
                get_wf.add(strWf);

                // 최저온도
                NodeList tmn = element.getElementsByTagName("tmn");
                Node text3 = (Node)tmn.item(0).getFirstChild();
                String strTmn = text3.getNodeValue();
                get_tmn.add(strTmn);

                // 최고온도
                NodeList tmx = element.getElementsByTagName("tmx");
                Node text4 = (Node)tmx.item(0).getFirstChild();
                String strTmx = text4.getNodeValue();
                get_tmx.add(strTmx);

                System.out.println("##############" + get_tmEf.get(i));
                System.out.println("##############" + get_wf.get(i));
                System.out.println("##############" + get_tmn.get(i));
                System.out.println("##############" + get_tmx.get(i));

            }

            // 아이콘
            for(int j=0; j<13; j++){
                int icon = getIcon(get_wf.get(j).toString().trim());
                get_icon.add(icon);
            }

            ScheduleListItem wItem1 = new ScheduleListItem(get_tmEf.get(0), get_wf.get(0), get_tmn.get(0), get_tmx.get(0), get_icon.get(0));
            ScheduleListItem wItem2 = new ScheduleListItem(get_tmEf.get(1), get_wf.get(1), get_tmn.get(1), get_tmx.get(1), get_icon.get(1));
            ScheduleListItem wItem3 = new ScheduleListItem(get_tmEf.get(2), get_wf.get(2), get_tmn.get(2), get_tmx.get(2), get_icon.get(2));
            ScheduleListItem wItem4 = new ScheduleListItem(get_tmEf.get(3), get_wf.get(3), get_tmn.get(3), get_tmx.get(3), get_icon.get(3));
            ScheduleListItem wItem5 = new ScheduleListItem(get_tmEf.get(4), get_wf.get(4), get_tmn.get(4), get_tmx.get(4), get_icon.get(4));
            ScheduleListItem wItem6 = new ScheduleListItem(get_tmEf.get(5), get_wf.get(5), get_tmn.get(5), get_tmx.get(5), get_icon.get(5));
            ScheduleListItem wItem7 = new ScheduleListItem(get_tmEf.get(6), get_wf.get(6), get_tmn.get(6), get_tmx.get(6), get_icon.get(6));
            ScheduleListItem wItem8 = new ScheduleListItem(get_tmEf.get(7), get_wf.get(7), get_tmn.get(7), get_tmx.get(7), get_icon.get(7));
            ScheduleListItem wItem9 = new ScheduleListItem(get_tmEf.get(8), get_wf.get(8), get_tmn.get(8), get_tmx.get(8), get_icon.get(8));
            ScheduleListItem wItem10 = new ScheduleListItem(get_tmEf.get(9), get_wf.get(9), get_tmn.get(9), get_tmx.get(9), get_icon.get(9));
            ScheduleListItem wItem11 = new ScheduleListItem(get_tmEf.get(10), get_wf.get(10), get_tmn.get(10), get_tmx.get(10), get_icon.get(10));
            ScheduleListItem wItem12 = new ScheduleListItem(get_tmEf.get(11), get_wf.get(11), get_tmn.get(11), get_tmx.get(11), get_icon.get(11));
            ScheduleListItem wItem13 = new ScheduleListItem(get_tmEf.get(12), get_wf.get(12), get_tmn.get(12), get_tmx.get(12), get_icon.get(12));





            if (outScheduleList == null) {
                outScheduleList = new ArrayList<ScheduleListItem>();
            }
            outScheduleList.add(wItem1);
            outScheduleList.add(wItem2);
            outScheduleList.add(wItem3);
            outScheduleList.add(wItem4);
            outScheduleList.add(wItem5);
            outScheduleList.add(wItem6);
            outScheduleList.add(wItem7);
            outScheduleList.add(wItem8);
            outScheduleList.add(wItem9);
            outScheduleList.add(wItem10);
            outScheduleList.add(wItem11);
            outScheduleList.add(wItem12);
            outScheduleList.add(wItem13);

            System.out.println(outScheduleList.get(0));
            monthViewAdapter.putSchedule(curPosition, outScheduleList);

            scheduleAdapter.scheduleList = outScheduleList;
            scheduleAdapter.notifyDataSetChanged();

            /*super.onPostExecute(doc);*/
        }

        // 날씨 아이콘
        private int getIcon(String weather){
            if(weather.equals("맑음")){
                return R.drawable.nb01;
            }else if(weather.equals("구름조금")){
                return R.drawable.nb02;
            }else if(weather.equals("구름많음")){
                return R.drawable.nb03;
            }else if(weather.equals("흐림")) {
                return R.drawable.nb04;
            }else if(weather.equals("비")) {
                return R.drawable.nb08;
            }else if(weather.equals("눈")) {
                return R.drawable.nb11;
            }else if(weather.equals("비/눈")) {
                return R.drawable.nb12;
            }else if(weather.equals("눈/비")) {
                return R.drawable.nb13;
            }else{
                return R.drawable.nb01;
            }
        }
    }
}