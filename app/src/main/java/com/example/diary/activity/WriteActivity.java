package com.example.diary.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.diary.R;
import com.example.diary.data.DiaryDBHelper;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class WriteActivity extends AppCompatActivity implements AutoPermissionsListener {

    //요청코드
    int GET_GALLERY_IMAGE = 200;
    int GET_CAPTURE_IMAGE = 201;

    TextView title, content, date_text;
    Button gallery, camera, store;
    ImageView[] imageViews = new ImageView[10];
    int[] imageViewId = {R.id.photo1, R.id.photo2, R.id.photo3, R.id.photo4, R.id.photo5, R.id.photo6, R.id.photo7, R.id.photo8, R.id.photo9, R.id.photo10};
    ArrayList<Uri> imageUris;
    ArrayList <Uri> modi_uris;
    Uri photoURI;
    String currentPhotoPath;
    String id;
    boolean modify;
    int store_state = 0;
    int image_ch = 1;
    int year, month, day;

    DiaryDBHelper diaryDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.diary_preferences",MODE_PRIVATE);

        if(sharedPreferences.getBoolean("mode",false) == true){
            setTheme(R.style.DarkTheme);
        }else{
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_write);

        AutoPermissions.Companion.loadAllPermissions(this, 101); //권한

        diaryDBHelper = new DiaryDBHelper(this);

        for (int i = 0; i < 10; i++) {
            imageViews[i] = findViewById(imageViewId[i]);
        }
        gallery = findViewById(R.id.gallery);

        //갤러리에서 이미지 가져오기
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); //이미지 여러장 선택
                intent.setAction(intent.ACTION_GET_CONTENT); //앨범 호출
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        camera = findViewById(R.id.camera);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        title = findViewById(R.id.title);
        content = findViewById(R.id.content);

        store = findViewById(R.id.store);

        date_text = findViewById(R.id.date_text);

        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t = title.getText().toString();
                String c = content.getText().toString();
                String date = date_text.getText().toString();

                if (store_state == 0) { //새로 작성
                    String imagePath = uri_path(imageUris.get(0));
                    diaryDBHelper.insert(t, imagePath, c, date);
                    finish();
                } else { //수정
                    String imagePath;
                    if(image_ch == 0) { //사진 수정했을 경우
                        imagePath = uri_path(imageUris.get(0));
                    }else{
                        imagePath = uri_path(modi_uris.get(0));
                    }
                    diaryDBHelper.update(id, t, imagePath, c, date);
                    finish();
                }
            }
        });

        Intent intent = getIntent();
        modify = intent.getExtras().getBoolean("modify");

        //수정
        if (modify == true) {
            id = intent.getExtras().getString("id");
            store_state = 1;

            String sql = "select * from " + diaryDBHelper.TABLE_NAME + " where _id = " + "'" + id + "'";

            Cursor cursor = diaryDBHelper.select_sql(sql);
            if (cursor.moveToNext()) {
                String i = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.IMAGE));
                String t = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.TITLE));
                String c = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.CONTENT));
                String d = cursor.getString(cursor.getColumnIndexOrThrow(diaryDBHelper.DATE));
                Uri uriImage = getUriFromPath(i);
                modi_uris = new ArrayList<>();
                modi_uris.add(uriImage);
                imageViews[0].setImageURI(modi_uris.get(0));

                title.setText(t);
                content.setText(c);
                date_text.setText(d);
            }
        } else {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            date_text.setText(year + "년 " + (month + 1) + "월 " + day + "일"); //디폴트
        }
        LinearLayout date_set = findViewById(R.id.date_set);
        date_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(WriteActivity.this, AlertDialog.THEME_HOLO_LIGHT, listener, year, month, day).show();
            }
        });
    }

    //datepicker
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            date_text.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
        }
    };

    //uri를 path로 바꾸기
    private String uri_path(Uri uri) {
        String id;
        if(image_ch == 0) {
            id = uri.getLastPathSegment().split(":")[1];

        }else {
            List<String> pathSegments = uri.getPathSegments();
            id = pathSegments.get(pathSegments.size() - 1);
        }
        final String[] imageColumns = {MediaStore.Images.Media.DATA};
        final String imageOrderBy = null;

        Uri uriImage = getUri();
        String selectedImagePath = "path";

        Cursor imageCursor = getContentResolver().query(uriImage, imageColumns,
                MediaStore.Images.Media._ID + "=" + id, null, imageOrderBy);

        if (imageCursor.moveToFirst()) {
            selectedImagePath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
        }
        Log.e("path", selectedImagePath); // use selectedImagePath

        imageCursor.close();
        return selectedImagePath;
    }


    private Uri getUri() {
        String state = Environment.getExternalStorageState();
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    //이미지뷰에 올라온 사진을 바이드단위로 바꿔줌
    private byte[] imageViewToByte(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream); //70% 압축
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    private File createImageFile() throws IOException {
        Log.d("확인", "2");
        // Create an image file name 이미지 파일 이름
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        Log.d("확인", storageDir.toString());
//        File image1 = new File("/sdcard/DCIM/Camera/" + imageFileName + ".jpg");
        // Save a file: path for use with ACTION_VIEW intents 파일저장
        currentPhotoPath = image.getAbsolutePath();
        Log.d("확인", currentPhotoPath);
        return image;
    }


    private void dispatchTakePictureIntent() {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go 사진이 들어갈 파일 만들기
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.d("확인", "예외");
                }
                // Continue only if the File was successfully created 파일이 생성된 경우 실행
                if (photoFile != null) {
                    try {
                        photoURI = FileProvider.getUriForFile(WriteActivity.this, "com.example.diary", photoFile);
                    } catch (IllegalArgumentException e) {
                        Toast.makeText(this, "예외2", Toast.LENGTH_LONG).show();
                    }
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, GET_CAPTURE_IMAGE);
                }
            }
        }
    }

    //갤러리에 사진 추가
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath); //
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        image_ch = 0;
        //갤러리에서 이미지 가져오기
        if (requestCode == GET_GALLERY_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data.getClipData() != null || data.getData() != null) {
                imageUris = new ArrayList<>(); //이미지 선택하면 객체 생성
                for (int i = 0; i < 10; i++) {
                    imageViews[i].setImageResource(0); //이미지 선택하면 이전 이미지는 이미지뷰에서 리셋
                }
            }
            if (data.getClipData() != null) {
                //여러 장
                int count = data.getClipData().getItemCount();

                if (count > 10) {
                    Toast.makeText(this, "사진은 10장까지 선택할 수 있습니다.", Toast.LENGTH_LONG).show();
                } else {
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imageUris.add(imageUri);
                        imageViews[i].setImageURI(imageUris.get(i));
                        Log.d("확인", "이미지: " + i + "->" + imageUris.get(i));
                    }
                }
            } else {
                //한장
                Uri imageUri = data.getData();
                Log.d("확인", "uri : " + imageUri);
                imageUris.add(imageUri);
                imageViews[0].setImageURI(imageUris.get(0));
            }

        }
        //사진 촬영
        else if (requestCode == GET_CAPTURE_IMAGE && resultCode == Activity.RESULT_OK) {
            galleryAddPic();

            /* 이미지 파일의 용량이 너무 커서 그대로 앱에 띄울 경우
             * 메모리 부족으로 비정상 종료될 수 있으므로 크기를 줄여 비트맵으로 로딩한 후 설정 */
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8; // 1/8 로 크기를 줄임
            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, options);
            imageViews[0].setImageBitmap(bitmap);

        }
    }

    //path를 uri로 바꾸기
    public Uri getUriFromPath(String filePath) {
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, "_data = '" + filePath + "'", null, null);

        cursor.moveToNext();
        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

        return uri;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }

    @Override
    public void onDenied(int i, String[] permissions) {
//        Toast.makeText(this, "권한 거부: " + permissions.length, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGranted(int i, String[] permissions) {
//        Toast.makeText(this, "권한 동의: " + permissions.length, Toast.LENGTH_LONG).show();
    }
}
