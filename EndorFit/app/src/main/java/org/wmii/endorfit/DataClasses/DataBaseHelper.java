package org.wmii.endorfit.DataClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import org.wmii.endorfit.Activities.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;


public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Exercises.db";
    public static final String TABLE_NAME_MAIN = "exerciselist_table";
    public static final String TABLE_NAME_CATEGORIES = "categorieslist_table";
    public static final String TABLE_NAME_DIFFICULTY_LVLS = "difficultylevels_table";
    public static final String TABLE_NAME_INTERNAL_TYPES = "internal_types_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME_EX";
    public static final String COL_3 = "CATEGORY_ID";
    public static final String COL_4 = "DIFFICULTY_LEVEL_ID";
    public static final String COL_5 = "DESCRIPTION";
    public static final String COL_6 = "IMAGE_FILE_NAME";
    public static final String COL_7 = "INTERNAL_TYPE_ID";
    public static final String CCOL_2 = "NAME";
    private int sizeMain = 0;
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
        super(context, DATABASE_NAME, null, 1);
    }

    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

//    public DataBaseHelper(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
//        super(context, name, version, openParams);
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_MAIN
                + "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_2 + " TEXT UNIQUE," +
                COL_3 + " INTEGER," +
                COL_4 + " INTEGER," +
                COL_5 + " TEXT," +
                COL_6 + " TEXT," +
                COL_7 + " INTEGER," +
                "FOREIGN KEY " + "(" + COL_3 + ")" +
                " REFERENCES " + TABLE_NAME_CATEGORIES + " (" + COL_3 + ")," +
                "FOREIGN KEY " + "(" + COL_7 + ")" +
                " REFERENCES " + TABLE_NAME_INTERNAL_TYPES + " (" + COL_7 + ")," +
                "FOREIGN KEY " + "(" + COL_4 + ")" +
                " REFERENCES " + TABLE_NAME_DIFFICULTY_LVLS + " (" + COL_4 + ")" + ");");
        db.execSQL("CREATE TABLE " + TABLE_NAME_CATEGORIES +
                "(" + COL_3 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CCOL_2 + " TEXT UNIQUE);");
        db.execSQL("CREATE TABLE " + TABLE_NAME_DIFFICULTY_LVLS +
                "(" + COL_4 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CCOL_2 + " TEXT UNIQUE);");
        db.execSQL("CREATE TABLE " + TABLE_NAME_INTERNAL_TYPES +
                "(" + COL_7 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CCOL_2 + " TEXT UNIQUE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_INTERNAL_TYPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DIFFICULTY_LVLS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MAIN);
        onCreate(db);
    }


    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 16, outputStream);
        return outputStream.toByteArray();
    }

    public boolean insertDataMainTable(String name, int category, int difficulty, String description, String imageFileName, int internalType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, category);
        contentValues.put(COL_4, difficulty);
        contentValues.put(COL_5, description);
        contentValues.put(COL_6, imageFileName);
        contentValues.put(COL_7, internalType);

        long result = db.insert(TABLE_NAME_MAIN, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            ++sizeMain;
            return true;
        }
    }

    public boolean insertDataCategoryTable(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CCOL_2, name);

        long result = db.insert(TABLE_NAME_CATEGORIES, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            ++sizeMain;
            return true;
        }
    }

    public boolean insertDataDifficultyTable(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CCOL_2, name);

        long result = db.insert(TABLE_NAME_DIFFICULTY_LVLS, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            ++sizeMain;
            return true;
        }
    }

    public boolean insertDataInternalTypeTable(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CCOL_2, name);

        long result = db.insert(TABLE_NAME_INTERNAL_TYPES, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            ++sizeMain;
            return true;
        }
    }

    public void dropDataBase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_INTERNAL_TYPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DIFFICULTY_LVLS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MAIN);
    }

    public static Bitmap getImage(int id, int columnIndex) {
        //SQLiteDatabase db = this.getWritableDatabase();
        //String qu = "select " + COL_6 + "  from " + TABLE_NAME + " where " + COL_1 + "=" + i ;
        //Cursor cur = db.rawQuery(qu, null);
        Cursor cur = MainActivity.myDb.getOneRow(id);
        cur.moveToFirst();
        if (cur.getBlob(columnIndex) != null) {
            byte[] imgByte = cur.getBlob(columnIndex);
            cur.close();
            return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        }
        if (cur != null && !cur.isClosed()) {
            cur.close();
        }
        return null;
    }

    public static Bitmap getImageToList(int id, int columnIndex) {
        //SQLiteDatabase db = this.getWritableDatabase();
        //String qu = "select " + COL_6 + "  from " + TABLE_NAME + " where " + COL_1 + "=" + i ;
        //Cursor cur = db.rawQuery(qu, null);
        Cursor cur = MainActivity.myDb.getOneRow(id);
        cur.moveToFirst();
        if (cur.getBlob(columnIndex) != null) {
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

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT " + COL_1 + ", " + COL_2 + ", cat." + CCOL_2 + ", diflvl." + CCOL_2 + ", " + COL_5 + ", " + COL_6 + ", intertypes." + CCOL_2 +
                " FROM " + TABLE_NAME_MAIN +
                " JOIN " + TABLE_NAME_DIFFICULTY_LVLS + " diflvl USING (" + COL_4 + ")" +
                " JOIN " + TABLE_NAME_INTERNAL_TYPES + " intertypes USING (" + COL_7 + ")" +
                " JOIN " + TABLE_NAME_CATEGORIES + " cat USING (" + COL_3 + ")", null);
        return result;
    }

    public Cursor getDataForFirebase() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT " + COL_1 + ", " + COL_2 + ", intertypes." + CCOL_2 +
                " FROM " + TABLE_NAME_MAIN +
                " JOIN " + TABLE_NAME_INTERNAL_TYPES + " intertypes USING (" + COL_7 + ");", null);
        return result;
    }

    public Cursor getCategorizedData(int category) {
        SQLiteDatabase db = this.getReadableDatabase();


        if (category > 6 || category <= 0) {
            return getAllData();
        }

        Cursor result = db.rawQuery("SELECT " + COL_1 + ", " + COL_2 + ", cat." + CCOL_2 + ", diflvl." + CCOL_2 + ", " + COL_5 + ", " + COL_6 + ", intertypes." + CCOL_2 +
                " FROM " + TABLE_NAME_MAIN +
                " JOIN " + TABLE_NAME_DIFFICULTY_LVLS + " diflvl USING (" + COL_4 + ")" +
                " JOIN " + TABLE_NAME_INTERNAL_TYPES + " intertypes USING (" + COL_7 + ")" +
                " JOIN " + TABLE_NAME_CATEGORIES + " cat USING (" + COL_3 + ")" +
                " WHERE " + COL_3 + " = " + category + " ORDER BY " + COL_4, null);
        return result;
    }

    public Cursor getOneRow(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT " + COL_1 + ", " + COL_2 + ", cat." + CCOL_2 + ", diflvl." + CCOL_2 + ", " + COL_5 + ", " + COL_6 + ", intertypes." + CCOL_2 +
                " FROM " + TABLE_NAME_MAIN +
                " JOIN " + TABLE_NAME_DIFFICULTY_LVLS + " diflvl USING (" + COL_4 + ")" +
                " JOIN " + TABLE_NAME_INTERNAL_TYPES + " intertypes USING (" + COL_7 + ")" +
                " JOIN " + TABLE_NAME_CATEGORIES + " cat USING (" + COL_3 + ")" +
                " WHERE " + COL_1 + " = " + id, null);
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
        } catch (Exception ex) {
            Log.i("DATABASE", "Problem updating picture", ex);
            picturePath = "";
        }

        return picturePath;

    }
}

