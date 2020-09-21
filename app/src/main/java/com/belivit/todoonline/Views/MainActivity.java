package com.belivit.todoonline.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.belivit.todoonline.Adapters.TaskAdapter;
import com.belivit.todoonline.Models.AllTask;
import com.belivit.todoonline.Models.Task;
import com.belivit.todoonline.R;
import com.belivit.todoonline.Utils.GlobalData;
import com.belivit.todoonline.Utils.SharedPref;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button logoutBtn;
    ImageButton addFabIv;
    RecyclerView allTaskRv;
    List<Task> taskList;
    TaskAdapter taskAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        String name = SharedPref.getShared(MainActivity.this, SharedPref.USER_NAME);
        String userId = SharedPref.getShared(MainActivity.this, SharedPref.USER_ID);
        setTitle("{ " + name + " }" + " your Tasks");
        getAllTheTask(userId);


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.removeSharedAll(MainActivity.this);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
        addFabIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddTaskActivity.class));
            }
        });
    }

    private void getAllTheTask(String userId) {
        String URL = GlobalData.getAllTasknUrl();

        JSONObject params = new JSONObject();
        try {
            params.put("userId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("paul", "getAllTheTask : Params: " + params);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("paul", "getAllTheTask: Response: " + response.toString());
                Gson gson = new Gson();
                AllTask allTask = gson.fromJson(response.toString(), AllTask.class);
                taskList.addAll(Arrays.asList(allTask.getAllTask()));
                taskAdapter = new TaskAdapter(taskList, MainActivity.this);
                allTaskRv.setAdapter(taskAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("paul", "getAllTheTask: ErrorResponse:  " + error);

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    private void init() {
        logoutBtn = findViewById(R.id.logoutBtn);
        addFabIv = findViewById(R.id.addFabIv);


        allTaskRv = findViewById(R.id.allTaskRv);
        allTaskRv.setLayoutManager(new LinearLayoutManager(this));
        taskList = new ArrayList<>();
    }
}
