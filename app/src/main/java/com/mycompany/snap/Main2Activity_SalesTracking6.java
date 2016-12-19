package com.mycompany.snap;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;

import com.google.api.services.calendar.CalendarScopes;
import com.google.api.client.util.DateTime;

import com.google.api.services.calendar.model.*;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.api.services.calendar.model.Event;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;



public class Main2Activity_SalesTracking6 extends AppCompatActivity implements View.OnClickListener,EasyPermissions.PermissionCallbacks {
    Button next,back;

    EditText date,product,cost,remark;
    String sdate ,sproduct,scost,sremark,saddress,snameofclient;

DatabaseHelper myDb;

    CheckBox cb;


    GoogleAccountCredential mCredential;

    ProgressDialog mProgress;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Google Calendar API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { CalendarScopes.CALENDAR };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_activity__sales_tracking6);
        next = (Button) findViewById(R.id.button_next);
        next.setOnClickListener(this);
        back = (Button) findViewById(R.id.button_back);
        back.setOnClickListener(this);
        date=((EditText) findViewById(R.id.editText_date));
        date.setInputType(InputType.TYPE_NULL);
        product=(EditText) findViewById(R.id.editText_product);
        cost=(EditText) findViewById(R.id.editText_email);
        remark=(EditText) findViewById(R.id.editText_mobile);
        date.setOnClickListener(this);
       product.setOnClickListener(this);
        myDb = new DatabaseHelper(this);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Google Calendar API ...");


        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());


        if (getIntent().getExtras().getString("exists").contains("yes")) {

            Cursor res = (new DatabaseHelper(this)).getColumn_sales(getIntent().getExtras().getString("nameofclient"));
            res.moveToFirst();
          product.setText(res.getString(9));
            cost.setText(res.getString(10));
            remark.setText(res.getString(11));


        }


    }
    private void showPopUp3() {
        Dialog dialog;


        final String[] items = {" product1", " product2", " product3"};
        final ArrayList itemsSelected = new ArrayList();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select products : ");

        builder.setMultiChoiceItems(items, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedItemId,
                                        boolean isSelected) {
                        if (isSelected) {
                            itemsSelected.add(items[selectedItemId]);
                        } else if (itemsSelected.contains(selectedItemId)) {
                            itemsSelected.remove(items[Integer.valueOf(selectedItemId)]);
                        }
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Your logic when OK button is clicked
                        product.setText(itemsSelected.toString().substring(2,itemsSelected.toString().length()-1));
                    }
                });

        dialog = builder.create();
        dialog.show();

    }


    public void Submit(){

            sremark=remark.getText().toString();
            scost=cost.getText().toString();
            sproduct=product.getText().toString();
            sdate=date.getText().toString();

            Intent intent = getIntent();
           saddress = intent.getExtras().getString("address");
          snameofclient = (intent.getExtras().getString("nameofclient"));

        if (intent.getExtras().getString("exists").contains("yes")){
            Boolean isInserted = myDb.updateData_sales(intent.getExtras().getString("area"),(intent.getExtras().getString("nameofclient")),intent.getExtras().getString("address"),intent.getExtras().getString("contact"),(intent.getExtras().getString("designation")),intent.getExtras().getString("email"),intent.getExtras().getString("mobile"),(intent.getExtras().getString("status")),sdate,sproduct,scost,sremark,intent.getExtras().getString("uid"),intent.getExtras().getString("path"));

            if (isInserted == true){
                Toast.makeText(this, "Updated Database!", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(this, "Error! Try again", Toast.LENGTH_SHORT).show();

            }
        }
        else {

            Boolean isInserted = myDb.insertData_sales(intent.getExtras().getString("area"), (intent.getExtras().getString("nameofclient")), intent.getExtras().getString("address"), intent.getExtras().getString("contact"), (intent.getExtras().getString("designation")), intent.getExtras().getString("email"), intent.getExtras().getString("mobile"), (intent.getExtras().getString("status")), sdate, sproduct, scost, sremark, intent.getExtras().getString("uid"), intent.getExtras().getString("path"));
            if (isInserted == true){
                Toast.makeText(this, "Entered into Database!", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(this, "Error! Try again", Toast.LENGTH_SHORT).show();

            }
        }

    }



    @Override
    public void onClick(View v) {




         if (v.equals(back)){
            onBackPressed();
           // getResultsFromApi();
        }
         else  if (v.equals(product)){
             showPopUp3();
         }
        else if (v.equals(date)){
            DateDialog dialog=new DateDialog(v);
            FragmentTransaction ft =getFragmentManager().beginTransaction();
            dialog.show(ft, "DatePicker");

        }

        else if (v.equals(next) ){

            sdate = date.getText().toString();
            if (sdate.isEmpty()){
                Toast.makeText(Main2Activity_SalesTracking6.this,"Select date and try again.",Toast.LENGTH_SHORT).show();
            }
            else {
            Submit();

            getResultsFromApi();

               // finish();

            }}

        }


    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {
          Toast.makeText(Main2Activity_SalesTracking6.this,("No network connection available."),Toast.LENGTH_SHORT).show();
        } else {
            new MakeRequestTask(mCredential).execute();
        }
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                  Toast.makeText(Main2Activity_SalesTracking6.this,(
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app."),Toast.LENGTH_SHORT).show();
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
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

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                Main2Activity_SalesTracking6.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }





    /**
     * An asynchronous task that handles the Google Calendar API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;

        public MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API Android Quickstart")
                    .build();
        }

        /**
         * Background task to call Google Calendar API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of the next 10 events from the primary calendar.
         * @return List of Strings describing returned events.
         * @throws IOException
         */
        private List<String> getDataFromApi() throws IOException {
            // List the next 10 events from the primary calendar.
          /*  DateTime now = new DateTime(System.currentTimeMillis());
            List<String> eventStrings = new ArrayList<String>();
            Events events = mService.events().list("primary")
                    .setMaxResults(10)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            List<Event> items = events.getItems();

            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    // All-day events don't have start times, so just use
                    // the start date.
                    start = event.getStart().getDate();
                }
                eventStrings.add(
                        String.format("%s (%s)", event.getSummary(), start));
            }
            return eventStrings;*/

            // Refer to the Java quickstart on how to setup the environment:
// https://developers.google.com/google-apps/calendar/quickstart/java
// Change the scope to CalendarScopes.CALENDAR and delete any stored
// credentials.


            ArrayList arrayList = new ArrayList();

            if (sdate.isEmpty()){
                arrayList.add(0,"empty");
                return arrayList;
            }
            else {


            Event event = new Event()
                    .setSummary(snameofclient)
                    .setLocation(saddress)
                    .setDescription(sremark);

            DateTime startDateTime = new DateTime(sdate+"T09:00:00+05:30");
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime);

            event.setStart(start);

            DateTime endDateTime = new DateTime(sdate+"T10:00:00+05:30");
            EventDateTime end = new EventDateTime()
                    .setDateTime(endDateTime);

            event.setEnd(end);


            EventReminder[] reminderOverrides = new EventReminder[] {
                    new EventReminder().setMethod("email").setMinutes(120),
                    new EventReminder().setMethod("popup").setMinutes(120),
            };
            Event.Reminders reminders = new Event.Reminders()
                    .setUseDefault(false)
                    .setOverrides(Arrays.asList(reminderOverrides));
            event.setReminders(reminders);

            String calendarId = "primary";


                event = mService.events().insert(calendarId, event).execute();
            }


            arrayList.add(0,"Date added to google calender.");
            return arrayList;

        }


        @Override
        protected void onPreExecute() {
            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            mProgress.hide();
            if (output == null || output.size() == 0) {
                Toast.makeText(Main2Activity_SalesTracking6.this,"No results returned.",Toast.LENGTH_SHORT).show();
            }
            else if (output.get(0).toString().equals("empty")){
                Toast.makeText(Main2Activity_SalesTracking6.this,"Select date and try again.",Toast.LENGTH_SHORT).show();

            }
            else if (output.get(0).toString().equals("Date added to google calender.")){
                Toast.makeText(Main2Activity_SalesTracking6.this,output.get(0).toString(),Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(Main2Activity_SalesTracking6.this,MainActivity.class);


                startActivity(intent1);

            }
            else {
                output.add(0, "Data retrieved using the Google Calendar API:");
                Toast.makeText(Main2Activity_SalesTracking6.this, TextUtils.join("\n", output),Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            Main2Activity_SalesTracking6.REQUEST_AUTHORIZATION);
                } else {
                  Toast.makeText(Main2Activity_SalesTracking6.this,("The following error occurred:\n"
                            + mLastError.getMessage()),Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(Main2Activity_SalesTracking6.this,"Request cancelled",Toast.LENGTH_SHORT).show();
            }
        }
    }

}




