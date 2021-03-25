package com.example.diary.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.diary.R;

public class AlarmReceiver extends BroadcastReceiver {
    int NOTICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.d("확인", "실행11121");
        int requestID = (int) System.currentTimeMillis();
        Log.d("확인", "실행1");
        Toast.makeText(context,"알람 울립니다.11", Toast.LENGTH_LONG).show();

//        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Log.d("확인", "실행2");
//
        Intent notificationIntent = new Intent(context, MainActivity.class); //프래크먼트로 이동할 수 없음
        notificationIntent.putExtra("particularFragment", "notiIntent"); //프래그먼트로 이동하기 위한 절차
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Log.d("확인", "실행3");
        PendingIntent pendingIntent = PendingIntent.getActivity(context,requestID,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT); //알림창 터치시 액티비티 실행

        Log.d("확인", "실행4");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "일정 알림 채널";
            String description = "저장된 일정을 알립니다.";
            int importance = NotificationManager.IMPORTANCE_HIGH; //소리와 알림메시지를 같이 보여줌
            NotificationChannel channel = new NotificationChannel("default", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if(notificationManager != null)
                notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");
        builder.setContentTitle("일정") //알림창 제목
                .setContentText("일정입니다.") //알림창 내용
                .setSmallIcon(R.drawable.ic_launcher_background) // 아이콘 설정하지 않으면 오류남
                .setDefaults(Notification.DEFAULT_VIBRATE) //알림을 진동으로 설정
                .setContentIntent(pendingIntent) //알림창 터치시 이동하는 인텐트
                .setPriority(Notification.PRIORITY_MAX) //헤드업 알림 설정하기 위해 MAX나 HIGH을 사용해야 가능
//                .setFullScreenIntent(pendingIntent, true)
                .setAutoCancel(true); //알림창 터치시 자동 삭제
//                .setChannelId("default");
        Log.d("확인", "실행5");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1 , builder.build()); //노티피케이션 작동

        Log.d("확인", "실행6");
//        Intent serviceIntent = new Intent(context, AlarmService.class);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
//            context.startForegroundService(serviceIntent);
//        } else {
//            context.startService(serviceIntent);
//        }


    }
}
