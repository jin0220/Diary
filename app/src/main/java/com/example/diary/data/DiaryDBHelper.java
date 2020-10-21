package com.example.diary.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//데이터베이스를 만들거나 열기 위해 필요한 일들을 도와주는 역할을 함. 이미 배포된 db 업그레이드 시 유용.
public class DiaryDBHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "Diary.db"; //데이터베이스 이름
    public static int VERSION = 1; //데이터베이스 버전
    public static String TABLE_NAME = "diary_table";

    //속성명
    public static String _ID = "_id";
    public static String DATE = "date";
    public static String TITLE = "title";
    public static String IMAGE = "image";
    public static String CONTENT = "content";


    SQLiteDatabase sqLiteDatabase;

    public DiaryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

//테이블 생성
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table if not exists " + TABLE_NAME + "("
                + _ID + " integer PRIMARY KEY autoincrement, "
                +  DATE  +" DATE, "
                + TITLE + " TEXT, "
                + IMAGE + " TEXT, "
                + CONTENT + " TEXT)";

        try {
            sqLiteDatabase.execSQL(sql);
        }catch (Exception e){
            Log.e("Database","Exception in CREATE_SQL", e);
        }

        Log.d("확인","onCreate 호출");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(newVersion > 1){
            try {
                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            }catch (Exception e){
                Log.e("Database","Exception in  UPGRADE_SQL", e);
            }
        }
    }

    public void insert(String t, String i, String c, String date){
        sqLiteDatabase = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TITLE, t);
        values.put(IMAGE, i);
        values.put(CONTENT, c);
        values.put(DATE, date);

        sqLiteDatabase.insert(DiaryDBHelper.TABLE_NAME, null, values); //레코드 삽입
    }

    public Cursor select(){
        sqLiteDatabase = getReadableDatabase(); //데이터 읽어 오기

        String[] projection = {
                _ID,
                DATE,
                TITLE,
                IMAGE,
                CONTENT
        };

        String sortOrder = DATE + " DESC";

        Cursor cursor = sqLiteDatabase.query(
                TABLE_NAME,   // The table to query
                projection,             // 값을 가져올 column name의 배열
                null,              // where 문에 필요한 column
                null,          // where 문에 필요한 value
                null,                   // group by를 적용할 column
                null,                   // having 절
                sortOrder               // 정렬 방식
        );

        return  cursor;
    }

    public Cursor read(String date){
        sqLiteDatabase = getReadableDatabase();

        String sql = "select * from "+ TABLE_NAME + " where date = " + "'" + date + "'";
        Log.d("확인", sql);
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);

        return cursor;
    }

        /*
        public void open(){
            DatabaseHelper databaseHelper = new DatabaseHelper(this, DATABASE_NAME, null, VERSION);
            SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase(); //읽고 쓰기 위해 DB open.

        }*/

}