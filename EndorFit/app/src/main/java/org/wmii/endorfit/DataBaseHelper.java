package org.wmii.endorfit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Blob;


public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Exercises.db";
    public static final String TABLE_NAME = "exerciselist_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "CATEGORY";
    public static final String COL_4 = "DIFFICULTY_LEVEL";
    public static final String COL_5 = "DESCRIPTION";
    public static final String COL_6 = "IMAGE";
    public static final String COL_7 = "INTERNAL_TYPE";
    private int size = 0;
    private static final String TAG = "DataBaseHelper";
    public static final int DATABASE_VERSION = 2;

    public long getSize(Context context) {
        //SQLiteDatabase db = this.getWritableDatabase();
        File f = context.getDatabasePath("Exercises.db");
        long dbSize = f.length();
        Log.d(TAG, "dbSize: " + dbSize);
        return dbSize;
    }

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null,1);
    }

    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

//    public DataBaseHelper(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
//        super(context, name, version, openParams);
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME
                + "("+ COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_2 + " TEXT UNIQUE," +
                COL_3 + " TEXT," +
                COL_4 + " TEXT," +
                COL_5 + " TEXT," +
                COL_6 + " BLOB,"  +
                COL_7 + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
//    public void insertImg(int id , Bitmap img ) {
//
//
//        byte[] data = getBitmapAsByteArray(img); // this is a function
//
//        insertStatement_logo.bindLong(1, id);
//        insertStatement_logo.bindBlob(2, data);
//
//        insertStatement_logo.executeInsert();
//        insertStatement_logo.clearBindings() ;
//
//    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, outputStream);
        return outputStream.toByteArray();
    }
    public boolean insertData(String name, String category, String difficulty, String description, byte[] image, String internalType)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, category);
        contentValues.put(COL_4, difficulty);
        contentValues.put(COL_5, description);
        contentValues.put(COL_6,image);
        contentValues.put(COL_7,internalType);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
        {
            return false;
        }
        else
        {
            ++size;
            return true;
        }
    }
    public void dropDataBase()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery("DROP TABLE " + TABLE_NAME, null);
    }
    public void insertImg(int id , Bitmap img ) {
        // String insertStatement_logo = "INSERT INTO "
        SQLiteDatabase db = this.getWritableDatabase();
        byte[] data = getBitmapAsByteArray(img); // this is a function
        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL_6, img).bindLong(1, id);
//        insertStatement_logo.bindBlob(2, data);
//
//        insertStatement_logo.executeInsert();
//        insertStatement_logo.clearBindings() ;

    }
    //TODO method returning One Exercise - I will use that in  both list and description
    //TODO so I need 2 methods - one for the list and one for the description
    //TODO - I don't need a description in the list
    //doesn't work properly!!!
//    public Exercise getExerciseFromDatabase(int id){
//        SQLiteDatabase db = this.getWritableDatabase();
////        if(db.rawQuery("SELECT " + "*" + " FROM " + TABLE_NAME + " WHERE " + )){
////
////        }
//
//        Exercise exercise = new Exercise(
//                db.rawQuery("SELECT " + COL_2 + " FROM " + TABLE_NAME + " WHERE " + COL_1 + " = " + id + ";", null).toString(),
//                db.rawQuery("SELECT " + COL_3 + " FROM " + TABLE_NAME + " WHERE " + COL_1 + " = " + id + ";", null).toString(),
//                db.rawQuery("SELECT " + COL_5 + " FROM " + TABLE_NAME + " WHERE " + COL_1 + " = " + id + ";", null).toString(),
//                db.rawQuery("SELECT " + COL_4 + " FROM " + TABLE_NAME + " WHERE " + COL_1 + " = " + id + ";", null).toString());
//        return exercise;
//    }
    public static Bitmap getImage(int id, int columnIndex){
        //SQLiteDatabase db = this.getWritableDatabase();
        //String qu = "select " + COL_6 + "  from " + TABLE_NAME + " where " + COL_1 + "=" + i ;
        //Cursor cur = db.rawQuery(qu, null);
        Cursor cur = MainActivity.myDb.getOneRow(id);
        cur.moveToFirst();
        if (cur.getBlob(columnIndex) != null){
            byte[] imgByte = cur.getBlob(columnIndex);
            cur.close();
            return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        }
        if (cur != null && !cur.isClosed()) {
            cur.close();
        }

        return null;
    }
    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_4, null);
        return result;
    }
    public Cursor getCategorizedData(int category)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String categoryString;
        switch (category)
        {
            case 0:
            {
                categoryString = "\'Chest\'";
                break;
            }
            case 1:
            {
                categoryString = "\'Arms\'";
                break;
            }
            case 2:
            {
                categoryString = "\'ABS & Back\'";
                break;
            }
            case 3:
            {
                categoryString = "\'Shoulders\'";
                break;
            }
            case 4:
            {
                categoryString = "\'Legs\'";
                break;
            }
            default:
            {
                return getAllData();
            }
        }
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_3 + " = " + categoryString + " ORDER BY " + COL_4, null);
        return result;
    }
    public Cursor getOneRow(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = " + id,null);
        return result;
    }


}
