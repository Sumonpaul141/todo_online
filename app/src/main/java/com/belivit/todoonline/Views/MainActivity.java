package com.belivit.todoonline.Views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
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
import android.widget.TextView;

import com.android.volley.NetworkResponse;
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
import com.belivit.todoonline.Utils.RecyclerViewSwipeDecorator;
import com.belivit.todoonline.Utils.SharedPref;
import com.belivit.todoonline.Utils.ToastUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
    TextView mainErrorTv;
    SwipeRefreshLayout mainSwipeRef;
    boolean swiping;


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


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0 ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                Collections.swap(taskList, fromPosition, toPosition);
                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                int position = viewHolder.getAdapterPosition();
//                String taskIDForDelete = taskList.get(position).getTaskId();
//                taskDelete(taskIDForDelete, position);
            }


            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                final boolean draging = actionState == ItemTouchHelper.ACTION_STATE_DRAG;
                mainSwipeRef.setEnabled(!draging);
//                swiping = actionState == ItemTouchHelper.ACTION_STATE_SWIPE;
//                Log.d("paul", "onSelectedChanged: drag : " + swiping);


            }

//            @Override
//            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//                if (swiping){
//                    viewHolder.itemView.setBackgroundColor(Color.parseColor("#8C999C"));
//                }else {
//                    viewHolder.itemView.setBackgroundColor(Color.parseColor("#dee7e9"));
//                }
//                new RecyclerViewSwipeDecorator.Builder(c,recyclerView, viewHolder,dX, dY, actionState, isCurrentlyActive)
//                        .addSwipeLeftBackgroundColor(Color.parseColor("#8C999C"))
//                        .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
//                        .setSwipeLeftActionIconTint(R.color.colorWhite)
//                        .create()
//                        .decorate();
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//            }
        }).attachToRecyclerView(allTaskRv);


        mainSwipeRef.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout(userId);
                mainSwipeRef.setRefreshing(false);
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
                Collections.reverse(taskList);
                taskAdapter = new TaskAdapter(taskList, MainActivity.this);
                allTaskRv.setAdapter(taskAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialog.dismiss();
                allTaskRv.setVisibility(View.GONE);
                noInternetLL.setVisibility(View.VISIBLE);
                if (error.toString().toLowerCase().contains("noconnectionerror")){
                    mainErrorTv.setText("No internet! Check your internet connection.");
                }else if(error.toString().toLowerCase().contains("timeout")){
                    mainErrorTv.setText("Request timeout. Server not responding!");
                }else {
                    mainErrorTv.setText("Error in network. Please try again later.");
                }
                Log.d("paul", "getAllTheTask: ErrorResponse:  " + error);

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    private void init() {
        addFabIv = findViewById(R.id.addFabIv);
        mainErrorTv = findViewById(R.id.mainErrorTv);
        allTaskRv = findViewById(R.id.allTaskRv);
        allTaskRv.setLayoutManager(new LinearLayoutManager(this));
        taskList = new ArrayList<>();
        noInternetLL = findViewById(R.id.noInternetLL);
        mainSwipeRef = findViewById(R.id.mainSwipeRef);
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
        }

        return super.onOptionsItemSelected(item);
    }


    private void taskDelete(String taskId, final int position){
        String URL = GlobalData.getDeleteTaskUrl();
        loadingDialogeSHow();
        JSONObject params = new JSONObject();
        try {
            params.put("taskId", taskId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("paul", "taskDelete : Params: " + params);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("paul", "taskDelete: Response: " + response.toString());
                String deletedCount = "", n = "";
                mDialog.dismiss();
                try {
                    deletedCount = response.getString("deletedCount");
                    n = response.getString("n");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (deletedCount.equals("1") && n.equals("1")){
                    taskList.remove(position);
                    taskAdapter.notifyItemRemoved(position);

                }else {
                    ToastUtils.showToastOk(MainActivity.this, "Item Previously deleted");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialog.dismiss();
                ToastUtils.showToastError(MainActivity.this, "Cannot deleted. Try again");

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
