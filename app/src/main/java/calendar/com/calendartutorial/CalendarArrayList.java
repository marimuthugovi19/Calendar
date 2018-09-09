package calendar.com.calendartutorial;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by chinraam on 14/2/17.
 */

public class CalendarArrayList implements Parcelable
{
    public String getDaySelectStatus() {
        return daySelectStatus;
    }

    public void setDaySelectStatus(String daySelectStatus) {
        this.daySelectStatus = daySelectStatus;
    }

    String status="";
    String selectedValue="";

    String daySelectStatus = "";
    protected CalendarArrayList(Parcel in) {
        status = in.readString();
        selectedValue = in.readString();
        unSelectedValue = in.readString();
        daySelectStatus = in.readString();
    }

    public static final Creator<CalendarArrayList> CREATOR = new Creator<CalendarArrayList>() {
        @Override
        public CalendarArrayList createFromParcel(Parcel in) {
            return new CalendarArrayList(in);
        }

        @Override
        public CalendarArrayList[] newArray(int size) {
            return new CalendarArrayList[size];
        }
    };




    Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public CalendarArrayList(String selectedDateFormat, String unSelectedDateFormat, String s, Date date,String daySelectStatus) {

        this.selectedValue=selectedDateFormat;
        this.unSelectedValue=unSelectedDateFormat;
        this.status=s;
        this.date=date;
        this.daySelectStatus = daySelectStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }

    public String getUnSelectedValue() {
        return unSelectedValue;
    }

    public void setUnSelectedValue(String unSelectedValue) {
        this.unSelectedValue = unSelectedValue;
    }

    String unSelectedValue="";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(selectedValue);
        dest.writeString(unSelectedValue);
        dest.writeString(daySelectStatus);
    }
}
