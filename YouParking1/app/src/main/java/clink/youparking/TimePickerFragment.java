package clink.youparking;

import android.content.Context;
import android.os.Bundle;
import android.app.TimePickerDialog;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.app.DialogFragment;
import android.app.Dialog;
import java.util.Calendar;
import android.widget.TimePicker;
import android.widget.Toast;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    final Calendar c = Calendar.getInstance();
    private long phpTime = 0;
    final int twoHoursLater = 7200;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        TextView tv = (TextView) getActivity().findViewById(R.id.timeText);

        Calendar calendar = Calendar.getInstance();
        calendar.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                hourOfDay, minute, 0);
//        System.out.println(c.get(Calendar.YEAR) + " " + c.get(Calendar.MONTH) + " " + c.get(Calendar.DAY_OF_MONTH)
//                + " " + hourOfDay + " " + minute);

        long time = calendar.getTimeInMillis();
        int hour = hourOfDay % 12;
        tv.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                minute, hourOfDay < 12 ? "AM" : "PM"));

        long nowTime = System.currentTimeMillis()/1000;
        long nowTimePlusTwo = nowTime + twoHoursLater;

        phpTime = time/1000;
        User.time = phpTime;

//        System.out.println("Set Time: " + User.time);
//        System.out.println("Two Hours Later Time: " + nowTimePlusTwo);

        if (phpTime <= nowTimePlusTwo)
        {
            ((MainActivity)getActivity()).setValidTime(false);

            Context context = getActivity().getApplicationContext();
            CharSequence text = "Departure must be at least 2 hours from now.";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else {
            ((MainActivity)getActivity()).setValidTime(true);
        }
    }

    public long getPhpTime() {
        return phpTime;
    }
}
