package com.example.kush.tracker_final;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class home extends AppCompatActivity {
    private TextInputLayout inputLayoutmsg;
    TextView tv2;
    SharedPreferences sharedPreferences;
    String transmit;
    ImageView iv1,iv2,iv3,control;
    Animation a;
    EditText msg ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home);
        msg = (EditText)findViewById(R.id.message);

        RelativeLayout rv = (RelativeLayout)findViewById(R.id.rv);
        rv.setVisibility(View.INVISIBLE);
        sharedPreferences = this.getSharedPreferences("com.example.kush.tracker_final", Context.MODE_PRIVATE);
        transmit = sharedPreferences.getString("transmit","gjhchjchj");
        tv2 = (TextView)findViewById(R.id.textView2);
        tv2.setText("welcome "+sharedPreferences.getString("USER_NAME",""));
        TextView tv8 = (TextView)findViewById(R.id.textView8);
        tv8.setText(getDeviceName());
        a = AnimationUtils.loadAnimation(home.this,R.anim.images);
        inputLayoutmsg = (TextInputLayout) findViewById(R.id.input_layout_msg);
        msg.addTextChangedListener(new home.MyTextWatcher(msg));
        iv1 = (ImageView)findViewById(R.id.iv1);
        iv2 = (ImageView)findViewById(R.id.iv2);
        iv3 = (ImageView)findViewById(R.id.iv3);
        control = (ImageView) findViewById(R.id.control);

        iv1.setAlpha(0f);
        iv2.setAlpha(0f);
        iv3.setAlpha(0f);


        Log.i("res", "onCreate: "+transmit.toString());
        try {
            Intent intent = new Intent(getApplicationContext(), home.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext()
                    , 1, intent, 0);

            Notification note = new Notification.Builder(getApplicationContext())
                    .setContentTitle("NEVER LOSE")
                    .setContentText("transmitting in safe mode")
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setSmallIcon(android.R.drawable.sym_def_app_icon)
                    .build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(1, note);
        }catch (Exception e){
            e.printStackTrace();
        }

        startit();

    }

    public void startit(){
        if( !transmit.equals("true") )
        {

            sharedPreferences.edit().putString("transmit","true").apply();
            startService(new Intent(this,myservice.class));

        }
    }

    public void hs ( View v ){
        Intent i = new Intent(home.this,hs.class);
        startActivity(i);
    }
    int i=0;
    public void control(View v){

        if(iv1.getAlpha() == 0f) {
            control.animate().rotationBy(45f).start();
            iv3.animate().alpha(1f).setDuration(50).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    iv2.animate().alpha(1f).setDuration(20).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            iv1.animate().alpha(1f).setDuration(10).start();
                            super.onAnimationEnd(animation);
                        }
                    });
                    super.onAnimationEnd(animation);
                }
            });

        }else{
            control.animate().rotationBy(45f).start();
            iv3.setAlpha(0f);
            iv2.setAlpha(0f);
            iv1.setAlpha(0f);
        }

    }

    public void map(View v){
        Intent i = new Intent(home.this,MapsActivity.class);
        i.putExtra("secret", sharedPreferences.getString("secret",""));
        startActivity(i);
    }


    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public void save(View v){

        if(msg.getText().toString().contains(":")) {

            String type = msg.getText().toString().split(":")[0];
            String mssg = msg.getText().toString().split(":")[1];
            if (type.equals("play")) {
                var.play = mssg;
                Toast.makeText(this, "saved changes to play", Toast.LENGTH_SHORT).show();

                tv2.setVisibility(View.VISIBLE);
                Button hs = (Button) findViewById(R.id.hs);
                hs.setVisibility(View.VISIBLE);
                LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
                ll.setVisibility(View.VISIBLE);
                RelativeLayout rv = (RelativeLayout) findViewById(R.id.rv);
                rv.setVisibility(View.INVISIBLE);
                ImageView iv9 =(ImageView)findViewById(R.id.iv9);
                iv9.setVisibility(View.VISIBLE);

            } else if (type.equals("noplay")) {
                var.noplay = mssg;
                Toast.makeText(this, "saved changes to noplay", Toast.LENGTH_SHORT).show();

                tv2.setVisibility(View.VISIBLE);
                Button hs = (Button) findViewById(R.id.hs);
                hs.setVisibility(View.VISIBLE);
                LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
                ll.setVisibility(View.VISIBLE);
                RelativeLayout rv = (RelativeLayout) findViewById(R.id.rv);
                rv.setVisibility(View.INVISIBLE);
                ImageView iv9 =(ImageView)findViewById(R.id.iv9);
                iv9.setVisibility(View.VISIBLE);


            } else {
                inputLayoutmsg.setError("enter coded message properly");
                requestFocus(msg);
            }
        }else{
            inputLayoutmsg.setError("enter coded message properly");
            requestFocus(msg);
        }

    }

    private boolean validatemsg() {
        if (msg.getText().toString().trim().isEmpty()) {
            inputLayoutmsg.setError("enter coded message");
            requestFocus(msg);
            return false;
        } else {
            inputLayoutmsg.setError(null);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.msg:
                    validatemsg();
                    break;


            }
        }
    }


    public void cm(View v){

        tv2.setVisibility(View.INVISIBLE);
        ImageView iv9 =(ImageView)findViewById(R.id.iv9);
        iv9.setVisibility(View.INVISIBLE);
        Button hs = (Button)findViewById(R.id.hs);
        hs.setVisibility(View.INVISIBLE);
        LinearLayout ll = (LinearLayout)findViewById(R.id.ll);
        ll.setVisibility(View.INVISIBLE);
        RelativeLayout rv = (RelativeLayout)findViewById(R.id.rv);
        rv.setVisibility(View.VISIBLE);


    }

    public void settings(View v){

    }


}
