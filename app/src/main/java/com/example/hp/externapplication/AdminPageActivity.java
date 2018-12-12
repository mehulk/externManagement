package com.example.hp.externapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AdminPageActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView externList ;
    Context mContext;
    ArrayList<ExternInfo> externInfoList;
    MyAdapter mAdapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        mContext=this;

        //toolbar
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Extern List");

        //list view
        externList = findViewById(R.id.externList) ;

        externList.setOnItemClickListener(this);

        //DONE attach my_adapter

        listProcessor lp = new listProcessor();
        lp.execute("infoDB.txt");





    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //DONE  on item click
        ExternInfo selectedExtern = externInfoList.get(i);
        Intent se = new Intent(this,SingleExternTask.class);
        se.putExtra("selectedExtern",selectedExtern);
        startActivity(se);
    }

    private class listProcessor extends AsyncTask<String, Void, Integer> {

        ProgressDialog progressDialog;
        int delay = 500 ; // ms

        public listProcessor() {
            super();
        }

        // The onPreExecute is executed on the main UI thread before background processing is
        // started. In this method, we start the progressdialog.
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Show the progress dialog on the screen
            progressDialog = ProgressDialog.show(mContext, "Wait!","Displaying Extern List");
        }

        // This method is executed in the background and will return a result to onPostExecute
        // method. It receives the file name as input parameter.
        @Override
        protected Integer doInBackground(String... strings) {
            String infoString="";


            try {
                // Pretend downloading takes a long time
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Now we read the file, line by line and construct the
            // Json string from the information read in.
            try {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(openFileInput(strings[0])));
                String line;
                StringBuffer text = new StringBuffer();
                while ((line = bReader.readLine()) != null) {
                    text.append(line + "\n");
                }
                infoString = text.toString();

                processExternInfo p = new processExternInfo();
                externInfoList= p.convertJsonStringToObject(infoString);
                bReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return (Integer) 1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            mAdapter = new MyAdapter(mContext,externInfoList);
            externList.setAdapter(mAdapter);

            progressDialog.dismiss();
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();

            progressDialog.dismiss();
        }
    }
}
