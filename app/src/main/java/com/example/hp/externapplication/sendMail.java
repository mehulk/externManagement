package com.example.hp.externapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

public class sendMail extends AsyncTask<registerActivity.UserInfo,Void,Void> {
    private Context context;
    private ProgressDialog progressDialog;

    public sendMail(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context,"Creating account","Please Wait....");
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        Toast.makeText(context,"Registered. Continue to login!",Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(registerActivity.UserInfo... userInfos) {

        try{
            Thread.sleep(800);

        }catch(InterruptedException e){
            e.printStackTrace();
        }

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailTo:"));
        emailIntent.setType("message/rfc882");
        String[] recepient = {userInfos[0].email,"admin@SopraSteria.com","mehulkanotra98@gmail.com"};
        emailIntent.putExtra(Intent.EXTRA_EMAIL  , recepient);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "New Extern");
        emailIntent.putExtra(Intent.EXTRA_TEXT   , userInfos[0].emailBody);
        Intent chooser = Intent.createChooser(emailIntent,"Send Mail");
        context.startActivity(chooser);


        return null;

    }
}
