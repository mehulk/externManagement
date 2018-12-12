package com.example.hp.externapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MyAdapter  extends ArrayAdapter<ExternInfo> {
    private final Context context;
    private final ArrayList<ExternInfo> externsInfoList;

    public MyAdapter(Context context, ArrayList<ExternInfo> externsInfoList) {
        super(context, R.layout.card_layout_admin, externsInfoList);
        this.context = context;
        this.externsInfoList = externsInfoList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View externInfoView;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Change the layout based on who the message is from
        externInfoView = inflater.inflate(R.layout.card_layout_admin, parent, false);
        TextView textName = externInfoView.findViewById(R.id.textName);
        textName.setText(externsInfoList.get(position).Name);

        TextView textMobile = externInfoView.findViewById(R.id.textMobile);
        textMobile.setText(externsInfoList.get(position).Mobile);

        TextView textEmail = externInfoView.findViewById(R.id.textEmail);
        textEmail.setText(externsInfoList.get(position).Email);

        TextView textLocation = externInfoView.findViewById(R.id.textLocation);
        textLocation.setText(externsInfoList.get(position).Location);

        return externInfoView;
    }
}

















//public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private Context context;
//    private ArrayList<ExternInfo> externs;
//
//    private static final int VIEW_HOLDER_TYPE_1=1;
//
//    public static class viewHolder extends RecyclerView.ViewHolder {
//        // each data item is just a string in this case
//        public TextView textName,textEmail,textMobile,textLocation;
//        public ImageView imageExtern;
//        public viewHolder(View v) {
//            super(v);
//            this.textName = (TextView) v.findViewById(R.id.textName);
//            this.textEmail = (TextView) v.findViewById(R.id.textEmail);
//            this.textMobile = (TextView) v.findViewById(R.id.textMobile);
//            this.textLocation = (TextView) v.findViewById(R.id.textLocation);
//            this.imageExtern = (ImageView) v.findViewById(R.id.imageExtern);
//        }
//
//
//    }
//
//    public MyAdapter(Context context, ArrayList<ExternInfo> externs) {
//        this.context = context;
//        this.externs = externs;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
//                                                      int viewType) {
//
//        View v;
//        v = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.card_layout_admin, parent, false);
//        viewHolder vh1 = new viewHolder(v);
//        return vh1;
//
//
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        ExternInfo ei = externs.get(position);
//        viewHolder vh = (viewHolder) holder;
//        vh.textName.setText(ei.Name);
//        vh.textEmail.setText(ei.Email);
//        vh.textMobile.setText(ei.Mobile);
//        vh.textLocation.setText(ei.Location);
//
//        }
//
//
//    @Override
//    public int getItemCount() {
//        return externs.size();
//    }
//}









