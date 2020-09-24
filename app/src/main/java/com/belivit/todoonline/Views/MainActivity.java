package com.belivit.todoonline.Views;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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
import com.belivit.todoonline.Utils.ToastUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.internal.Util;

public class MainActivity extends AppCompatActivity {
    ImageButton addFabIv;
    RecyclerView allTaskRv;
    List<Task> taskList;
    TaskAdapter taskAdapter;
    Dialog mDialog;
    LinearLayout noInternetLL;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        String name = SharedPref.getShared(MainActivity.this, SharedPref.USER_NAME);
        userId = SharedPref.getShared(MainActivity.this, SharedPref.USER_ID);
        setTitle("{ " + name + " }" + " your Tasks");
        getAllTheTask(userId);

        addFabIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddTaskActivity.class));
            }
        });
    }

    void logout(){
        SharedPref.removeSharedAll(MainActivity.this);
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
    void refreshLayout(String userId){
        taskList.clear();
        getAllTheTask(userId);
    }

    private void getAllTheTask(String userId) {
        allTaskRv.setVisibility(View.VISIBLE);
        noInternetLL.setVisibility(View.GONE);
        loadingDialogeSHow();
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
                mDialog.dismiss();
                Gson gson = new Gson();
                AllTask allTask = gson.fromJson(response.toString(), AllTask.class);
                taskList.addAll(Arrays.asList(allTask.getAllTask()));
                taskAdapter = new TaskAdapter(taskList, MainActivity.this);
                allTaskRv.setAdapter(taskAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialog.dismiss();
                allTaskRv.setVisibility(View.GONE);
                noInternetLL.setVisibility(View.VISIBLE);
                Log.d("paul", "getAllTheTask: ErrorResponse:  " + error);

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    private void init() {
        addFabIv = findViewById(R.id.addFabIv);


        allTaskRv = findViewById(R.id.allTaskRv);
        allTaskRv.setLayoutManager(new LinearLayoutManager(this));
//        allTaskRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        taskList = new ArrayList<>();
        noInternetLL = findViewById(R.id.noInternetLL);
    }

    private void loadingDialogeSHow() {
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.alert_loading);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu_opiton, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.main_logout:
                logout();
                break;
            case R.id.main_refresh:
                refreshLayout(userId);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
