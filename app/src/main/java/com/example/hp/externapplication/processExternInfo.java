package com.example.hp.externapplication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.lang.reflect.Type;
import java.util.Arrays;

public class processExternInfo {



    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson = gsonBuilder.create();

    public ArrayList<ExternInfo> convertJsonStringToObject(String info){
        ArrayList<ExternInfo> externs = new ArrayList<>();
        Type type = new TypeToken<ArrayList<ExternInfo>>(){}.getType();
        externs = (gson.fromJson(info,type));

        return  externs;

    }
    public String  convertObjectToJson(ArrayList<ExternInfo> ei){
        String json =gson.toJson(ei);
        return  json;
    }

}
