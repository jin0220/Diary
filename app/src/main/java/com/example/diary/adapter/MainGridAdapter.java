package com.example.diary.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.diary.R;
import com.example.diary.activity.ReadActivity;
import com.example.diary.data.MainGridData;

import java.util.ArrayList;

public class MainGridAdapter extends RecyclerView.Adapter<MainGridAdapter.ViewHolder> {

    TextView textView;
    ImageView imageView;

    public static final int HEADER_VIEW = 0;
    public static final int ITEM_VIEW = 1;

    ArrayList<MainGridData> items = new ArrayList<>();

    public  class HeaderView extends MainGridAdapter.ViewHolder{

        public HeaderView(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.date);

        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ViewHolder(View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조. (hold strong reference)
            textView = itemView.findViewById(R.id.textView) ;
            imageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO : process click event.
                    Intent intent = new Intent(v.getContext(), ReadActivity.class);//인텐트 안에서 getApplicationContext()가 에러 난다명 앞에 getActivity() 붙여줌
//                Log.d("확인","실행 2");
                    int position = getAdapterPosition();
                    MainGridData data = (MainGridData) items.get(position);
//                Log.d("확인",  item.getText()); //날짜
//                String sql = "select * from "+ diaryDBHelper.TABLE_NAME + " where date = " + i;
//                Cursor cursor = diaryDBHelper.getReadableDatabase().rawQuery();
//                Log.d("확인","실행 3 : "+ da);
                    intent.putExtra("date", data.getText());
//                intent.putExtra("image", data.getImage().toString());
                    v.getContext().startActivity(intent);

                }
            });
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public MainGridAdapter() {

    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        switch (viewType){
            case HEADER_VIEW:
                view = inflater.inflate(R.layout.main_grid_header, parent, false) ;
                return new HeaderView(view) ;
            default:
                view = inflater.inflate(R.layout.activity_main_grid, parent, false) ;
                return new ViewHolder(view) ;
        }
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(MainGridAdapter.ViewHolder holder, int position) {

        MainGridData mainGridData = items.get(position);

        if(holder instanceof HeaderView){
            textView.setText(mainGridData.getText());
        }else{
            textView.setText(mainGridData.getText());

            Uri image = mainGridData.getImage();
            imageView.setImageURI(image);

//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 8; // 1/8 로 크기를 줄임
//            Bitmap bitmap = BitmapFactory.decodeFile(image, options);
//            imageView.setImageBitmap(bitmap);
        }
//        String text = mData.get(position) ;
//        holder.textView1.setText(text) ;
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return items.size() ;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    public void addData(String text, Uri image, int viewType){

        MainGridData item = new MainGridData();

        item.setText(text);
        item.setImage(image);
        item.setViewType(viewType);

        items.add(item);

    }

    public void addData2(String text, int viewType){

        MainGridData item = new MainGridData();

        item.setText(text);
        item.setViewType(viewType);

        items.add(item);

    }

//    TextView textView;
//    ImageView imageView;
//
//    ArrayList<MainGridData> items = new ArrayList<>();
//
//    public MainGridAdapter() {
//
//    }
//
//    @Override
//    public int getCount() {
//        return items.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return items.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        Context context = viewGroup.getContext();
//
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        view = inflater.inflate(R.layout.activity_main_grid,viewGroup,false);
//
//        textView = view.findViewById(R.id.textView);
//
//        imageView = view.findViewById(R.id.imageView);
//
//        MainGridData mainGridData = items.get(i);
//
//        textView.setText(mainGridData.getText()); //텍스트 설정
//
//        Uri image = mainGridData.getImage();
////        Log.d("확인", "어댑터 " + image);
////        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
////        Uri uriImage = Uri.parse(image);
////        Log.d("확인","uriimage : " + uriImage);
////        imageView.setImageURI(uriImage);
//        imageView.setImageURI(image);
//
//        return view;
//    }
//
//    public void addData(String text, Uri image){
//        String[] date = text.split("-");
//        String[] day = date[2].split(" ");
//        switch (date[1]){
//            case "01":
//                date[1] = "Jan.";
//                break;
//            case "02":
//                date[1] = "Feb.";
//                break;
//            case "03":
//                date[1] = "Mar.";
//                break;
//            case "04":
//                date[1] = "Apr.";
//                break;
//            case "05":
//                date[1] = "May.";
//                break;
//            case "06":
//                date[1] = "Jun.";
//                break;
//            case "07":
//                date[1] = "Jul.";
//                break;
//            case "08":
//                date[1] = "Aug.";
//                break;
//            case "09":
//                date[1] = "Sep.";
//                break;
//            case "10":
//                date[1] = "Oct.";
//                break;
//            case "11":
//                date[1] = "Nov.";
//                break;
//            case "12":
//                date[1] = "Dec.";
//                break;
//        }
//        MainGridData item = new MainGridData();
//
////        item.setText(date[1] + "\n" + day[0]);
//        item.setText(text);
//        item.setImage(image);
//
//        items.add(item);
//    }
}
