package com.mycompany.snap;

/**
 * Created by akhilpendyala on 7/28/16.
 */
import java.util.Calendar;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

@SuppressLint("ValidFragment")
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    EditText txtdate;
    public DateDialog(View view){
        txtdate=(EditText)view;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {


// Use the current date as the default date in the dialog
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);


    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        //show to the selected date in the text box
       // String date=day+"-"+(month+1)+"-"+year;
        month = month+1;
        String mont = Integer.toString(month);
        String da = Integer.toString(day);
        if (month<10){
             mont = "0"+Integer.toString(month);
        }
        if (day<10){
            da = "0"+Integer.toString(day);
        }

        String date=year+"-"+mont+"-"+da;
        txtdate.setText(date);
    }



}
