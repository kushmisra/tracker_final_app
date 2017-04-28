package com.example.kush.tracker_final;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class hs extends AppCompatActivity {

    EditText un;
    EditText pw;
    Button fu;
    String user="",pass="";
    TextInputLayout layout_un,layout_pw;
//    String secret =" ";
    public static String secret = "";
    boolean proceed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_hs);

        un = (EditText)findViewById(R.id.un);
        pw = (EditText)findViewById(R.id.pw);
        fu = (Button)findViewById(R.id.fu);


        layout_pw = (TextInputLayout)findViewById(R.id.layout_pw);
        layout_un = (TextInputLayout)findViewById(R.id.layout_un);

        un.addTextChangedListener(new hs.MyTextWatcher(un));
        pw.addTextChangedListener(new hs.MyTextWatcher(pw));

    }



    private boolean validateName() {
        if (un.getText().toString().trim().isEmpty()) {
            layout_un.setError("enter username");
            requestFocus(un);
            return false;
        } else {
            layout_un.setError(null);
        }

        return true;
    }



    private boolean validatePassword() {
        if (pw.getText().toString().trim().isEmpty()) {
            layout_pw.setError("please enter password");
            requestFocus(pw);
            return false;
        } else {
            layout_pw.setError(null);
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
                case R.id.un:
                    validateName();
                    break;

                case R.id.pw:
                    validatePassword();
                    break;

            }
        }
    }




    public class task extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {


            Map<String,String> m = new HashMap<>();
            m.put("name",user);
            m.put("password",pass);
            JSONObject jsonObject = new JSONObject(m);
            String url;
            url = Network.BASE_URL_STRING + "getsecret";
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
                        secret = Jo.getString("secret");
                        var.num = Long.valueOf(Jo.getString("mob"));
                        proceed = true;
                    }else{

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(proceed){
                proceed = false;
                Intent i = new Intent(hs.this,ha.class);
                startActivity(i);
            }else{
                Toast.makeText(hs.this, "not a valid user", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(aVoid);
        }
    }

    public void fu(View v){
        user = un.getText().toString();
        pass = pw.getText().toString();
        new task().execute();

    }


}
