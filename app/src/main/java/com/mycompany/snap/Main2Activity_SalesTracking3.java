package com.mycompany.snap;

import android.content.Intent;
import android.database.Cursor;
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

public class Main2Activity_SalesTracking3 extends AppCompatActivity implements View.OnClickListener{

    final int CAMERA_REQUEST = 13323;
    Button next,back,general;
    ImageView result;
    Intent intent;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    String uid,path = "null";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_activity__sales_tracking3);
        back=(Button) findViewById(R.id.button_back);
        back.setOnClickListener(this);
        next=(Button) findViewById(R.id.button_next);
        next.setOnClickListener(this);
        general=(Button) findViewById(R.id.button_general);
        general.setOnClickListener(this);
        result = (ImageView) findViewById(R.id.imageView);
        intent = getIntent();
        uid = intent.getExtras().getString("uid");

        if (getIntent().getExtras().getString("exists").contains("yes")){
          //  path = "sdcard/snap/"+intent.getExtras().getString("nameofclient").substring(0,2)+"/"+uid+".jpg";
            Cursor res = (new DatabaseHelper(this)).getColumn_sales(intent.getExtras().getString("nameofclient"));
            res.moveToFirst();
            path = res.getString(13);

            result.setImageDrawable(Drawable.createFromPath(path));

        }




    }

    public File getFile(){
        File folder = new File("sdcard/snap/"+intent.getExtras().getString("nameofclient").substring(0,2));

        // File folder = new File(String.valueOf(Environment.getDataDirectory()));

        if (!folder.exists()){
            folder.mkdir();
        }
        File image_file = new File(folder,uid+".jpg");
        return image_file;
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        path = "sdcard/snap/"+intent.getExtras().getString("nameofclient").substring(0,2)+"/"+uid+".jpg";

        result.setImageDrawable(Drawable.createFromPath(path));
    }

    @Override
    public void onClick(View v) {

         if (v.equals(next)){



          Intent i = new Intent(this, Main2Activity_SalesTracking5.class);
          Intent intent = getIntent();

          Bundle b = intent.getExtras();

          b.putString("path", path);
          b.putString("uid", uid);
          i.putExtras(b);


          startActivity(i);
          // finish();




        }

        else if (v.equals(back)){
            onBackPressed();
        }

        else if (v.equals(general)){

            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = getFile();
            camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(camera_intent,REQUEST_IMAGE_CAPTURE);
        }
    }
}
