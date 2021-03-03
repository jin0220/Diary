package com.example.diary.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.diary.R;
import com.example.diary.data.DiaryDBHelper;
import com.example.diary.fragment.CalendarFragment;
import com.example.diary.fragment.MainFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    MainFragment mainFragment;
    CalendarFragment calendarFragment;

    DiaryDBHelper diaryDBHelper;

    TextView total, favorite, today;

    public static Activity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.diary_preferences",MODE_PRIVATE);

//        Log.d("확인","모드" + sharedPreferences.getBoolean("mode",false));
        if(sharedPreferences.getBoolean("mode",false) == true){
            setTheme(R.style.DarkTheme);
//            Log.d("확인","모드1" + sharedPreferences.getBoolean("mode",false));
        }else{
            setTheme(R.style.LightTheme);
//            Log.d("확인","모드2" + sharedPreferences.getBoolean("mode",false));
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        total = header.findViewById(R.id.total);
        favorite = header.findViewById(R.id.favorite);
        today = header.findViewById(R.id.today);

        diaryDBHelper = new DiaryDBHelper(this);


        Cursor cursor1 = diaryDBHelper.select();
        String t = Integer.toString(cursor1.getCount()); //레코드 개수 반환
        total.setText(t);

        String sql = "select favorite from diary_table where favorite = 1";
        Cursor cursor2 = diaryDBHelper.select_sql(sql);
        String f = Integer.toString(cursor2.getCount()); //레코드 개수 반환 (좋아요를 누르고 메인으로 돌아갔을 때 값도 바로 바뀔 수 있게 수정하기)
        favorite.setText(f);

        String click_date = Calendar.getInstance().get(Calendar.YEAR) + "년 " + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "월 " + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "일";
        String sql2 = "select * from " + diaryDBHelper.TABLE_SCHEDULE + " where start_date like " + "'" + click_date + "%'";
        Cursor cursor3 = diaryDBHelper.select_sql(sql2);
        String day = Integer.toString(cursor3.getCount()); //레코드 개수 반환
        today.setText(day);

        mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container,mainFragment).commit(); //프래그먼트 생성

        mainActivity = MainActivity.this;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //드로어 메뉴
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
        } else if (id == R.id.nav_calendar) {
            calendarFragment = new CalendarFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, calendarFragment).commit();
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    boolean lock = true; //잠금 상태 확인

    @Override
    protected void onStart() {
        super.onStart();
        AppLock appLock = new AppLock(this);
        if(lock && appLock.isPassLockSet()){
            Intent intent = new Intent(this, PasswordActivity.class);
            intent.putExtra("type", AppLockConst.UNLOCK_PASSWORD);
            startActivityForResult(intent, AppLockConst.UNLOCK_PASSWORD);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppLockConst.UNLOCK_PASSWORD && resultCode == Activity.RESULT_OK){
            lock = false; //잠금 해제
        }
    }
}
