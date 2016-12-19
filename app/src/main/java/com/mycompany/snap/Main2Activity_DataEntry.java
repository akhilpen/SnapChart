package com.mycompany.snap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Main2Activity_DataEntry extends AppCompatActivity implements View.OnClickListener
{
    Button next,back;
    String ins_name,section;
    String year, strength;
    EditText editText_ins_name,editText_year,editText_section,editText_strength;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_activity__data_entry);
        next=(Button) findViewById(R.id.button_next);
        back=(Button) findViewById(R.id.button_back);
        editText_ins_name=(EditText) findViewById(R.id.editText_date);
        editText_year=(EditText) findViewById(R.id.editText_product);
        editText_section=(EditText) findViewById(R.id.editText_email);
        editText_strength=(EditText) findViewById(R.id.editText_strength);
        next.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    public void onClick(View v) {
       /* ins_name=editText_ins_name.getText().toString();
        year=Integer.getInteger(editText_year.getText().toString());
        section=editText_section.getText().toString();
        Bundle b = new Bundle();
        b.putString("ins_name",ins_name);
        b.putString("section",section);
        b.putInt("year",year);*/
        if (v.equals(next)){

            Intent intent = new Intent(this, Main2Activity_DataEntry3.class);
            ins_name=editText_ins_name.getText().toString();
            year=(editText_year.getText().toString());
            strength=(editText_strength.getText().toString());

            section=editText_section.getText().toString();


            if (strength.isEmpty()||ins_name.isEmpty()||year.isEmpty()||section.isEmpty())
            {
                Toast.makeText(this, "Please enter all details!", Toast.LENGTH_LONG).show();

            }
            else {
            Bundle b = new Bundle();
            b.putString("ins_name",ins_name);
            b.putString("strength",strength);
            b.putString("section",section);
            b.putString("count","1");
            b.putString("year",year);
            intent.putExtras(b);
            startActivity(intent);
            //finish();
            }
        }

        if (v.equals(back)){
            onBackPressed();
           /* Intent intent= new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();*/
        }
    }
}
