package calendar.com.calendartutorial;

import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;




public class MonthNameGalleryAdapter extends BaseAdapter {

    Activity mContext;
    LayoutInflater mLayoutInflater;

    ArrayList<CalendarArrayList> totalLength=new ArrayList<>();

    private Calendar currentDate = Calendar.getInstance();

    Float originalTextSize;
    private static final String DATE_FORMAT = "MMM yy";

    public MonthNameGalleryAdapter(Activity context, ArrayList<CalendarArrayList> totalLength) {
        mContext = context;
        this.totalLength=totalLength;
        if (mContext != null)
        {
            mLayoutInflater = LayoutInflater.from(mContext);
        }
    }

    @Override
    public int getCount() {
        return totalLength.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = mLayoutInflater.inflate(R.layout.month_adapter, viewGroup, false);

        TextView textView=(TextView)itemView.findViewById(R.id.textView);
        


        if(totalLength.get(i).getStatus().equalsIgnoreCase("1"))
        {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mContext.getResources().getDimension(R.dimen._14sdp));
            textView.setTextColor(Color.parseColor("#000000"));
            textView.setText(totalLength.get(i).getSelectedValue().replace(" "," \'"));
        }
        else
        {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mContext.getResources().getDimension(R.dimen._10sdp));
            textView.setTextColor(Color.parseColor("#80000000"));
            textView.setText(totalLength.get(i).getUnSelectedValue());
        }


        return itemView;
    }

}
