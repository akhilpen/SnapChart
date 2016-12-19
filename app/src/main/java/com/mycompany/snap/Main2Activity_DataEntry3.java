package com.mycompany.snap;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;

public class Main2Activity_DataEntry3 extends AppCompatActivity implements View.OnClickListener{

    final int CAMERA_REQUEST = 13323;
    Button next,back,general;
    ImageView result;
    Intent intent;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    String coun,uid,path = "null";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_activity__dataentry3);
        back=(Button) findViewById(R.id.button_back);
        back.setOnClickListener(this);
        next=(Button) findViewById(R.id.button_next);
        next.setOnClickListener(this);
        general=(Button) findViewById(R.id.button_general);
        general.setOnClickListener(this);
        result = (ImageView) findViewById(R.id.imageView);
        TextView sno = (TextView) findViewById(R.id.textView_sno);
        intent = getIntent();
        coun= intent.getExtras().getString("count");
        uid = intent.getExtras().getString("ins_name").substring(0, 2) + "_" + intent.getExtras().getString("year") + "_" + intent.getExtras().getString("section") + "_" + coun;

        sno.setText("S.No.: "+Integer.toString(Integer.parseInt(intent.getExtras().getString("count"))));


    }

    public File getFile(){
        File folder = new File("sdcard/snap/" + intent.getExtras().getString("ins_name").substring(0, 2) + "_" + intent.getExtras().getString("year") + "_" + intent.getExtras().getString("section"));

        // File folder = new File(String.valueOf(Environment.getDataDirectory()));

        if (!folder.exists()){
            folder.mkdir();
        }
        File image_file = new File(folder,uid+".jpg");
        return image_file;
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        path = "sdcard/snap/" + intent.getExtras().getString("ins_name").substring(0, 2) + "_" + intent.getExtras().getString("year") + "_" + intent.getExtras().getString("section")+"/"+uid+".jpg";

        result.setImageDrawable(Drawable.createFromPath(path));
    }

    @Override
    public void onClick(View v) {

        if (v.equals(back)){
            onBackPressed();
            /*Intent i = new Intent(this,Main2Activity_DataEntry.class);
            startActivity(i);
            finish();*/
        }

        else if (v.equals(next)){

            if (result.getDrawable()==null){
                Toast.makeText(Main2Activity_DataEntry3.this,"Take a picture!",Toast.LENGTH_SHORT).show();
            }
            else {


                Intent i = new Intent(this, Main2Activity_DataEntry5.class);
                Intent intent = getIntent();

                Bundle b = intent.getExtras();

                b.putString("path", path);
                b.putString("uid", uid);
                int count = Integer.parseInt(coun);
                i.putExtras(b);

                if ((count) != Integer.parseInt(intent.getExtras().getString("strength"))) {

                    startActivity(i);
                    finish();
                } else {

                    Intent intent1 = new Intent(this, Main2Activity_DataEntry5.class);

                    intent = getIntent();


                    b = intent.getExtras();
                    b.putString("uid", uid);

                    b.putString("path", path);
                    intent1.putExtras(b);

                    startActivity(intent1);
                    //finish();
                }

            }

        }

        else if (v.equals(general)){

            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = getFile();
            camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(camera_intent,REQUEST_IMAGE_CAPTURE);
        }
    }
}
