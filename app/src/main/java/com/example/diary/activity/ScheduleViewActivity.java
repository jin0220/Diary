package com.example.diary.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diary.R;
import com.example.diary.data.DiaryDBHelper;
import com.example.diary.fragment.CalendarFragment;

public class ScheduleViewActivity extends AppCompatActivity {

    TextView title, memo, start_time, end_time;
    String date, get_title, _id;
    DiaryDBHelper diaryDBHelper;
    Button modify, remove;
    int MODIFY_REQUEST = 1;

    public static Activity scheduleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_view);

        title = findViewById(R.id.title);
        memo = findViewById(R.id.memo);
        start_time = findViewById(R.id.startTime);
        end_time = findViewById(R.id.endTime);
        modify = findViewById(R.id.modify);
        remove = findViewById(R.id.remove);

        scheduleView = ScheduleViewActivity.this;

        final Intent intent = getIntent();
        date = intent.getExtras().getString("date");
        get_title = intent.getExtras().getString("title");

        diaryDBHelper = new DiaryDBHelper(this);

        String sql = "select * from " + diaryDBHelper.TABLE_SCHEDULE + " where start_date like " + "'" + date + "%' and title = '" + get_title + "'";

        Cursor cursor = diaryDBHelper.select_sql(sql);
        if (cursor.moveToNext()) {
            _id = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper._ID));
            String t = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.TITLE));
            String m = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.MEMO));
            String s_t = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.START_DATE));
            String e_t = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.END_DATE));

            title.setText(t);
            memo.setText(m);
            start_time.setText(s_t);
            end_time.setText(e_t);
        }

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(ScheduleViewActivity.this, ScheduleWriteActivity.class);
                intent2.putExtra("modify",true);
                intent2.putExtra("_id",_id);
                startActivityForResult(intent2, MODIFY_REQUEST);
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleViewActivity.this);
                builder.setMessage("일정을 삭제할까요?");
                builder.setPositiveButton("삭제",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                diaryDBHelper.schedule_delete(_id);
                                Intent intent3 = new Intent(ScheduleViewActivity.this, CalendarFragment.class);
                                intent3.putExtra("state", 1);
                                setResult(RESULT_OK, intent3);
                                finish();
                            }
                        });
                builder.setNegativeButton("취소", null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == MODIFY_REQUEST && resultCode == Activity.RESULT_OK){
            String start_date = intent.getExtras().get("start_date").toString();
            String end_date = intent.getExtras().get("end_date").toString();

            title.setText(intent.getExtras().get("title").toString());
            memo.setText(intent.getExtras().get("memo").toString());
            start_time.setText(start_date);
            end_time.setText(end_date);

            Intent intent4 = new Intent(this, CalendarFragment.class); //수정 시 캘린더 메인에도 수정이 필요하기 때문에 요청 보냄
            intent4.putExtra("state",2);
            intent4.putExtra("title",intent.getExtras().get("title").toString());
            intent4.putExtra("time", start_date.split(" ")[3] + " " + start_date.split(" ")[4] + " ~ " +
                    end_date.split(" ")[3] + " " + end_date.split(" ")[4]);
            setResult(RESULT_OK,intent4);
        }
    }
}