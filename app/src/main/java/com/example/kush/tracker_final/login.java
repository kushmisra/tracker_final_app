package com.example.kush.tracker_final;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class login extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText password;
    EditText username;
    EditText email;
    EditText mob;
    TextView login;
    Button signup;
    String user = "";
    String pass = "";
    String mobi = " ";
    String em = " ";
    boolean islogin = false;
    boolean confirm = false;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword,inputLayoutmob;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);
        sharedPreferences = this.getSharedPreferences("com.example.kush.tracker_final", Context.MODE_PRIVATE);
        password = (EditText)findViewById(R.id.password);
        username = (EditText)findViewById(R.id.username);
        email =(EditText)findViewById(R.id.email);
        login = (TextView)findViewById(R.id.login);
        signup = (Button)findViewById(R.id.signup);
        mob = (EditText)findViewById(R.id.mob);
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputLayoutmob = (TextInputLayout) findViewById(R.id.input_layout_mob);


        username.addTextChangedListener(new MyTextWatcher(username));
        email.addTextChangedListener(new MyTextWatcher(email));
        password.addTextChangedListener(new MyTextWatcher(password));
        mob.addTextChangedListener(new MyTextWatcher(mob));


    }



    private boolean validateName() {
        if (username.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError("enter username");
            requestFocus(username);
            return false;
        } else {
            inputLayoutName.setError(null);
            return true;

        }


    }

    private boolean validateEmail() {
        String iemail = email.getText().toString().trim();

        if (iemail.isEmpty() || !isValidEmail(iemail)) {
            inputLayoutEmail.setError("enter correct email");
            requestFocus(email);
            return false;
        } else {
            inputLayoutEmail.setError(null);
        }

        return true;
    }

    private boolean validatePassword() {
        if (password.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError("please enter password");
            requestFocus(password);
            return false;
        } else {
            inputLayoutPassword.setError(null);
        }

        return true;
    }

    private boolean validateMob() {
        if (mob.getText().toString().trim().length()!= 10) {
            inputLayoutmob.setError("please enter valid number");
            requestFocus(mob);
            return false;
        } else {
            inputLayoutmob.setError(null);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
                case R.id.username:
                    validateName();
                    break;
                case R.id.email:
                    validateEmail();
                    break;
                case R.id.password:
                    validatePassword();
                    break;
                case R.id.mob:
                    validateMob();
                    break;
            }
        }
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


    public class task extends AsyncTask<Void,Void,Void>{
        String message= "";
        @Override
        protected Void doInBackground(Void... params) {

            Map<String,String> m = new HashMap<>();
            m.put("name",user);
            m.put("password",pass);
            Log.i("in", "doInBackground: llklklklkl");
            String url;
            if(islogin) {
                url = Network.BASE_URL_STRING + "checklogin";
            }else{
                url = Network.BASE_URL_STRING + "create";
                m.put("mob",mobi);
                m.put("email",em);
                m.put("make",getDeviceName());
            }
            JSONObject jsonObject = new JSONObject(m);
            Response res = Network.postRequest(getApplicationContext(),jsonObject.toString(),url);
            if(res == null){
                Log.i("res", "doInBackground: recieved null ");
            }else{
                Log.i("res", "doInBackground: "+res.toString());
                ResponseBody jsonData = res.body();
                try {
                    JSONObject Jobject = new JSONObject(jsonData.string());
                    String status = Jobject.getString("status");
                    if(status.equals("200")){
                        JSONObject Jo = Jobject.getJSONObject("data");
                        user = Jo.getString("USER_NAME");
                        sharedPreferences.edit().putString("secret",Jo.getString("USER_SECRET")).apply();
                        sharedPreferences.edit().putString("USER_NAME",user).apply();
                        confirm = true;

                    }else{
                        confirm = false;
                        message = Jobject.getString("message");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

//            Toast.makeText(login.this, "confirm"+confirm+message, Toast.LENGTH_SHORT).show();
            if(confirm
                && email.length()>0 && mob.length()>0 && username.length()>0 && password.length()>0){
                Intent i = new Intent(login.this,home.class);
                startActivity(i);
            }
            super.onPostExecute(aVoid);
        }
    }

    public void login (View v){
//
        Intent i = new Intent(login.this,signup.class);
        startActivity(i);
        finish();
    }

    public void signup(View v){
//
        if (validateEmail() && validateMob() && validateName() && validatePassword()) {
            user = username.getText().toString();
            pass = password.getText().toString();
            mobi = mob.getText().toString();
            em = email.getText().toString();

            if (mobi.length() > 0 && em.length() > 0 && user.length() > 0 && pass.length() > 0) {
//            Toast.makeText(this, "in the condition", Toast.LENGTH_SHORT).show();
                new task().execute();
            }
        }

    }



}
