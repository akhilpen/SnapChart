package com.mycompany.snap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;


public class Main2Activity_SalesTracking2 extends AppCompatActivity implements View.OnClickListener {
    Button next,back;
    String client_name,address;
    String area;
    DatabaseHelper myDb;
    String[] nameofclient;
    EditText editText_area,editText_address;
    InstantAutoComplete editText_client_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_activity__sales_tracking2);
        next=(Button) findViewById(R.id.button_next);
        back=(Button) findViewById(R.id.button_back);
        editText_client_name=(InstantAutoComplete) findViewById(R.id.editText_nameofclient);
        editText_area=(EditText) findViewById(R.id.editText_area);
        editText_address=(EditText) findViewById(R.id.editText_address);
        editText_area.setOnClickListener(this);
        next.setOnClickListener(this);
        back.setOnClickListener(this);
         myDb = new DatabaseHelper(this);

         nameofclient = myDb.getAllclients_sales();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,nameofclient);
        editText_client_name.setAdapter(adapter);
        editText_client_name.setOnClickListener(this);
        //if (Arrays.asList(nameofclient).contains())

       // editText_client_name.setThreshold(0);
        //editText_client_name.showDropDown();
        Area();

    }

    public void Area() {


        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.getIsGPSTrackingEnabled()) {
            //editText_area.setText("asdfadsfasdfasdfs");

            editText_area.setText(gpsTracker.getAddressLine(this));
            editText_address.setText(String.valueOf(gpsTracker.latitude) + "," + String.valueOf(gpsTracker.longitude));
        }

    }

    public void onClick(View v) {



        if (v.equals(next)){
            Bundle b = new Bundle();
            if (Arrays.asList(nameofclient).contains(editText_client_name.getText().toString())){
                Cursor res = myDb.getColumn_sales(editText_client_name.getText().toString());
                res.moveToFirst();
                editText_area.setText(res.getString(0));
                editText_address.setText(res.getString(2));
                b.putString("exists","yes");
                Toast.makeText(Main2Activity_SalesTracking2.this,res.getString(0),Toast.LENGTH_SHORT).show();
            }
            else {
                b.putString("exists","no");
            }


            Intent intent = new Intent(this, Main2Activity_SalesTracking3.class);
            client_name=editText_client_name.getText().toString();
            area=(editText_area.getText().toString());

            address=editText_address.getText().toString();
           /* Bundle b = new Bundle();
                b.putString("nameofclient",client_name);
                b.putString("strength",strength);
                b.putString("address",address);
                b.putString("count","1");
                b.putString("area",area);
                intent.putExtras(b);
            startActivity(intent);
            finish();*/


            if (client_name.isEmpty()||area.isEmpty()||address.isEmpty())
            {
                Toast.makeText(this, "Please enter all details!", Toast.LENGTH_LONG).show();

            }
            else {
                //Toast.makeText(this, client_name+" "+area+" "+address+" "+strength, Toast.LENGTH_LONG).show();

                b.putString("nameofclient",client_name);
                b.putString("address",address);
                b.putString("area",area);
                b.putString("uid",client_name+area.substring(0,3));
                intent.putExtras(b);
                startActivity(intent);
                //finish();
        }}

        if (v.equals(back)){
            onBackPressed();

        }
    }


}
