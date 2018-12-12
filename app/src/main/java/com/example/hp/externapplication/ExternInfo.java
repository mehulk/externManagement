package com.example.hp.externapplication;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;

public class ExternInfo implements Serializable{
    String Name;
    String Email ,Password , Location, Gender, Mobile;
    ArrayList<Task> tasks ;


    public ExternInfo(String name, String email, String password, String location, String gender, String mobile){
        this.Name=name;
        this.Email=email;
        this.Gender=gender;
        this.Mobile=mobile;
        this.Location=location;
        this.Password = password;
        tasks = new ArrayList<>();
    }
    public void addTask(Task t){
        tasks.add(t);
    }

}

 class Task implements  Serializable{
    String task;
    boolean isDone;
    String time;
    public Task(String task,String time){
        this.task=task;
        isDone=false;
        this.time=time;

     }
     public void taskDone(){
        isDone=true;
     }

}
