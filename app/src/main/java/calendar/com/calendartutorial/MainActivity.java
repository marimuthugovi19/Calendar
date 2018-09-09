package calendar.com.calendartutorial;

import android.app.Activity;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{
    MonthNameGalleryAdapter monthNameGalleryAdapter;

    LinearLayout calendarLayout;
    Gallery monthNameGallery;
    CustomViewPager monthValueViewPager;

    CalendarArrayList calendarHeadingArrayList;
    ArrayList<CalendarArrayList> calendarHeadingArrayLists = new ArrayList();

    TextView sunTextView,
    monTextView,
    tueTextView,
    wedTextView,
    thuTextView,
    friTextView,
    satTextView;

    private Calendar currentDate = Calendar.getInstance();
    Date minDate=null,maxDate=null;
    int diffMonth=0,currentYearPosition=0;


    static Date[] selectedDays = new Date[0];
    static String[] availableDays;

    private static final int DAYS_COUNT = 42;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedDays = new Date[0];

        calendarLayout = (LinearLayout) findViewById(R.id.calendarLayout);

        monthNameGallery = (Gallery) calendarLayout.findViewById(R.id.monthNameViewPager);
        monthValueViewPager = (CustomViewPager) calendarLayout.findViewById(R.id.monthValueViewPager);

        sunTextView = (TextView) calendarLayout.findViewById(R.id.sunTextView);
        monTextView = (TextView) calendarLayout.findViewById(R.id.monTextView);
        tueTextView = (TextView) calendarLayout.findViewById(R.id.tueTextView);
        wedTextView = (TextView) calendarLayout.findViewById(R.id.wedTextView);
        thuTextView = (TextView) calendarLayout.findViewById(R.id.thuTextView);
        friTextView = (TextView) calendarLayout.findViewById(R.id.friTextView);
        satTextView = (TextView) calendarLayout.findViewById(R.id.satTextView);


        minDate = Calendar.getInstance().getTime();

        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR,30);
        maxDate = c.getTime();

        Calendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(minDate);

        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(maxDate);


        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        diffMonth = diffYear * 12 + (endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH)) + 1;


        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.DAY_OF_MONTH, 1);
        currentDate.add(Calendar.MONTH, 1);

        for (int i = 0; i < diffMonth; i++) {

            String selectedDateFormat = "", unSelectedDateFormat = "";
            startCalendar = new GregorianCalendar();
            startCalendar.setTime(minDate);
            startCalendar.add(Calendar.MONTH, i);

            SimpleDateFormat sdf1 = new SimpleDateFormat("MMM yy", Locale.getDefault());
            SimpleDateFormat sdf2 = new SimpleDateFormat("MMM", Locale.getDefault());

            selectedDateFormat = sdf1.format(startCalendar.getTimeInMillis());
            unSelectedDateFormat = sdf2.format(startCalendar.getTimeInMillis());
            String currentDateFormat = sdf1.format(Calendar.getInstance().getTimeInMillis());


            if (selectedDateFormat.equalsIgnoreCase(currentDateFormat)) {
                currentYearPosition = i;
            }

            calendarHeadingArrayList = new CalendarArrayList(selectedDateFormat, unSelectedDateFormat, "0", startCalendar.getTime(),"0");
            calendarHeadingArrayLists.add(calendarHeadingArrayList);
        }


        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        SimpleDateFormat sdf = new SimpleDateFormat("EE", Locale.getDefault());

        for (int i = 0; i < 7; i++) {

            switch (i) {
                case 0:
                    sunTextView.setText(sdf.format(cal.getTime()));
                    break;

                case 1:
                    monTextView.setText(sdf.format(cal.getTime()));
                    break;

                case 2:
                    tueTextView.setText(sdf.format(cal.getTime()));
                    break;

                case 3:
                    wedTextView.setText(sdf.format(cal.getTime()));
                    break;

                case 4:
                    thuTextView.setText(sdf.format(cal.getTime()));
                    break;

                case 5:
                    friTextView.setText(sdf.format(cal.getTime()));
                    break;

                case 6:
                    satTextView.setText(sdf.format(cal.getTime()));
                    break;
            }

            cal.add(Calendar.DAY_OF_WEEK, 1);
        }


        monthNameGalleryAdapter = new MonthNameGalleryAdapter(MainActivity.this, calendarHeadingArrayLists);
        monthNameGallery.setAdapter(monthNameGalleryAdapter);
        monthNameGallery.setSelection(0);


        monthNameGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {

                monthValueViewPager.setCurrentItem(i);

                for (int k = 0; k < calendarHeadingArrayLists.size(); k++) {
                    if (k == i) {
                        final int m = k;
                        calendarHeadingArrayLists.get(k).setStatus("1");
                    } else {
                        calendarHeadingArrayLists.get(k).setStatus("0");
                    }

                    monthNameGalleryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        monthValueViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int mScrollState = ViewPager.SCROLL_STATE_IDLE;

            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
                if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                    return;
                }
                //monthViewPager.scrollTo(monthValueViewPager.getScrollX(), monthValueViewPager.getScrollY());
            }

            @Override
            public void onPageSelected(final int position) {

                monthNameGallery.setSelection(monthValueViewPager.getCurrentItem());
            }

            @Override
            public void onPageScrollStateChanged(final int state) {
                mScrollState = state;
                if (state == ViewPager.SCROLL_STATE_IDLE) {

                }
            }
        });


        CalendarHistoryFragmentAdapter daysValueViewPager=new CalendarHistoryFragmentAdapter(getSupportFragmentManager(),calendarHeadingArrayLists);
        monthValueViewPager.setAdapter(daysValueViewPager);

        daysValueViewPager.SetOnItemClickListener(new CalendarHistoryFragmentAdapter.OnItemClickListener1() {
            @Override
            public void onItemClick(View view, int position, Date date) {

                selectedDays = new Date[]{date};
                Log.e("Date Values","--->"+date);

            }
        });



    }


}
