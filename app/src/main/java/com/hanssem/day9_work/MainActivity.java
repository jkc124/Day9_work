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
                xml.execute("http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=1165059000"); // 방배본동 날씨 주소
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
            ArrayList<String> get_temp = new ArrayList<String>(); //온도를 담을 배열
            ArrayList<String> get_wfKor = new ArrayList<String>(); // 날씨상태를 담을 배열
            ArrayList<String> get_hour = new ArrayList<String>(); // 시간을 담을 배열


            for(int i=0; i<5; i++) { // 최근 3시간 간격 5개의 날씨를 가져옴
                Node node = nodeList.item(i);//data 엘리먼트
                Element element = (Element) node;
                NodeList temp = element.getElementsByTagName("temp"); // 온도를 가져옴
                /*get_temp=temp.item(0).getChildNodes().item(0).getNodeValue();*/
                /*System.out.println("################온도 : " + get_temp );*/
                Node text1 = (Node)temp.item(0).getFirstChild();
                String strTemp = text1.getNodeValue();
                get_temp.add(strTemp);

                NodeList wfKor = element.getElementsByTagName("wfKor"); // 날씨정보
                /*get_wfKor = wfKor.item(0).getChildNodes().item(0).getNodeValue();*/
                /*System.out.println("################날씨 : "+get_wfKor);*/
                Node text2 = (Node)wfKor.item(0).getFirstChild();
                String strWfKor = text2.getNodeValue();
                get_wfKor.add(strWfKor);

                NodeList hour = element.getElementsByTagName("hour"); // 시간
                /*get_hour = hour.item(0).getChildNodes().item(0).getNodeValue();*/
                Node text3 = (Node)hour.item(0).getFirstChild();
                String strHour = text3.getNodeValue();
                get_hour.add(strHour);

            }
            System.out.println(get_temp.get(0)+get_temp.get(1)+get_temp.get(2)+get_temp.get(3)+get_temp.get(4));
            System.out.println(get_wfKor.get(0) + get_wfKor.get(1) + get_wfKor.get(2) + get_wfKor.get(3) + get_wfKor.get(4));
            System.out.println(get_hour.get(0)+get_hour.get(1)+get_hour.get(2)+get_hour.get(3)+get_hour.get(4));

            int icon = getProperResourceId(get_wfKor.get(0).toString().trim());
            System.out.println("#######icon : "+icon);

            ScheduleListItem wItem = new ScheduleListItem(get_hour.get(0), get_temp.get(0), get_wfKor.get(0), icon); //현재 날씨 저장

            if (outScheduleList == null) {
                outScheduleList = new ArrayList<ScheduleListItem>();
            }
            outScheduleList.add(wItem);

            System.out.println(outScheduleList.get(0));
            monthViewAdapter.putSchedule(curPosition, outScheduleList);

            scheduleAdapter.scheduleList = outScheduleList;
            scheduleAdapter.notifyDataSetChanged();
            /*super.onPostExecute(doc);*/
        }

        // 날씨 아이콘
        private int getProperResourceId(String weather){
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
            }else{
                return R.drawable.nb01;
            }
        }
    }
}
