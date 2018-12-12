package com.example.hp.externapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SingleExternTask extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView externList;
    externTaskAdapter eta;
    String m_Text="";
    Context context;
    ExternInfo ei ;
    ArrayList<Task> task;
    ArrayList<ExternInfo> externInfoList;
    Task newTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_extern_task);

        Intent in = getIntent();
        ei = (ExternInfo)in.getSerializableExtra("selectedExtern");


        externList = findViewById(R.id.externTaskToDo);
        context=this;

        TextView externName = findViewById(R.id.externName);
        externName.setText(ei.Name);

        TextView externEmail = findViewById(R.id.externEmail);
        externEmail.setText(ei.Email);

        TextView externMobile = findViewById(R.id.externMobile);
        externMobile.setText(ei.Mobile);

        TextView externLocation = findViewById(R.id.externLocation);
        externLocation.setText(ei.Location);

        task=ei.tasks;

        externList.setOnItemClickListener(this);
        eta = new externTaskAdapter(this,task);
        externList.setAdapter(eta);

        //DONE set eta as adapter and pass data
        //DONE add arrayadapter of task


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Add New Task");
                final EditText input = new EditText(context);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        m_Text = input.getText().toString();
                        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                        newTask = new Task(m_Text,date);
                        m_Text="";
                        //DONE add new Task to arraylist of task and show update
                        task.add(newTask);
                        eta.notifyDataSetChanged();
                        //TODO write the updated arraylist back to file
                        listProcessor2 lp = new listProcessor2();
                        lp.execute("infoDB.txt");



                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

    }


    public void writeToFile(String json){

        try {
            FileOutputStream fos = openFileOutput("infoDB.txt", Context.MODE_PRIVATE);
            fos.write(json.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //DONE set on item listner to check boxes
        final int pos = i;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Task Done!");
        builder.setMessage("Is The Task Done?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                task.get(pos).taskDone();
                eta.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Task Completed", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "No Change", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

    private class listProcessor2 extends AsyncTask<String, Void, Integer> {

        ProgressDialog progressDialog;
        int delay = 500 ; // ms

        public listProcessor2() {
            super();
        }

        // The onPreExecute is executed on the main UI thread before background processing is
        // started. In this method, we start the progressdialog.
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Show the progress dialog on the screen
            progressDialog = ProgressDialog.show(context, "Wait!","Updating");
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

            for(int i=0;i<externInfoList.size();i++){
                if(ei.Email.equals(externInfoList.get(i))){
                    externInfoList.get(i).tasks.add(newTask);
                    break;
                }
            }
            processExternInfo p = new processExternInfo();
            String json = p.convertObjectToJson(externInfoList);
            writeToFile(json);
            return (Integer) 1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();

            progressDialog.dismiss();
        }
    }
}
