package com.belivit.todoonline.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.belivit.todoonline.Adapters.TodoAdapter;
import com.belivit.todoonline.Interface.TodoCheckEvent;
import com.belivit.todoonline.Models.AllTodo;
import com.belivit.todoonline.Models.Todo;
import com.belivit.todoonline.R;
import com.belivit.todoonline.Utils.GlobalData;
import com.belivit.todoonline.Utils.ToastUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailsTaskActivity extends AppCompatActivity implements TodoCheckEvent {
    TextView detailsTaskDescriptionTv, detailsTaskTitleTV;
    String title= "", description ="", taskId = "";
    EditText todoEt;
    Button addTodoIb;

    List<Todo> todoList;
    RecyclerView todoRv;
    TodoAdapter todoAdapter;

    LinearLayout linearLayout;
    Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_task);
        setTitle("Add todo to your task");
        init();
        if (getIntent().getStringExtra("title")  != null && getIntent().getStringExtra("description") != null){
            title = getIntent().getStringExtra("title");
            description = getIntent().getStringExtra("description");
            taskId = getIntent().getStringExtra("taskId");
        }
        seeAllTodos(taskId);

        detailsTaskDescriptionTv.setText(description);
        detailsTaskTitleTV.setText(title);
        addTodoIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoTitle = todoEt.getText().toString().trim();
                addTodoToDb(todoTitle);
                todoEt.setText("");
            }
        });

    }

    private void seeAllTodos(String taskId) {
        String URL = GlobalData.getAllTodoUrl();
        loadingDialogeSHow();
        JSONObject params = new JSONObject();
        try {
            params.put("taskId", taskId);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("paul", "addTodoToDb : Params: " + params);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("paul", "addTodoToDb: Response: " + response.toString());
                mDialog.dismiss();
                Gson gson = new Gson();
                AllTodo allTodo = gson.fromJson(response.toString(), AllTodo.class);
                todoList.addAll(Arrays.asList(allTodo.getAllTodo()));
                todoAdapter = new TodoAdapter(todoList, DetailsTaskActivity.this, DetailsTaskActivity.this);
                todoRv.setAdapter(todoAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialog.dismiss();
                if (error.toString().toLowerCase().contains("connectionerror")){
                    ToastUtils.showToastError(DetailsTaskActivity.this,"No internet! Check your internet connection.");
                }else if(error.toString().toLowerCase().contains("timeout")){
                    ToastUtils.showToastError(DetailsTaskActivity.this,"Request timeout. Server not responding!");
                }else {
                    ToastUtils.showToastError(DetailsTaskActivity.this,"Error in network. Please try again later.");
                }
                Log.d("paul", "addTodoToDb:  ResponseError:  " + error);

            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void addTodoToDb(String todoTitle) {
        String URL = GlobalData.getAddTodoUrl();

        loadingDialogeSHow();
        JSONObject params = new JSONObject();
        try {
            params.put("todoTitle", todoTitle);
            params.put("taskId", taskId);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("paul", "addTodoToDb : Params: " + params);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("paul", "addTodoToDb: Response: " + response.toString());
                mDialog.dismiss();
                String code = "", todoTitle = "", todoId = "";
                try {
                    code = response.getString("code");
                    todoTitle = response.getString("todoTitle");
                    todoId = response.getString("todoId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code.equals("1")){
                    Todo newTodo = new Todo(todoId, todoTitle, false);
                    todoList.add(newTodo);
                    todoAdapter.notifyDataSetChanged();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialog.dismiss();
                ToastUtils.showToastError(DetailsTaskActivity.this, "Connection Error, Try again");
                Log.d("paul", "addTodoToDb:  ResponseError:  " + error);

            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void init() {
        detailsTaskDescriptionTv = findViewById(R.id.detailsTaskDescriptionTv);
        detailsTaskTitleTV = findViewById(R.id.detailsTaskTitleTV);
        todoEt = findViewById(R.id.todoEt);
        addTodoIb = findViewById(R.id.addTodoIb);

        todoRv = findViewById(R.id.todoRv);
//        todoRv.setHasFixedSize(true);
        todoRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        todoList = new ArrayList<>();

        linearLayout = findViewById(R.id.parentLL);
    }

    @Override
    public void onTodoChecked(final boolean isDone, final Todo todo, final int position) {
        String URL = GlobalData.getCheckTodoUrl();

        loadingDialogeSHow();
        JSONObject params = new JSONObject();
        try {
            params.put("todoID", todo.getTodoId());
            params.put("isDone", isDone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("paul", "onTodoChecked : Params: " + params);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("paul", "onTodoChecked: Response: " + response.toString());
                mDialog.dismiss();
                int code = 0;
                try {
                    code = response.getInt("nModified");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code == 1){
                    Todo newTodo = new Todo(todo.getTodoId(), todo.getTodoTitle(), isDone);
                    todoList.set(position, newTodo);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialog.dismiss();
                Log.d("paul", "onTodoChecked:  ResponseError:  " + error);
                ToastUtils.showToastError(DetailsTaskActivity.this, "Cannot check item without internet. Try again");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onTodoDelete(String todoId, final int position) {
        String URL = GlobalData.getDeleteTodoUrl();
        loadingDialogeSHow();

        JSONObject params = new JSONObject();
        try {
            params.put("todoID", todoId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("paul", "onTodoDelete : Params: " + params);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("paul", "onTodoDelete: Response: " + response.toString());
                mDialog.dismiss();
                String code = "", n = "";
                try {
                    code = response.getString("deletedCount");
                    n = response.getString("n");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code.equals("1") && n.equals("1")){
                    Log.d("paul", "onResponse: Deleted done " + position);
                    todoAdapter.delete(position);
                    ToastUtils.showToastOk(DetailsTaskActivity.this, "Deleted!");
                }else {
                    ToastUtils.showToastError(DetailsTaskActivity.this, "Item Previously deleted");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialog.dismiss();
                ToastUtils.showToastError(DetailsTaskActivity.this, "Cannot delete. Try again");

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    @Override
    public void onTodoUpdate(Todo todo, int position) {
        editTodo(todo, position);
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
        inflater.inflate(R.menu.task_menu_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_edit:
                editTask(taskId);
                break;
            case R.id.menu_delete:
                deleteTaskAlert();
                break;
            case R.id.menu_archive:
                archiveTask(taskId);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void archiveTask(String taskId) {
        ToastUtils.showToastOk(DetailsTaskActivity.this, "This task will be archived");
    }


    private void deleteTaskAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(DetailsTaskActivity.this);
        dialog.setTitle("Delete");
        dialog.setMessage("Are you sure you want to delete this whole task? That can't be undone anymore.");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                taskDelete(taskId);
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("Ow No!!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void editTask(final String taskId) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alert_edit);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        final EditText updateTitleEt = dialog.findViewById(R.id.updateTitleEt);
        final EditText updateDescEt = dialog.findViewById(R.id.updateDescEt);
        final TextView updateTopicTV = dialog.findViewById(R.id.updateTopicTV);
        updateDescEt.setText(detailsTaskDescriptionTv.getText());
        updateTitleEt.setText(detailsTaskTitleTV.getText());
        updateTopicTV.setText("Edit task :");
        Button cancelButton = dialog.findViewById(R.id.cencelButton);
        Button updateButton = dialog.findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String utitle, udesc;
                utitle = updateTitleEt.getText().toString().trim();
                udesc = updateDescEt.getText().toString().trim();
                if (utitle.isEmpty() || udesc.isEmpty()){
                    ToastUtils.showToastError(DetailsTaskActivity.this, "Fill both fields");
                }else {
                    updateTaskApiCall(taskId,utitle, udesc, dialog);
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    private void editTodo(final Todo todo, final int position) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alert_edit);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        final EditText updateTitleEt = dialog.findViewById(R.id.updateTitleEt);
        final EditText updateDescEt = dialog.findViewById(R.id.updateDescEt);
        final TextView updateTopicTV = dialog.findViewById(R.id.updateTopicTV);
        updateTitleEt.setText(todo.getTodoTitle());
        updateDescEt.setVisibility(View.GONE);
        updateTopicTV.setText("Edit todo :");
        Button cancelButton = dialog.findViewById(R.id.cencelButton);
        Button updateButton = dialog.findViewById(R.id.updateButton);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String utitle;
                utitle = updateTitleEt.getText().toString().trim();
                if (utitle.isEmpty()){
                    ToastUtils.showToastError(DetailsTaskActivity.this, "Can't be empty");
                }else {
                    updateTodoApiCall(todo.getTodoId(), utitle, dialog, position);
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void updateTaskApiCall(String taskId, final String utitle, final String udesc, final Dialog dialog) {
        String URL = GlobalData.getUpdateTaskUrl();
        loadingDialogeSHow();
        JSONObject params = new JSONObject();
        try {
            params.put("taskId", taskId);
            params.put("title", utitle);
            params.put("description", udesc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("paul", "updateTaskApiCall : Params: " + params);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("paul", "updateTaskApiCall: Response: " + response.toString());
                mDialog.dismiss();
                String nModified = "", n = "";
                try {
                    nModified = response.getString("nModified");
                    n = response.getString("n");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (nModified.equals("1") && n.equals("1")){
                    dialog.dismiss();
                    ToastUtils.showToastOk(DetailsTaskActivity.this, "Task updated");
                    detailsTaskTitleTV.setText(utitle);
                    detailsTaskDescriptionTv.setText(udesc);
                }else {
                    dialog.dismiss();
                    ToastUtils.showToastOk(DetailsTaskActivity.this, "Nothing updated");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialog.dismiss();
                ToastUtils.showToastError(DetailsTaskActivity.this, "Cannot update. \n Check internet connection");

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


    private void updateTodoApiCall(String todoId, final String utitle, final Dialog dialog, final int position) {
        String URL = GlobalData.getUpdateTodoUrl();
        loadingDialogeSHow();
        JSONObject params = new JSONObject();
        try {
            params.put("todoId", todoId);
            params.put("todoTitle", utitle);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("paul", "updateTodoApiCall : Params: " + params);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("paul", "updateTodoApiCall: Response: " + response.toString());
                mDialog.dismiss();
                String nModified = "", n = "";
                try {
                    nModified = response.getString("nModified");
                    n = response.getString("n");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (nModified.equals("1") && n.equals("1")){
                    dialog.dismiss();
                    ToastUtils.showToastOk(DetailsTaskActivity.this, "Todo updated");
                    todoAdapter.update(position, utitle);
                }else {
                    dialog.dismiss();
                    ToastUtils.showToastOk(DetailsTaskActivity.this, "Nothing updated");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialog.dismiss();
                ToastUtils.showToastError(DetailsTaskActivity.this, "Cannot update. \n Check internet connection");

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


    private void taskDelete(String taskId){
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
                mDialog.dismiss();
                String deletedCount = "", n = "";
                try {
                    deletedCount = response.getString("deletedCount");
                    n = response.getString("n");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (deletedCount.equals("1") && n.equals("1")){
                    startActivity(new Intent(DetailsTaskActivity.this, MainActivity.class));
                    finish();

                }else {
                    ToastUtils.showToastOk(DetailsTaskActivity.this, "Item Previously deleted");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialog.dismiss();
                ToastUtils.showToastError(DetailsTaskActivity.this, "Cannot deleted. Try again");

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
