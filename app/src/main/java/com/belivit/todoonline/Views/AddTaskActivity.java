package com.belivit.todoonline.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.belivit.todoonline.R;
import com.belivit.todoonline.Utils.GlobalData;
import com.belivit.todoonline.Utils.SharedPref;
import com.belivit.todoonline.Utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class AddTaskActivity extends AppCompatActivity {
    EditText addtitleEt, addDescEt;
    Button addTaskBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        setTitle("Create new task");
        init();
        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = addtitleEt.getText().toString().trim();
                String desc = addDescEt.getText().toString().trim();
                String userId = SharedPref.getShared(AddTaskActivity.this, SharedPref.USER_ID);

                if (title.equals("") || desc.equals("")){
                    if (title.equals("")){
                        addtitleEt.setError("This field cannot be empty");
                    }
                    if (desc.equals("")){
                        addDescEt.setError("This field cannot be empty");
                    }
                }else {
                    addtitleEt.setText("");
                    addDescEt.setText("");
                    saveTaskToDB(title, desc, userId);
                }

            }
        });
    }

    private void saveTaskToDB(String title, String desc, String userId) {

        String URL = GlobalData.getAddTasknUrl();

        JSONObject params = new JSONObject();
        try {
            params.put("taskTitle", title);
            params.put("taskDescription", desc);
            params.put("userId", userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("paul", "saveTaskToDB : Params: " + params);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("paul", "saveTaskToDB: Response: " + response.toString());
                String code = "";
                try {
                    code = response.getString("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code.equals("1")){
                    startActivity(new Intent(AddTaskActivity.this, MainActivity.class));
                } else {
                    ToastUtils.showToastError(AddTaskActivity.this, "Task created failed, try again later");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("paul", "saveTaskToDB:  ResponseError:  " + error);

            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);



    }


    private void init() {
        addtitleEt = findViewById(R.id.addTitleEt);
        addDescEt = findViewById(R.id.addDescEt);
        addTaskBtn = findViewById(R.id.addTaskBtn);
    }
}
