package com.example.diary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//데이터베이스를 만들거나 열기 위해 필요한 일들을 도와주는 역할을 함. 이미 배포된 db 업그레이드 시 유용.
public class DiaryDBHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "Diary.db"; //데이터베이스 이름
    public static int VERSION = 1; //데이터베이스 버전
    public static String TABLE_NAME = "diary_table";

    public DiaryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

//테이블 생성
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table if not exists " + TABLE_NAME + "("
                + "_id integer PRIMARY KEY autoincrement, "
                + "date DATE, "
                + "title TEXT, "
                + "image STRING, "
                + "content TEXT)";

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

        /*
        public void open(){
            DatabaseHelper databaseHelper = new DatabaseHelper(this, DATABASE_NAME, null, VERSION);
            SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase(); //읽고 쓰기 위해 DB open.

        }*/

}