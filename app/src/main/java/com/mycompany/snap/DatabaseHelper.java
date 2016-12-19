package com.mycompany.snap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by akhilpendyala on 7/10/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    //public static final String DATABASE_NAME = "snap.db";
    private static final String DATABASE_NAME = "snap.db";
    public static final String TABLE_NAME1 = "details";
    public static final String TABLE_NAME2 = "sales";
    public static final String col_1 = "name_insti";
    public static final String col_2 = "year";
    public static final String col_3 = "section";
    public static final String col_4 = "first_name";
    public static final String col_5 = "last_name";
    public static final String col_6 = "email";
    public static final String col_7 = "mobile";
    public static final String col_8 = "remarks";
    public static final String col_9 = "uid";
    public static final String col_10 = "path";


    public static final String col_11 = "area";
    public static final String col_12 = "nameofclient";
    public static final String col_13 = "address";
    public static final String col_14 = "contact";
    public static final String col_15 = "designation";
    public static final String col_16 = "email";
    public static final String col_17 = "mobile";
    public static final String col_18 = "status";
    public static final String col_19 = "date";
    public static final String col_20 = "product";
    public static final String col_21 = "cost";
    public static final String col_22 = "remark";
    public static final String col_23 = "uid";
    public static final String col_24 = "path";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
      //  SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_details);
        db.execSQL(CREATE_TABLE_sales);
    }

    private static final String CREATE_TABLE_details =   "create table " + TABLE_NAME1 + "(name_insti STRING DEFAULT NULL,year STRING DEFAULT NULL,section STRING DEFAULT NULL, first_name STRING DEFAULT NULL, last_name STRING DEFAULT NULL, email STRING DEFAULT NULL, mobile STRING DEFAULT NULL, remarks STRING DEFAULT NULL,uid STRING,path STRING DEFAULT NULL)";


    // Tag table create statement
    private static final String CREATE_TABLE_sales = ("create table " + TABLE_NAME2 + "(area STRING DEFAULT NULL,nameofclient STRING DEFAULT NULL,address STRING DEFAULT NULL, contact STRING DEFAULT NULL, designation STRING DEFAULT NULL, email STRING DEFAULT NULL, mobile STRING DEFAULT NULL, status STRING DEFAULT NULL, date STRING DEFAULT NULL, product STRING DEFAULT NULL, cost STRING DEFAULT NULL, remark STRING DEFAULT NULL,uid STRING,path STRING DEFAULT NULL)");


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME2);

        onCreate(db);
    }

    public boolean insertData_details(String name_insti, String year, String section, String first_name, String last_name, String email, String mobile, String remarks, String uid,String path) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_1, name_insti);
        contentValues.put(col_2, year);
        contentValues.put(col_3, section);
        contentValues.put(col_4, first_name);
        contentValues.put(col_5, last_name);
        contentValues.put(col_6, email);
        contentValues.put(col_7, mobile);
        contentValues.put(col_8, remarks);
        contentValues.put(col_9, uid);
        contentValues.put(col_10, path);

        //long result = db.replace(TABLE_NAME1, null, contentValues);

        long result = db.insert(TABLE_NAME1, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }


    }

    public boolean insertData_sales(String area, String nameofclient, String address, String contact, String designation, String email, String mobile, String status, String date,String product,String cost, String remark, String uid,String path) {
        SQLiteDatabase dbt = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_11, area);
        contentValues.put(col_12, nameofclient);
        contentValues.put(col_13, address);
        contentValues.put(col_14, contact);
        contentValues.put(col_15, designation);
        contentValues.put(col_16, email);
        contentValues.put(col_17, mobile);
        contentValues.put(col_18, status);
        contentValues.put(col_19, date);
        contentValues.put(col_20, product);
        contentValues.put(col_21, cost);
        contentValues.put(col_22, remark);
        contentValues.put(col_23, uid);
        contentValues.put(col_24, path);

        //dbt.update(TABLE_NAME2,contentValues, "uid="+"'"+uid+"'",null);


        long result = dbt.insertWithOnConflict(TABLE_NAME2,null,contentValues,dbt.CONFLICT_IGNORE);



        if (result == -1) {
            return false;
        } else {
            return true;
        }


    }

    public boolean updateData_sales(String area, String nameofclient, String address, String contact, String designation, String email, String mobile, String status, String date,String product,String cost, String remark, String uid,String path) {
        SQLiteDatabase dbt = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col_11, area);
        contentValues.put(col_12, nameofclient);
        contentValues.put(col_13, address);
        contentValues.put(col_14, contact);
        contentValues.put(col_15, designation);
        contentValues.put(col_16, email);
        contentValues.put(col_17, mobile);
        contentValues.put(col_18, status);
        contentValues.put(col_19, date);
        contentValues.put(col_20, product);
        contentValues.put(col_21, cost);
        contentValues.put(col_22, remark);
        contentValues.put(col_23, uid);
        contentValues.put(col_24, path);

        long result=dbt.update(TABLE_NAME2,contentValues, "uid="+"'"+uid+"'",null);


       // long result = dbt.insertWithOnConflict(TABLE_NAME2,null,contentValues,dbt.CONFLICT_IGNORE);



        if (result == -1) {
            return false;
        } else {
            return true;
        }


    }

    public Cursor getAllData_details(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * from "+ TABLE_NAME1,null);
        return res;
    }

    public Cursor getAllData_sales(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * from "+ TABLE_NAME2,null);
        return res;
    }

    public String[] getAllclients_sales(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT nameofClient from "+ TABLE_NAME2,null);
        String[] temp = new String[res.getCount()];
        int i =0;
        while (res.moveToNext())
        {
            temp[i]=res.getString(0);
            i++;

        }
            return temp;
    }

    public Cursor getColumn_sales(String nameofclient){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * from "+ TABLE_NAME2+" where nameofclient = '"+nameofclient+"'",null);

        return res;
    }

    public void deleteAllData_details(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME1,null,null);

    }

    public void deleteAllData_sales(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME2,null,null);

    }

    public void deleteRow_sales(String UID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME2,"uid="+"'"+UID+"'",null);

    }


}