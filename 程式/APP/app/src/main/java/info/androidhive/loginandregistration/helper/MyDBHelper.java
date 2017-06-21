package info.androidhive.loginandregistration.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
    public  static final String DATABASE_NAME="order.db";
    public  static final String Table_Name="ordertest";
    public  static final String COL_1=" _ID";
    public  static final String COL_2="english";
    public  static final String COL_4="chinese";
    public  static final String COL_3="amount";
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + Table_Name + " ( " +
                    COL_1 + " INTEGER PRIMARY KEY autoincrement  , " +
                    COL_2 + " TEXT  NOT NULL  , "+
                    COL_4 + " TEXT  NOT NULL  , "+
                    COL_3 + " INTEGER  NOT NULL );" ;//欄位名字和資料庫 以及欄位不要重複  會壞掉
    public static final String DROP_TABLE="DROP TABLE IF EXIST "+Table_Name;
    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }
    public void  insert(String english,String chinese,int amount)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put("english",english);
        contentValues.put("chinese",chinese);
        contentValues.put("amount",amount);
        this.getWritableDatabase().insertOrThrow(Table_Name,"",contentValues);
    }
    public void delete(String chinese)
    {
        this.getWritableDatabase().delete(Table_Name,"chinese='"+chinese+"'",null);
    }
    public void update(String chinese, int amount)
    {
        if(amount>0)
            this.getWritableDatabase().execSQL("UPDATE "+Table_Name+" SET amount='"+amount+"' WHERE chinese='"+chinese+"'");
        else
        {
            delete(chinese);
        }
    }
    public Cursor list_all()
    {
        Cursor cursor=this.getReadableDatabase().rawQuery("SELECT * FROM "+Table_Name,null);
        return cursor;
    }
}