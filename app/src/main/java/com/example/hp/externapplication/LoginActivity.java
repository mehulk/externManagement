package com.example.hp.externapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;

    //For admins
    private static final String adminUsername1="admin1@soprasteria.com";
    private static final String adminPassword1 ="password";
    private static final String adminUsername2="admin2@soprasteria.com";
    private static final String adminPassword2 ="password";


    private TextInputLayout textEmail;
    private TextInputLayout textPassword;
    private Button loginButton;
    private TextView textNewUser;
    private CheckBox adminBox ;
    private TextView changePassword;
    Context mContext;
    ArrayList<ExternInfo> externInfoList;
    ExternInfo singleExtern ;
    String emailInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        mContext=this;

        textEmail = findViewById(R.id.textEmail);
        textPassword = findViewById(R.id.textPassword);
        loginButton = findViewById(R.id.loginButton);
        adminBox = findViewById(R.id.adminBox);
        loginButton.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");

        textNewUser = findViewById(R.id.textnewUser);
        textNewUser.setOnClickListener(this);

        changePassword = findViewById(R.id.changePassword);
        changePassword.setOnClickListener(this);


        if(!sharedPreferences.contains(adminUsername1)){
            editor.putString(adminUsername1,adminPassword1);
            editor.putString(adminUsername2,adminPassword2);
            String is1 = "is"+adminUsername1;
            String is2 = "is"+adminUsername2;
            editor.putBoolean(is1,true);
            editor.putBoolean(is2,true);
            editor.commit();
        }

    }

     boolean validateEmail() {
        String emailInput = textEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textEmail.setError("Field can't be empty");
            return false;
        } else {
            textEmail.setError(null);
            return true;
        }
    }

    boolean validatePassword() {
        String passwordInput = textPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textPassword.setError("Field can't be empty");
            return false;
        } else {
            textPassword.setError(null);
            return true;
        }

    }

    @Override
    public void onClick(View view) {
        //validatePassword();
        //validateEmail();
        emailInput = textEmail.getEditText().getText().toString().trim();
        String passwordInput = textPassword.getEditText().getText().toString();

        switch (view.getId()){
            case R.id.textnewUser:
                Intent in = new Intent(this,registerActivity.class);
                startActivity(in);
                break;
            case R.id.changePassword:
                Intent cp = new Intent(this,changePasswordActivity.class);
                startActivity(cp);
                break;

            case R.id.loginButton:
                validatePassword();
                validateEmail();
                if(!validateEmail() || !validatePassword() ){
                    return;
                }
                if(adminBox.isChecked()){
                    // code for admin login
                    String is = "is"+emailInput;
                    if(sharedPreferences.getBoolean(is,false)!=true){
                        Toast.makeText(this,"YOU ARE NOT A ADMIN!",Toast.LENGTH_LONG).show();
                        return;
                    }
                    else{
                        String pw = sharedPreferences.getString(emailInput," ");
                        if(!passwordInput.equals(pw)){
                            textPassword.setError("Incorrect Password");
                            Toast.makeText(this,pw,Toast.LENGTH_LONG).show();
                            return;
                        }
                        else{
                            Intent sa = new Intent(this,AdminPageActivity.class);
                            sa.putExtra("email",emailInput);
                            startActivity(sa);
                        }
                    }
                }
                else{
                    //DONE code for extern login
                    String pw = sharedPreferences.getString(emailInput," ");
                    if(pw.equals(" ")){
                        textPassword.setError("Incorrect Email");
                        return;
                    }
                    else if(!pw.equals(passwordInput)){
                        textPassword.setError("Incorrect Password");
                        return;
                    }
                    else{
                        Intent se = new Intent(this,SingleExternTask.class);

                        try {
                            BufferedReader bReader = new BufferedReader(new InputStreamReader(openFileInput("infoDB.txt")));
                            String line;
                            StringBuffer text = new StringBuffer();
                            while ((line = bReader.readLine()) != null) {
                                text.append(line + "\n");
                            }
                            String infoString = text.toString();

                            processExternInfo p = new processExternInfo();
                            externInfoList= p.convertJsonStringToObject(infoString);
                            bReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

//                        listProcessor lp = new listProcessor();
//                        lp.execute("infoDB.txt");

                        for(int i=0;i<externInfoList.size();i++){
                            if(externInfoList.get(i).Email.equals(emailInput)){
                                singleExtern = externInfoList.get(i);
                                break;
                            }
                        }
                        if(singleExtern==null){
                            Toast.makeText(mContext,"Error",Toast.LENGTH_LONG).show();
                            return;
                        }else{
                            se.putExtra("selectedExtern",singleExtern);

                        }

                        startActivity(se);
                    }
                }

                break;
            default:
                break;
        }

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
            progressDialog = ProgressDialog.show(mContext, "Wait!","Logging You In!");
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

            progressDialog.dismiss();
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
            progressDialog.dismiss();
        }
    }
}
