package com.mycompany.snap;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;



public class Main2Activity_SalesTracking5 extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    Button next,back;

    EditText contact,designation,email,mobile,status;

    String scontact,sdesignation,semail,smobile,sstatus;
    ImageView result,result2;
   // DatabaseHelper myDb;
    NetworkInfo nInfo;

    ConnectivityManager cManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_activity__sales_tracking5);
        next = (Button) findViewById(R.id.button_next);
        next.setOnClickListener(this);

        back = (Button) findViewById(R.id.button_back);
        back.setOnClickListener(this);
        contact=(EditText) findViewById(R.id.editText_date);
        designation=(EditText) findViewById(R.id.editText_product);
        email=(EditText) findViewById(R.id.editText_email);
        mobile=(EditText) findViewById(R.id.editText_mobile);
        status=(EditText) findViewById(R.id.editText_status);
        status.setInputType(InputType.TYPE_NULL);


        //status.setOnTouchListener(this);
        status.setOnClickListener(this);

        Intent intent = getIntent();

        if (intent.getExtras().getString("exists").contains("yes")) {

            Cursor res = (new DatabaseHelper(this)).getColumn_sales(intent.getExtras().getString("nameofclient"));
            res.moveToFirst();
            contact.setText(res.getString(3));
            designation.setText(res.getString(4));
            email.setText(res.getString(5));
            mobile.setText(res.getString(6));
            status.setText(res.getString(7));


        }


    }

    private void showPopUp3() {
        final Dialog dialog ;


        final String[] items = {" Hot", " Warm", " cold"," Closed"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select products : ");

        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        status.setText(items[i].substring(1));
                     dialogInterface.dismiss();

                    }


                });


        dialog = builder.create();
        dialog.show();

    }



    @Override
    public void onClick(View v) {


        if (v.equals(back)){
            onBackPressed();
            /*Intent i = new Intent(this,Main2Activity_SalesTracking3.class);
            startActivity(i);
            finish();*/
        }
        else if (v.equals(status)){
            showPopUp3();
        }

        else if (v.equals(next)){
            Intent intent=getIntent();
            Intent i = new Intent(this,Main2Activity_SalesTracking6.class);


            sdesignation=designation.getText().toString();
            scontact=contact.getText().toString();
            semail=email.getText().toString();
            smobile=mobile.getText().toString();
            sstatus = status.getText().toString();


            Bundle b = new Bundle();
            b = intent.getExtras();
            b.putString("designation",sdesignation);
            b.putString("contact",scontact);
            b.putString("email", semail);
            b.putString("mobile",smobile);
            b.putString("status", sstatus);

            i.putExtras(b);

      if (sstatus=="Select"){
          Toast.makeText(Main2Activity_SalesTracking5.this,"Select status",Toast.LENGTH_LONG).show();
      }
            else {

          startActivity(i);
          // finish();
      }
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.equals(status)){
         showPopUp3();
        }
        return false;
    }


}
