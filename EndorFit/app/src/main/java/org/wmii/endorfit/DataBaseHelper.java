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
import java.io.FileOutputStream;
import java.sql.Blob;


public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Exercises.db";
    public static final String TABLE_NAME = "exerciselist_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "CATEGORY";
    public static final String COL_4 = "DIFFICULTY_LEVEL";
    public static final String COL_5 = "DESCRIPTION";
    public static final String COL_6 = "IMAGE_FILE_NAME";
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
                COL_6 + " TEXT,"  +
                COL_7 + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 16, outputStream);
        return outputStream.toByteArray();
    }
    public boolean insertData(String name, String category, String difficulty, String description, String imageFileName, String internalType)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, category);
        contentValues.put(COL_4, difficulty);
        contentValues.put(COL_5, description);
        contentValues.put(COL_6,imageFileName);
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
    public static Bitmap getImageToList(int id, int columnIndex){
        //SQLiteDatabase db = this.getWritableDatabase();
        //String qu = "select " + COL_6 + "  from " + TABLE_NAME + " where " + COL_1 + "=" + i ;
        //Cursor cur = db.rawQuery(qu, null);
        Cursor cur = MainActivity.myDb.getOneRow(id);
        cur.moveToFirst();
        if (cur.getBlob(columnIndex) != null){
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
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
        SQLiteDatabase db = this.getReadableDatabase();
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
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = " + id,null);
        return result;
    }
    public String getPicturePath(int id, Context context, Bitmap picture) {
        // Saves the new picture to the internal storage with the unique identifier of the report as
        // the name. That way, there will never be two report pictures with the same name.
        String picturePath = "";
        File internalStorage = context.getDir("DatabasePictures", Context.MODE_PRIVATE);
        File reportFilePath = new File(internalStorage + Integer.toString(id) + ".jpeg");
        picturePath = reportFilePath.toString();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(reportFilePath);
            picture.compress(Bitmap.CompressFormat.JPEG, 32 /*quality*/, fos);
            fos.close();
        }
        catch (Exception ex) {
            Log.i("DATABASE", "Problem updating picture", ex);
            picturePath = "";
        }

                return picturePath;

    }
}

