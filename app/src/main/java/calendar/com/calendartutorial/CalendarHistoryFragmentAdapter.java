package calendar.com.calendartutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;


public class CalendarHistoryFragmentAdapter extends FragmentPagerAdapter
{
    ArrayList<CalendarArrayList> calendarHeadingArrayLists=new ArrayList<>();
    OnItemClickListener1 mListener;

    public CalendarHistoryFragmentAdapter(FragmentManager fm, ArrayList<CalendarArrayList> calendarHeadingArrayLists) {
        super(fm);
        this.calendarHeadingArrayLists=calendarHeadingArrayLists;
    }

    @Override
    public Fragment getItem(int position) {
        CalendarHistoryFragment attendanceHistoryFragment=new CalendarHistoryFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("position",position);
        bundle.putParcelableArrayList("array_list",calendarHeadingArrayLists);
        attendanceHistoryFragment.setArguments(bundle);

        attendanceHistoryFragment.SetOnItemClickListener(new CalendarHistoryFragment.OnItemClickListener1() {
            @Override
            public void onItemClick(View view, int position, Date date) {
                mListener.onItemClick(view,position,date);
            }
        });

        return attendanceHistoryFragment;
    }

    public interface OnItemClickListener1
    {
        void onItemClick(View view, int position, Date date);
    }

    public void SetOnItemClickListener(final OnItemClickListener1 mItemClickListener)
    {
        this.mListener = mItemClickListener;
    }

    @Override
    public int getCount() {
        return calendarHeadingArrayLists.size();
    }
}
