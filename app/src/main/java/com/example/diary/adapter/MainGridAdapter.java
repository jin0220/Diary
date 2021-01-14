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

    TextView textView, id;
    ImageView imageView;

    public static final int HEADER_VIEW = 0;
    public static final int ITEM_VIEW = 1;

    ArrayList<MainGridData> items = new ArrayList<>();

    public  class HeaderView extends MainGridAdapter.ViewHolder{

        public HeaderView(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ViewHolder(View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조. (hold strong reference)
            textView = itemView.findViewById(R.id.textView) ;
            imageView = itemView.findViewById(R.id.imageView);
            id = itemView.findViewById(R.id.id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ReadActivity.class);//인텐트 안에서 getApplicationContext()가 에러 난다면 앞에 getActivity() 붙여줌
                    int position = getAdapterPosition();
                    MainGridData data = (MainGridData) items.get(position);
                    intent.putExtra("id", data.getId());
                    v.getContext().startActivity(intent);
//                    ((Activity)v.getContext()).finish();
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
            id.setText(mainGridData.getId());

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

    //grid
    public void addData(String id, String text, Uri image, int viewType){

        MainGridData item = new MainGridData();

        String date = convert(text);

        item.setId(id);
        item.setText(date);
        item.setImage(image);
        item.setViewType(viewType);

        items.add(item);
    }


    //header
    public void addData2(String text, int viewType){
        MainGridData item = new MainGridData();

        item.setText(text);
        item.setViewType(viewType);

        items.add(item);

    }

    private String convert(String text) {
        String[] date = text.split(" ");
        String[] day = date[2].split("일");
        switch (date[1]){
            case "01월":
                date[1] = "Jan.";
                break;
            case "02월":
                date[1] = "Feb.";
                break;
            case "03월":
                date[1] = "Mar.";
                break;
            case "04월":
                date[1] = "Apr.";
                break;
            case "05월":
                date[1] = "May.";
                break;
            case "06월":
                date[1] = "Jun.";
                break;
            case "07월":
                date[1] = "Jul.";
                break;
            case "08월":
                date[1] = "Aug.";
                break;
            case "09월":
                date[1] = "Sep.";
                break;
            case "10월":
                date[1] = "Oct.";
                break;
            case "11월":
                date[1] = "Nov.";
                break;
            case "12월":
                date[1] = "Dec.";
                break;
        }
        return date[1] + "\n" + day[0];
    }
}
