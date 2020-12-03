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
    public static String TABLE_DIARY= "diary_table";
    public static String TABLE_IMAGE = "image_table";

    //속성명(diary_table)
    public static String _ID = "_id";
    public static String DATE = "date";
    public static String TITLE = "title";
    public static String IMAGE = "image";
    public static String IMAGE_CODE = "image_code";
    public static String CONTENT = "content";
    public static String FAVORITE = "favorite";

    //속성명(image_table)
    public static String IMAGE1 = "image1";
    public static String IMAGE2 = "image2";
    public static String IMAGE3 = "image3";
    public static String IMAGE4 = "image4";
    public static String IMAGE5 = "image5";
    public static String IMAGE6 = "image6";
    public static String IMAGE7 = "image7";
    public static String IMAGE8 = "image8";
    public static String IMAGE9 = "image9";
    public static String IMAGE10 = "image10";


    SQLiteDatabase sqLiteDatabase;

    public DiaryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    //이미지를 저장할 때 NULL이 아니면 onCreate에서 초기화한 num을 저장 insert부분에서 num을 증가
    //외래키 설정? sqlite에서 지원이 되나?

//테이블 생성
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String diary_table = "create table if not exists " + TABLE_DIARY + "("
                + _ID + " integer PRIMARY KEY autoincrement, "
                +  DATE  +" DATE, "
                + TITLE + " TEXT, "
                + IMAGE + " TEXT, "
                + IMAGE_CODE + " TEXT, "
                + CONTENT + " TEXT, "
                + FAVORITE + " INTEGER DEFAULT 0)";

        String image_table = "create table if not exists " + TABLE_IMAGE + "("
                + IMAGE_CODE + " PRIMARY KEY, "
                + IMAGE1  +" TEXT, "
                + IMAGE2 + " TEXT, "
                + IMAGE3 + " TEXT, "
                + IMAGE4 + " TEXT, "
                + IMAGE5 + " TEXT, "
                + IMAGE6 + " TEXT, "
                + IMAGE7 + " TEXT, "
                + IMAGE8 + " TEXT, "
                + IMAGE9 + " TEXT, "
                + IMAGE10 + " TEXT)";

        try {
            sqLiteDatabase.execSQL(diary_table);
            sqLiteDatabase.execSQL(image_table);
        }catch (Exception e){
            Log.e("Database","Exception in CREATE_SQL", e);
        }

        Log.d("확인","onCreate 호출");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(newVersion > 1){
            try {
                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DIARY);
                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE);
            }catch (Exception e){
                Log.e("Database","Exception in  UPGRADE_SQL", e);
            }
        }
    }

    //diary_table
    public void insert(String t, String i, String c, String date, String code){
        sqLiteDatabase = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TITLE, t);
        values.put(IMAGE, i);
        if(i != null)
            values.put(IMAGE_CODE, code);
//        else{
//            values.put(IMAGE_CODE,null);
//        }
        values.put(CONTENT, c);
        values.put(DATE, date);

        sqLiteDatabase.insert(DiaryDBHelper.TABLE_DIARY, null, values); //레코드 삽입
    }

    public Cursor select(){
        sqLiteDatabase = getReadableDatabase(); //데이터 읽어 오기

        String[] projection = {
                _ID,
                DATE,
                TITLE,
                IMAGE,
                IMAGE_CODE,
                CONTENT
        };

        String sortOrder = DATE + " DESC, " + _ID + " DESC";

        Cursor cursor = sqLiteDatabase.query(
                TABLE_DIARY,   // The table to query
                projection,             // 값을 가져올 column name의 배열
                null,              // where 문에 필요한 column
                null,          // where 문에 필요한 value
                null,                   // group by를 적용할 column
                null,                   // having 절
                sortOrder               // 정렬 방식
        );

        return  cursor;
    }

//    public Cursor read(String date){
//        sqLiteDatabase = getReadableDatabase();
//
//        String sql = "select * from "+ TABLE_NAME + " where date = " + "'" + date + "'";
////        Log.d("확인", sql);
//        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
//
//        return cursor;
//    }

    public  Cursor select_sql(String sql){
        sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);

        return cursor;
    }

    public boolean update(String id, String t, String i, String c , String date){
        ContentValues values = new ContentValues();
        values.put(_ID, id);
        values.put(TITLE, t);
        values.put(IMAGE_CODE, i); //수정
        values.put(CONTENT, c);
        values.put(DATE, date);
        return sqLiteDatabase.update(TABLE_DIARY, values, "_id=" + id, null) > 0;
    }

    public boolean favorit_update(String id, int favorite){
        ContentValues values = new ContentValues();
        values.put(_ID, id);
        values.put(FAVORITE, favorite);
        return sqLiteDatabase.update(TABLE_DIARY, values, "_id=" + id, null) > 0;
    }

    public Boolean delete(String id){
        return sqLiteDatabase.delete(TABLE_DIARY, "_id = " + id, null) > 0;
    }

        /*
        public void open(){
            DatabaseHelper databaseHelper = new DatabaseHelper(this, DATABASE_NAME, null, VERSION); //버전을 바꿀 수 있음
            SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase(); //읽고 쓰기 위해 DB open.

        }*/

    //image_table
    public void image_insert(String code, String image1, String image2, String image3, String image4, String image5, String image6, String image7, String image8, String image9, String image10){
        sqLiteDatabase = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(IMAGE_CODE, code);
        values.put(IMAGE1, image1);
        values.put(IMAGE2, image2);
        values.put(IMAGE3, image3);
        values.put(IMAGE4, image4);
        values.put(IMAGE5, image5);
        values.put(IMAGE6, image6);
        values.put(IMAGE7, image7);
        values.put(IMAGE8, image8);
        values.put(IMAGE9, image9);
        values.put(IMAGE10, image10);

        sqLiteDatabase.insert(DiaryDBHelper.TABLE_IMAGE, null, values); //레코드 삽입
    }
}