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

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Exercises.db";
    public static final String TABLE_NAME = "exerciselist_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "CATEGORY";
    public static final String COL_4 = "DIFFICULTY_LEVEL";
    public static final String COL_5 = "DESCRIPTION";
    public static final String COL_6 = "IMAGE";
    private int size = 0;
    private static final String TAG = "DataBaseHelper";

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
                COL_6 + " BLOB);");
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
    public boolean insertData(String name, String category, String difficulty, String description)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, category);
        contentValues.put(COL_4, difficulty);
        contentValues.put(COL_5, description);
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
    public Bitmap getImage(int i){
        SQLiteDatabase db = this.getWritableDatabase();
        String qu = "select " + COL_6 + "  from " + TABLE_NAME + " where " + COL_1 + "=" + i ;
        Cursor cur = db.rawQuery(qu, null);

        if (cur.moveToFirst()){
            byte[] imgByte = cur.getBlob(0);
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
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return result;
    }
    public Cursor getOneRow(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = " + id,null);
        return result;
    }


}
