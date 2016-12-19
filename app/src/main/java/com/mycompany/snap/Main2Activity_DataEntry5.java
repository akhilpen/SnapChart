package com.mycompany.snap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.HashMap;

public class Main2Activity_DataEntry5 extends AppCompatActivity implements View.OnClickListener{
Button next,back;

    EditText first,last,email,mobile,remarks;
    TextView sno;
    String fname,lname,semail,smobile,sremarks;
    ImageView result,result2;
    DatabaseHelper myDb;
    NetworkInfo nInfo;

    ConnectivityManager cManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_activity__data_entry5);
        next = (Button) findViewById(R.id.button_next);
        next.setOnClickListener(this);

        back = (Button) findViewById(R.id.button_back);
        back.setOnClickListener(this);
        sno = (TextView) findViewById(R.id.textView_sno);
        first=(EditText) findViewById(R.id.editText_firstname);
        last=(EditText) findViewById(R.id.editText_lastname);
        email=(EditText) findViewById(R.id.editText_email);
        mobile=(EditText) findViewById(R.id.editText_mobile);
        remarks=(EditText) findViewById(R.id.editText_remarks);
        result = (ImageView) findViewById(R.id.imageView2);
        //result2 = (ImageView) findViewById(R.id.imageView);
        //result.setImageDrawable(result2.getDrawable());
        //result.setImageMatrix(result2.getImageMatrix());
        Intent intent=getIntent();
        myDb = new DatabaseHelper(this);
       // result.setImageBitmap(intent.getExtras().getParcelable("photo"));
        sno.setText("S.No.: "+Integer.toString(Integer.parseInt(intent.getExtras().getString("count"))));


        Bitmap bitmap = (Bitmap)this.getIntent().getParcelableExtra("bmp");

        result.setImageBitmap(bitmap);


     cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);


    }
    public boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public void Submit(){


            fname=first.getText().toString();
            lname=last.getText().toString();
            semail=email.getText().toString();
            smobile=mobile.getText().toString();
            sremarks=remarks.getText().toString();
            Intent intent = getIntent();

            HashMap postData = new HashMap();
            postData.put("ins_name",intent.getExtras().getString("ins_name"));
            postData.put("section",intent.getExtras().getString("section"));
            postData.put("year",intent.getExtras().getString("year"));

            postData.put("fname",fname);
            postData.put("lname",lname);
            postData.put("email",semail);
            postData.put("mobile",smobile);
            postData.put("remarks",sremarks);



            Boolean isInserted = myDb.insertData_details(intent.getExtras().getString("ins_name"),(intent.getExtras().getString("year")),intent.getExtras().getString("section"),fname,lname,semail,(smobile),sremarks,intent.getExtras().getString("uid"),intent.getExtras().getString("path"));
            if (isInserted == true){
                Toast.makeText(this, "Entered into Database!", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(this, "Error! Try again", Toast.LENGTH_SHORT).show();

            }
    }

    @Override
    public void onClick(View v) {



        if (v.equals(back)){
           onBackPressed();
        }
        else if (v.equals(next)){
            Submit();
            Intent intent=getIntent();
            Intent i = new Intent(this,Main2Activity_DataEntry3.class);
            String coun= intent.getExtras().getString("count");
            int count = Integer.parseInt(coun);
            //count++;
            //coun = Integer.toString(count);


            fname=first.getText().toString();
            lname=last.getText().toString();
            semail=email.getText().toString();
            smobile=mobile.getText().toString();
            sremarks=remarks.getText().toString();
         Bundle b = new Bundle();
            //b.putString("last",lname);
            //b.putString("first",fname);
            //b.putString("email", semail);
            //b.putString("mobile",smobile);
            //b.putString("remarks", sremarks);
            b = intent.getExtras();
            b.putString("count", Integer.toString(count+1));
            //b.putString("strength",intent.getExtras().getString("strength"));
            i.putExtras(b);

            if ((count) != Integer.parseInt(intent.getExtras().getString("strength"))){
//if (Integer.parseInt(intent.getExtras().getString("strength"))!=0){
    b.putString("strength", Integer.toString(Integer.parseInt(intent.getExtras().getString("strength"))-1));

    startActivity(i);
            //finish();
            }
            else {

               Intent intent1 = new Intent(this,MainActivity.class);

                intent=getIntent();


                b = intent.getExtras();



                intent1.putExtras(b);

                startActivity(intent1);
                finish();
            }

        }

    }

}
