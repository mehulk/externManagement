package com.example.hp.externapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class externTaskAdapter extends ArrayAdapter<Task> {
    Context context;
    ArrayList<Task> listTask;

    public externTaskAdapter(Context context, ArrayList<Task> listTask){
        super(context , R.layout.extern_task, listTask);
        this.context=context;
        this.listTask=listTask;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View externTaskView;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        externTaskView = inflater.inflate(R.layout.extern_task,parent,false);
        TextView textTask = externTaskView.findViewById(R.id.textTask);
        CheckBox boxTaskDone  = externTaskView.findViewById(R.id.boxTaskDone);
        TextView taskTime = externTaskView.findViewById(R.id.taskTime);
        textTask.setText(listTask.get(position).task);
        if(listTask.get(position).isDone){
            boxTaskDone.setChecked(true);
        }
        taskTime.setText(listTask.get(position).time.toString());

        return externTaskView;


    }
}
