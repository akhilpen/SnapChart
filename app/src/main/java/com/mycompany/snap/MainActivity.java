package com.mycompany.snap;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Context;


import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button button_data,button_sales,button_upload,button_show;
    DatabaseHelper myDb;

    private  final String TAG = this.getClass().getName();
    private ProgressDialog progressDialog;
    int progressBarStatus;
    ImageButton logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_data= (Button) findViewById(R.id.button_visiting);
        button_sales= (Button) findViewById(R.id.button_sales);
        button_show= (Button) findViewById(R.id.button_show);
        button_show.setOnClickListener(this);
        logout = (ImageButton) findViewById(R.id.imageButton);
        logout.setOnClickListener(this);
        button_data.setOnClickListener(this);
        button_sales.setOnClickListener(this);
        myDb = new DatabaseHelper(this);

        button_upload = (Button) findViewById(R.id.button_upload);
        button_upload.setOnClickListener(this);


    }
    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }







    @Override
    public void onClick(View v) {


        if (v.equals(logout)){

            LoginPage.spref.edit().clear().commit();
            Intent intent = new Intent(this,LoginPage.class);
            startActivity(intent);

        }

        if (v.equals(button_data)) {
            Intent intent = new Intent(this, Main2Activity_DataEntry.class);
            startActivity(intent);
            finish();
        }

        if (v.equals(button_sales)) {
            //Intent intent = new Intent(this, LoginPage.class);
            //startActivity(intent);
            if (isDeviceOnline()){

            Intent intent = new Intent(this, Main2Activity_SalesTracking2.class);
            startActivity(intent);
            finish();}
            else {
                Toast.makeText(this,"Requires network connection ",Toast.LENGTH_SHORT).show();
            }

        }


        if (v.equals(button_upload)) {


            HashMap<String, String> table = new HashMap<String, String>();

            Cursor res_sales = myDb.getAllData_sales();
            Cursor res_details = myDb.getAllData_details();
            if (res_sales.getCount() == 0 && res_details.getCount() == 0) {
                Toast.makeText(this, "Database Empty!", Toast.LENGTH_LONG).show();
            } else {
                int i = res_sales.getCount() + 1;
                int j = res_details.getCount() + 1;

                String[] sales_tables = new String[i];
                sales_tables[0] = "<u><b>" + "Sales Tables" + " </b></u>";
                String[] details_tables = new String[j];
                details_tables[0] = "<u><b>" + "Details Tables" + " </b></u>";

                i--;
                j--;

                while (res_sales.moveToNext()) {
              /* table.put("Table "+i+":",("\nnameofclient: "+ res_sales.getString(1))+("\ndate: "+ res_sales.getString(8))
                       +("\narea: "+ res_sales.getString(0)+"\n\n\n")
                       );*/
                    sales_tables[res_sales.getCount() - i + 1] = "<p>" + "Table " + Integer.toString(res_sales.getCount() - i + 1) + ":" + ("<br>" + "UID: " + res_sales.getString(12)) + ("<br>" + "nameofclient: " + res_sales.getString(1)) + ("<br>" + "date: " + res_sales.getString(8))
                            + ("<br>" + "area: " + res_sales.getString(0)) + "</p>";
                    i--;

                }

                while (res_details.moveToNext()) {
              /* table.put("Table "+i+":",("\nnameofclient: "+ res_sales.getString(1))+("\ndate: "+ res_sales.getString(8))
                       +("\narea: "+ res_sales.getString(0)+"\n\n\n")
                       );*/
                    details_tables[res_details.getCount() - j + 1] = "<p>" + "Table " + Integer.toString(res_details.getCount() - j + 1) + ":" + ("<br>" + "UID: " + res_details.getString(8)) + ("<br>" + "name of institute: " + res_details.getString(0)) + ("<br>" + "year: " + res_details.getString(1) + "<br>section: " + res_details.getString(2) + "<br>first name: " + res_details.getString(3)+"</p>");
                    j--;

                }

                String message = new String();
                if (res_details.getCount() > 0) {
                    message=message+(Arrays.toString(details_tables).substring(1, Arrays.toString(details_tables).length() - 1).replaceAll(", ", ""));

                }
                if (res_sales.getCount() > 0) {
                    message=message+(Arrays.toString(sales_tables).substring(1, Arrays.toString(sales_tables).length() - 1).replaceAll(", ", ""));

                }

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                // alertDialogBuilder.setTitle("TABLES");
                alertDialogBuilder.setMessage(Html.fromHtml(message));

                alertDialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        ///
                    }
                });

                alertDialogBuilder.setPositiveButton("submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {



                        final ProgressDialog progressbar = new ProgressDialog(MainActivity.this);
                        progressbar.setMessage("Uploading...");
                        progressbar.setCancelable(false);
                        progressbar.setCanceledOnTouchOutside(false);
                        progressbar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressbar.show();
                        Cursor res_sales = myDb.getAllData_sales();
                        Cursor res_details = myDb.getAllData_details();


                        if (isDeviceOnline()) {


                            if (res_details.getCount() == 0 && res_sales.getCount() == 0) {
                                Toast.makeText(MainActivity.this, "Database empty!", Toast.LENGTH_LONG).show();
                            } else {


                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Cursor res_sales = myDb.getAllData_sales();
                                        Cursor res_details = myDb.getAllData_details();


                                        while (res_sales.moveToNext()) {

                                            String path_sales = new String("snap/sales/" + res_sales.getString(1) + "/");
                                            File f_sales = new File(res_sales.getString(13));
                                            String content_type = getMimeType(f_sales.getPath());

                                            String file_path = f_sales.getAbsolutePath();
                                            OkHttpClient client = new OkHttpClient();
                                            RequestBody file_body = RequestBody.create(MediaType.parse(content_type), f_sales);

                                            RequestBody request_body = new MultipartBody.Builder()
                                                    .setType(MultipartBody.FORM)
                                                    .addFormDataPart("date", res_sales.getString(8))
                                                    .addFormDataPart("email", res_sales.getString(5))
                                                    .addFormDataPart("mobile", res_sales.getString(6))
                                                    .addFormDataPart("status", res_sales.getString(7))
                                                    .addFormDataPart("product", res_sales.getString(9))
                                                    .addFormDataPart("cost", res_sales.getString(10))
                                                    .addFormDataPart("remark", res_sales.getString(11))
                                                    .addFormDataPart("uid", res_sales.getString(12))
                                                    .addFormDataPart("actual_path", path_sales + res_sales.getString(12) + ".jpg")
                                                    .addFormDataPart("area", res_sales.getString(0))
                                                    .addFormDataPart("designation", res_sales.getString(4))
                                                    .addFormDataPart("address", res_sales.getString(2))
                                                    .addFormDataPart("nameofclient", res_sales.getString(1))
                                                    .addFormDataPart("contact", res_sales.getString(3))
                                                    .addFormDataPart("path", path_sales)
                                                    .addFormDataPart("type", content_type)
                                                    .addFormDataPart("employee",LoginPage.sname)
                                                    .addFormDataPart("uploaded_file", file_path.substring(file_path.lastIndexOf("/") + 1), file_body)
                                                    .build();

                                            Request request = new Request.Builder()
                                                    .url("http://snapchart.comli.com/submit_sales.php")
                                                    .post(request_body)
                                                    .build();

                                            try {
                                                Response response = client.newCall(request).execute();




                                                Log.d(TAG,response.networkResponse().toString());
                                               // Toast.makeText(MainActivity.this,"response.message().toString()",Toast.LENGTH_LONG).show();

                                                if (!response.isSuccessful()) {
                                                    throw new IOException("Error : " + response);
                                                }
                                                else {
                                                    if (res_sales.getString(7).contains("Closed")){
                                                        myDb.deleteRow_sales(res_sales.getString(12));
                                                         try {
                                                            FileUtils.deleteDirectory(new File("sdcard/snap/"+res_sales.getString(1).substring(0,2)+"/"));
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                }


                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                        while (res_details.moveToNext()) {

                                            String path = new String("snap/details/" + res_details.getString(0) + "/" + res_details.getString(1) + "/" + res_details.getString(2) + "/");
                                            File f = new File(res_details.getString(9));
                                            String content_type = getMimeType(f.getPath());

                                            String file_path = f.getAbsolutePath();
                                            OkHttpClient client = new OkHttpClient();
                                            RequestBody file_body = RequestBody.create(MediaType.parse(content_type), f);


                                            RequestBody request_body = new MultipartBody.Builder()
                                                    .setType(MultipartBody.FORM)
                                                    .addFormDataPart("uid", res_details.getString(8))
                                                    .addFormDataPart("ins_name", res_details.getString(0))
                                                    .addFormDataPart("section", res_details.getString(2))
                                                    .addFormDataPart("year", res_details.getString(1))
                                                    .addFormDataPart("fname", res_details.getString(3))
                                                    .addFormDataPart("lname", res_details.getString(4))
                                                    .addFormDataPart("email", res_details.getString(5))
                                                    .addFormDataPart("mobile", res_details.getString(6))
                                                    .addFormDataPart("remarks", res_details.getString(7))
                                                    .addFormDataPart("employee",LoginPage.sname)
                                                    .addFormDataPart("actual_path", path + res_details.getString(8) + ".jpg")
                                                    .addFormDataPart("path", path)
                                                    .addFormDataPart("type", content_type)
                                                    .addFormDataPart("uploaded_file", file_path.substring(file_path.lastIndexOf("/") + 1), file_body)
                                                    .build();

                                            Request request = new Request.Builder()
                                                    .url("http://snapchart.comli.com/submit_details.php")
                                                    .post(request_body)
                                                    .build();

                                            try {
                                                Response response = client.newCall(request).execute();

                                                if (!response.isSuccessful()) {
                                                    throw new IOException("Error : " + response);
                                                }else {
                                                     try {
                                            FileUtils.deleteDirectory(new File("sdcard/snap/"+ "_" +res_details.getString(0).substring(0,2)+"_"+res_details.getString(1) + "_" +res_details.getString(2)+"/"));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                                }


                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }



                                        }

                                        progressbar.dismiss();
                                       // myDb.deleteAllData_sales();
                                        myDb.deleteAllData_details();

                                        if ((myDb.getAllData_details().getCount()==0 )&& (myDb.getAllData_sales().getCount()==0)) {
                                            try {
                                                FileUtils.deleteDirectory(new File("sdcard/snap/"));
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }).start();



                            }
                        }

                        else{
                            Toast.makeText(MainActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        }




        if (v.equals(button_show)){
            Cursor res_details = myDb.getAllData_details();
            Cursor res_sales = myDb.getAllData_sales();

            Toast.makeText(this,"details : "+Integer.toString(res_details.getCount())+"\nsales   : "+Integer.toString(res_sales.getCount()), Toast.LENGTH_SHORT).show();


        }
    }

    private String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }


}
