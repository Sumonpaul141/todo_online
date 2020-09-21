package com.belivit.todoonline.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.belivit.todoonline.Models.UserInfo;
import com.belivit.todoonline.R;
import com.belivit.todoonline.Utils.GlobalData;
import com.belivit.todoonline.Utils.SharedPref;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText loginEmailEt, loginPasswordEt;
    Button loginSignInBtn, loginRegistrationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        loginRegistrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        loginSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmailEt.getText().toString().trim();
                String password = loginPasswordEt.getText().toString();
                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Fill both the field ", Toast.LENGTH_SHORT).show();
                }else {
                    logInUser(email, password);
                }
            }
        });


    }

    private void init() {
        loginEmailEt = findViewById(R.id.loginEmailEt);
        loginPasswordEt = findViewById(R.id.loginPasswordEt);
        loginSignInBtn = findViewById(R.id.loginSignInBtn);
        loginRegistrationBtn = findViewById(R.id.loginRegistrationBtn);
    }


    private void logInUser(String email, String password) {
        String URL = GlobalData.getLoginUrl();

        JSONObject rewParams = new JSONObject();
        try {
            rewParams.put("email", email);
            rewParams.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("paul", "onResponse: CustomerDetails URL: " + rewParams);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, rewParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("paul", "onResponse: Response: " + response.toString());
                Gson  gson = new Gson();
                UserInfo info = gson.fromJson(response.toString(), UserInfo.class);
                if (info.getCode().equals("0")){
                    Toast.makeText(LoginActivity.this, "user doesn't exists, Register now", Toast.LENGTH_SHORT).show();
                }else {

                    SharedPref.saveShared(LoginActivity.this, SharedPref.USER_NAME, info.getName());
                    SharedPref.saveShared(LoginActivity.this, SharedPref.USER_EMAIL, info.getEmail());
                    SharedPref.saveShared(LoginActivity.this, SharedPref.USER_ID, info.getId());
                    SharedPref.saveSharedBoolean(LoginActivity.this, SharedPref.IS_LOGGED_IN, true);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("paul", "onResponse: Error:  " + error);

            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }
}
