package com.example.kush.tracker_final;

import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class settings extends AppCompatActivity {

    ArrayList<String> text;
    ArrayList<Integer>images1;
    ListView LV;
    CustomListAdapter customListAdapter;
    String pass ="";

    public void clicked( View v )
    {
        int position = Integer.parseInt(v.getTag().toString());

        String text1 = text.get(position);

        Ab(text1,position);
    }

    public void check(String s,String p,int i){

        if(s.equals(pass)){

            switch (i){
                case 1:
                    Toast.makeText(settings.this, "password chnaged", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(settings.this, "username chnaged", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(settings.this, "email chnaged", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(settings.this, "mobile chnaged", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(settings.this, "emergancy number", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    Toast.makeText(settings.this, "emergancy name", Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    Toast.makeText(settings.this, "sim slot", Toast.LENGTH_SHORT).show();
                    break;
                case 8:
                    Toast.makeText(settings.this, "time period", Toast.LENGTH_SHORT).show();
                    break;

            }

        }else{
            Toast.makeText(settings.this, "Wrong password", Toast.LENGTH_SHORT).show();
        }

    }

    public void Ab (String s, final int i){

        final AlertDialog.Builder builder = new AlertDialog.Builder(settings.this);
        final View addView=getLayoutInflater().inflate(R.layout.settingsdialogue, null);

        ImageView imgv = (ImageView)addView.findViewById(R.id.imgv);
        imgv.setImageResource(images1.get(i));

        TextView tv = (TextView)addView.findViewById(R.id.tv);
        tv.setText( s );

        builder.setView(addView);

        builder.setPositiveButton("change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                EditText e =(EditText)addView.findViewById(R.id.username);
                EditText e2 =(EditText)addView.findViewById(R.id.password);
                String s = e.getText().toString();
                String p = e2.getText().toString();
                check(s,p,i);
            }

        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = builder.create();
        try {
            dialog.show();
        }catch(Exception e){
            e.printStackTrace();
        }


//        return  "l";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

                requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_settings);

        text = new ArrayList<>();
        images1 = new ArrayList<>();

        text.add("Change password");
        images1.add(R.drawable.pass);

        text.add("Change username");
        images1.add(R.drawable.user);

        text.add("Change email");
        images1.add(R.drawable.email);

        text.add("Change mobile");
        images1.add(R.drawable.mob);

        text.add("Add emergency number");
        images1.add(R.drawable.emergancy);

        text.add("Add emergancy name");
        images1.add(R.drawable.emergancy);

        text.add("preferred sim if dual");
        images1.add(R.drawable.simcard);

        text.add("timeperiod of transmission");
        images1.add(R.drawable.hourclock);



        LV = (ListView) findViewById(R.id.lv);
        customListAdapter=new CustomListAdapter(this,text,images1);
        LV.setAdapter(customListAdapter);

    }
}
