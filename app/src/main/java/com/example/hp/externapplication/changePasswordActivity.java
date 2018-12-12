package com.example.hp.externapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class changePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String MyPREFERENCES = "MyPrefs" ;

    private TextInputLayout oldPassword;
    private TextInputLayout textEmailCP;
    private TextInputLayout newPassword;
    private TextInputLayout confirmPassword;
    private Button changeButton ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Password");

        oldPassword = findViewById(R.id.oldPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        textEmailCP = findViewById(R.id.textEmailCP);

        changeButton = findViewById(R.id.changeButton);
        changeButton.setOnClickListener(this);


    }


    boolean validateEmail() {
        String emailInput = textEmailCP.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textEmailCP.setError("Field can't be empty");
            return false;
        } else {
            textEmailCP.setError(null);
            return true;
        }
    }

    boolean validateOldPassword() {
        String passwordInput = oldPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            oldPassword.setError("Field can't be empty");
            return false;
        } //DONE add code to check old password equals new password

        else {
            oldPassword.setError(null);
            return true;
        }
    }

    boolean validateNewPassword() {
        String passwordInput = newPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            newPassword.setError("Field can't be empty");
            return false;
        }
        else {
            newPassword.setError(null);
            return true;
        }
    }

    boolean validateConfirmPassword() {
        String passwordInput = confirmPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            confirmPassword.setError("Field can't be empty");
            return false;
        }
        else if(!passwordInput.equals(newPassword.getEditText().getText().toString().trim())){
            confirmPassword.setError("Password does not match");
            return false;
        }
        else {
            confirmPassword.setError(null);
            return true;
        }
    }


    @Override
    public void onClick(View view) {


        switch (view.getId()){
            case R.id.changeButton:
                validateConfirmPassword();
                validateNewPassword();
                validateOldPassword();
                validateEmail();
                if(!validateOldPassword() || !validateNewPassword() || !validateConfirmPassword() ||!validateEmail()){
                    return ;
                }
                SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                String prevPassword = sharedpreferences.getString(textEmailCP.getEditText().getText().toString().trim()," ");
                if(prevPassword.equals(" ")){
                    textEmailCP.setError("Incorrect Email");
                    return;
                }
                else if(!prevPassword.equals( oldPassword.getEditText().getText().toString().trim())){
                    oldPassword.setError("Incorrect Password");
                    return;
                }
                else{
                    editor.putString(textEmailCP.getEditText().getText().toString().trim(),newPassword.getEditText().getText().toString().trim());
                    editor.commit();
                    Toast.makeText(this,"Password Changed",Toast.LENGTH_LONG).show();
                    super.onBackPressed();
                }
                    break;

                default:
                    break;


        }
    }
}
