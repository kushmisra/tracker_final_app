package com.example.kush.tracker_final;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class smd extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    TextView tv3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smd);
        sharedPreferences = this.getSharedPreferences("com.example.kush.tracker_final", Context.MODE_PRIVATE);
        tv3 = (TextView)findViewById(R.id.textView3);
        tv3.setText("welcome "+sharedPreferences.getString("USER_NAME",""));

    }


    public void tms(View v){
        Intent i = new Intent(smd.this,MapsActivity.class);
        i.putExtra("secret", sharedPreferences.getString("secret",""));
        startActivity(i);
    }

    public void msgb(View v){

        EditText msg = (EditText)findViewById(R.id.msg);
        Button msgb = (Button)findViewById(R.id.msgb);
        String type = msg.getText().toString().split(":")[0];
        String mssg = msg.getText().toString().split(":")[1];
        if(type.equals("play")){
            var.play = mssg;
            Toast.makeText(this, "saved changes to play", Toast.LENGTH_SHORT).show();
        }else if(type.equals("noplay")){
            var.noplay = mssg;
            Toast.makeText(this, "saved changes to noplay", Toast.LENGTH_SHORT).show();
        }
        msg.setAlpha(0);
        msgb.setAlpha(0);

    }


    public void ccm(View v){
        EditText msg = (EditText)findViewById(R.id.msg);
        Button msgb = (Button)findViewById(R.id.msgb);
        msg.setAlpha(1);
        msgb.setAlpha(1);
    }




}
