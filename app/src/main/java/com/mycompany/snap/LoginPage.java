package com.mycompany.snap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class LoginPage extends AppCompatActivity implements View.OnClickListener {
    EditText et_username,et_password;
    Button signin;

   public static SharedPreferences spref;
    public static String sname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        et_username = (EditText) findViewById(R.id.etUsername);
        et_password = (EditText) findViewById(R.id.etPassword);
        signin = (Button) findViewById(R.id.bSignIn);
        signin.setOnClickListener(this);
        spref = getPreferences(MODE_PRIVATE);
        //spref.edit().clear().commit();
        if (spref.contains("username")&& spref.contains("password")){


            post(spref.getString("username",""),spref.getString("password",""));

        }
   }



    @Override
    public void onClick(View view) {

        post(et_username.getText().toString(),et_password.getText().toString());


    }

    private void post(final String username, final String password){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://snapchart.comli.com/login.php?username="+username+"&password="+password;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                        if (response.contains("failed")){
                            Toast.makeText(LoginPage.this,"wrong username or password",Toast.LENGTH_LONG).show();

                        }
                        else {
                            sname = response;
                            saveData(username,password);
                            Intent intent = new Intent(LoginPage.this,MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mTextView.setText("That didn't work!");
                Toast.makeText(LoginPage.this,"failed",Toast.LENGTH_LONG).show();

            }

        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void saveData(String username,String password) {
        spref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = spref.edit();
        ed.putString("username",username);
        ed.putString("password",password);
        ed.commit();


    }
}
