package com.example.hp.externapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toolbar;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class registerActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String MyPREFERENCES = "MyPrefs" ;


    TextInputLayout textEmail;
    TextInputLayout textMobile;
    Button buttonRegister;
    TextInputLayout textFirstName;
    TextInputLayout textLastName ;
    TextInputLayout textCreatePassword;
    Spinner genderAns;
    Spinner locationAns;
    Context mContext ;
    processExternInfo p = new processExternInfo();

    public class UserInfo {
        String FName;
        String LName;
        String email;
        String gender;
        String mobile;
        String location;
        String emailBody;
        String textCreatePassword;
        public UserInfo(String FName, String LName, String email, String gender, String mobile, String location , String textCreatePassword){
            this.FName=FName;
            this.LName=LName;
            this.email=email;
            this.gender=gender;
            this.mobile=mobile;
            this.location=location;
            this.textCreatePassword = textCreatePassword;
            emailBody = "Name :"+ FName +" "+ LName+"\nEmail :"+ email+"\nPassword :"+textCreatePassword+"\nMobile Number :"+mobile+"\n"+gender+"\nLocation :"+location;

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        textEmail = findViewById(R.id.textEmail);
        textMobile = findViewById(R.id.textMobile);
        buttonRegister = findViewById(R.id.buttonRegister);
        textFirstName = findViewById(R.id.textFirstName);
        textLastName = findViewById(R.id.textLastName);
        textCreatePassword = findViewById(R.id.textCreatePassword);
        mContext=this;

        // dropdown gender
        genderAns = findViewById(R.id.genderAns);
        String[] itemsGen = new String[]{"Male", "Female"};
        ArrayAdapter<String> adapterGen = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsGen);
        genderAns.setAdapter(adapterGen);

        //dropdown location
        locationAns = findViewById(R.id.locationAns);
        String[] itemsLocat = new String[]{"Noida", "Delhi","Greater Noida"};
        ArrayAdapter<String> adapterLocat = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsLocat);
        locationAns.setAdapter(adapterLocat);

        //toolbar
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");

        //onClickSubmit
        buttonRegister.setOnClickListener(this);



    }

    //validate functions
    private boolean validateEmail() {
        String emailInput = textEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textEmail.setError("Field can't be empty");
            return false;
        } else {
            textEmail.setError(null);
            return true;
        }
    }

    private boolean validateFirstName() {
        String firstNameInput = textFirstName.getEditText().getText().toString().trim();

        if (firstNameInput.isEmpty()) {
            textFirstName.setError("Field can't be empty");
            return false;
        } else {
            textFirstName.setError(null);
            return true;
        }
    }

    private boolean validateLastName() {
        String lastNameInput = textLastName.getEditText().getText().toString().trim();

        if (lastNameInput.isEmpty()) {
            textLastName.setError("Field can't be empty");
            return false;
        } else {
            textLastName.setError(null);
            return true;
        }
    }

    private boolean validateMobile() {
        String mobileInput = textMobile.getEditText().getText().toString().trim();

        if (mobileInput.isEmpty()) {
            textMobile.setError("Field can't be empty");
            return false;
        }else if(mobileInput.length()<10){
            textMobile.setError("Invalid Number");
            return false;
        }
        else {
            textMobile.setError(null);
            return true;
        }
    }

    boolean validatePassword() {
        String passwordInput = textCreatePassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textCreatePassword.setError("Field can't be empty");
            return false;
        }else if(passwordInput.length()<8){
            textCreatePassword.setError("Password cannot be less than 8 characters");
            return false;
        }
        else {
            textCreatePassword.setError(null);
            return true;
        }

    }

    @Override
    public void onClick(View view) {
        validateEmail();
        validateFirstName();
        validateLastName();
        validateMobile();
        validatePassword();


        switch (view.getId()){
            case R.id.buttonRegister:
                if(!validateEmail() || !validateFirstName() || !validateLastName() || !validateMobile() || !validatePassword()){
                    return;
                }
                String emailInput = textEmail.getEditText().getText().toString().trim();
                String firstNameInput = textFirstName.getEditText().getText().toString().trim();
                String lastNameInput = textLastName.getEditText().getText().toString().trim();
                String mobileInput = textMobile.getEditText().getText().toString().trim();
                String gender = genderAns.getSelectedItem().toString();
                String location = locationAns.getSelectedItem().toString();
                String createPassword = textCreatePassword.getEditText().getText().toString().trim();
                //DONE register user in shared preferences and add password

                UserInfo newUser = new UserInfo(firstNameInput,lastNameInput,emailInput,gender,mobileInput,location,createPassword);
                sendMail mail = new sendMail(this);
                mail.execute(newUser);
                ExternInfo newExtern = new ExternInfo(firstNameInput+" "+lastNameInput,emailInput,createPassword,location,gender,mobileInput);
                Task t = new Task("Create Account", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
                t.taskDone();
                newExtern.addTask(t);
                SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(emailInput,createPassword);
                editor.commit();

                //Read the file

                String json="";
                try {
                    FileOutputStream fos = openFileOutput("infoDB.txt", Context.MODE_APPEND);
                    fos.write(json.toString().getBytes());
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ArrayList<ExternInfo> ei=null;

                String info = readFromFile();

                    //code for initialisation
                    if(info.length()==0){
                        ei  =  new ArrayList<>();
                        ei.add(newExtern);
                        json = p.convertObjectToJson(ei);
                    }
                    else{
                        ei = p.convertJsonStringToObject(info);
                        ei.add(newExtern);
                        json = p.convertObjectToJson(ei);
                    }

                    writeToFile(json);



                super.onBackPressed();
        }



    }


    public String readFromFile(){
        String infoString="";

        try {
            BufferedReader bReader = new BufferedReader(new InputStreamReader(openFileInput("infoDB.txt")));
            String line;
            StringBuffer text = new StringBuffer();
            while ((line = bReader.readLine()) != null) {
                text.append(line + "\n");
            }
            infoString = text.toString();
            bReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return infoString;

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
}
