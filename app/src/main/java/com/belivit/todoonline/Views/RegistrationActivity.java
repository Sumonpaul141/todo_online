package com.belivit.todoonline.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.belivit.todoonline.Utils.ToastUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationActivity extends AppCompatActivity {
    EditText regConfirmPasswordEt, regPasswordEt, regNameEt, regEmailEt;
    Button regCreateAccountBtn, regSignInBtn;
    Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();

        regCreateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = regNameEt.getText().toString().trim();
                String email = regEmailEt.getText().toString().trim();
                String password = regPasswordEt.getText().toString();
                String confirmPassword = regConfirmPasswordEt.getText().toString();

                if(name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                    Toast.makeText(RegistrationActivity.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                }else {
                    if (confirmPassword.equals(password)){
                        registerUser(email, password, name);
                    }else {
                        Toast.makeText(RegistrationActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        regSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

    }

    private void loadingDialogeSHow() {
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.alert_loading);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }

    private void registerUser(String email, String password, String name) {
        String URL = GlobalData.getRegistrationUrl();
        loadingDialogeSHow();
        JSONObject rewParams = new JSONObject();
        try {
            rewParams.put("email", email);
            rewParams.put("password", password);
            rewParams.put("name", name);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("paul", "onResponse: CustomerDetails URL: " + rewParams);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, rewParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("paul", "onResponse: Response: " + response.toString());
                Gson gson = new Gson();
                mDialog.dismiss();
                UserInfo info = gson.fromJson(response.toString(), UserInfo.class);
                if (info.getCode().equals("1")){
                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                    finish();
                }else {
                    Toast.makeText(RegistrationActivity.this, info.getMassage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialog.dismiss();
                Log.d("paul", "onResponse: Error:  " + error);
                if (error.toString().toLowerCase().contains("noconnectionerror")){
                    ToastUtils.showToastError(RegistrationActivity.this,"No internet! Check your internet connection.");
                }else if(error.toString().toLowerCase().contains("timeout")){
                    ToastUtils.showToastError(RegistrationActivity.this,"Request timeout. Server not responding!");
                }else {
                    ToastUtils.showToastError(RegistrationActivity.this,"Error in network. Please try again later.");
                }

            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }


    private void init() {
        regEmailEt = findViewById(R.id.regEmailEt);
        regNameEt = findViewById(R.id.regNameEt);
        regPasswordEt = findViewById(R.id.regPasswordEt);
        regConfirmPasswordEt = findViewById(R.id.regConfirmPasswordEt);
        regCreateAccountBtn = findViewById(R.id.regCreateAccountBtn);
        regSignInBtn = findViewById(R.id.regSignInBtn);
    }
}
