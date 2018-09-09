package calendar.com.calendartutorial;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CalendarHistoryFragment extends Fragment
{
    private static final int DAYS_COUNT = 42;
    static  int position;
    private Calendar currentDate = Calendar.getInstance();

    OnItemClickListener1 mListener;


    CalendarArrayList calendarArrayList;
    ArrayList<CalendarArrayList> calendarHeadingArrayLists = new ArrayList<>();

    RecyclerView gridCalendar;

    private static CalendarHistoryFragment mInstance;
    public static synchronized CalendarHistoryFragment getInstance() {
        return mInstance;
    }


    ArrayList<CalendarArrayList> cells;
    CalendarRecyclerAdapter calendarRecyclerAdapter;
    Calendar calendar1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.month_value_adapter, container, false);
        gridCalendar = (RecyclerView) view.findViewById(R.id.gridCalendar);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if(menuVisible)
        {
            mInstance=this;

            Bundle bundle=getArguments();
            position=bundle.getInt("position");
            calendarHeadingArrayLists=bundle.getParcelableArrayList("array_list");

            // set header color according to current season
            int month = currentDate.get(Calendar.MONTH);


            cells = new ArrayList<>();

            calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.HOUR_OF_DAY, 0);
            calendar1.set(Calendar.MINUTE, 0);
            calendar1.set(Calendar.SECOND, 0);
            calendar1.setTime(calendarHeadingArrayLists.get(position).getDate());

            Calendar calendar = (Calendar) calendar1.clone();

            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            // determine the cell for current month's beginning
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

            // move calendar backwards to the beginning of the week
            calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

            // fill cells
            while (cells.size() < DAYS_COUNT)
            {
                calendarArrayList = new CalendarArrayList("", "", "0", calendar.getTime(),"0");
                if(MainActivity.selectedDays.length>0){
                    if(MainActivity.selectedDays[0].equals(calendar.getTime())){
                        calendarArrayList.setDaySelectStatus("1");
                    }
                }

                cells.add(calendarArrayList);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    calendarRecyclerAdapter = new CalendarRecyclerAdapter(getActivity(), cells, calendar1.getTime());
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 7);
                    gridCalendar.setLayoutManager(gridLayoutManager);
                    gridCalendar.setAdapter(calendarRecyclerAdapter);

                    calendarRecyclerAdapter.SetOnItemClickListener(new CalendarRecyclerAdapter.OnItemClickListener1() {
                        @Override
                        public void onItemClick(View view, int position, Date date) {

                            if(mListener!=null) mListener.onItemClick(view,position,date);
                        }
                    });
                }
            },50);






        }
    }

    public interface OnItemClickListener1
    {
        void onItemClick(View view, int position, Date date);
    }

    public void SetOnItemClickListener(final OnItemClickListener1 mItemClickListener)
    {
        this.mListener = mItemClickListener;
    }

    public static  class CalendarRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    {

        Activity fragmentActivity;
        ArrayList<CalendarArrayList> calendarArrayLists=new ArrayList<>();
        OnItemClickListener1 mListener;


        Date currentMonth;
        // today
        Date today = new Date();

        public CalendarRecyclerAdapter(Activity mContext, ArrayList<CalendarArrayList> cells, Date currentMonth)
        {

            this.currentMonth=currentMonth;
            this.fragmentActivity=mContext;
            calendarArrayLists=cells;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.month_calendar_day_grid, parent, false);
            CalendarRecyclerAdapter.CalendarTextViewHolder vh = new CalendarRecyclerAdapter.CalendarTextViewHolder(v);

            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            final CalendarTextViewHolder calendarTextViewHolder=(CalendarTextViewHolder)holder;


            calendarTextViewHolder.view.setBackgroundResource(0);

            final Date date = calendarArrayLists.get(position).getDate();
            final int day = date.getDate();
            final int month = date.getMonth();
            int year = date.getYear();

            // clear styling
            calendarTextViewHolder.view.setTypeface(null, Typeface.NORMAL);


            final Calendar calendar= Calendar.getInstance();
            calendar.setTime(today);

            final Calendar calendar1= Calendar.getInstance();
            calendar1.setTime(currentMonth);

            calendarTextViewHolder.view.setId(position);

            if (day==calendar.get(Calendar.DAY_OF_MONTH) && month== calendar.get(Calendar.MONTH)
                    && calendar.get(Calendar.YEAR)==calendar1.get(Calendar.YEAR))
            {
                calendarTextViewHolder.view.setText(String.valueOf(date.getDate()));
                calendarTextViewHolder.view.setTextColor(Color.parseColor("#ffffff"));
                calendarArrayLists.get(position).setStatus("1");
                calendarTextViewHolder.view.setBackgroundResource(R.drawable.round_circle_calendar_white);
                calendarTextViewHolder.daysLayout.setVisibility(View.VISIBLE);
                calendarTextViewHolder.view.setVisibility(View.VISIBLE);
            }
            else if (month != currentMonth.getMonth() || year != currentMonth.getYear())
            {
                calendarTextViewHolder.view.setVisibility(View.GONE);
                calendarTextViewHolder.view.setText(String.valueOf(date.getDate()));
                calendarTextViewHolder.view.setTextColor(Color.parseColor("#50000000"));
                calendarTextViewHolder.daysLayout.setVisibility(View.GONE);
            }
            else
            {
                calendarTextViewHolder.daysLayout.setVisibility(View.VISIBLE);
                calendarTextViewHolder.view.setTextColor(Color.parseColor("#000000"));
                calendarTextViewHolder.view.setText(String.valueOf(date.getDate()));
                calendarTextViewHolder.view.setVisibility(View.VISIBLE);
            }

            switch (calendarArrayLists.get(position).getDaySelectStatus()){
                case "1":
                    calendarTextViewHolder.view.setBackgroundResource(R.drawable.rectangle_calendar_blue);
                    calendarTextViewHolder.view.setTextColor(Color.parseColor("#ffffff"));
                    break;

                    default:
                        if(!calendarArrayLists.get(position).getStatus().equalsIgnoreCase("1")){
                            calendarTextViewHolder.view.setBackgroundResource(0);
                            calendarTextViewHolder.view.setTextColor(Color.parseColor("#000000"));

                        }
                        break;
            }


          /*  calendarTextViewHolder.daysLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
        }*/}


        public interface OnItemClickListener1
        {
            void onItemClick(View view, int position, Date date);
        }

        public void SetOnItemClickListener(final OnItemClickListener1 mItemClickListener)
        {
            this.mListener = mItemClickListener;
        }


        @Override
        public int getItemCount() {
            return calendarArrayLists.size();
        }

        public class CalendarTextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        {
            TextView view;
            RelativeLayout daysLayout;

            public CalendarTextViewHolder(View itemView) {
                super(itemView);

                daysLayout=(RelativeLayout)itemView.findViewById(R.id.daysLayout);
                view=(TextView)itemView.findViewById(R.id.calendarGridValue);

                daysLayout.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

                int prevPosition = -1;
                for (int i=0;i<calendarArrayLists.size();i++){

                    if(calendarArrayLists.get(i).getDaySelectStatus().equalsIgnoreCase("1")){
                        prevPosition = i;
                        break;
                    }

                }
                if(prevPosition!=-1){
                    calendarArrayLists.get(prevPosition).setDaySelectStatus("0");
                    notifyItemChanged(prevPosition);
                }
                calendarArrayLists.get(getAdapterPosition()).setDaySelectStatus("1");
                notifyItemChanged(getAdapterPosition());

                mListener.onItemClick(view,getAdapterPosition(),calendarArrayLists.get(getAdapterPosition()).getDate());

            }
        }
    }
}
