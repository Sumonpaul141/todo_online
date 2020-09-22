package com.belivit.todoonline.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
                Gson gson = new Gson();
                AllTodo allTodo = gson.fromJson(response.toString(), AllTodo.class);
                todoList.addAll(Arrays.asList(allTodo.getAllTodo()));
                todoAdapter = new TodoAdapter(todoList, DetailsTaskActivity.this, DetailsTaskActivity.this);
                todoRv.setAdapter(todoAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("paul", "addTodoToDb:  ResponseError:  " + error);

            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void addTodoToDb(String todoTitle) {
        String URL = GlobalData.getAddTodoUrl();

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
                String code = "";
                try {
                    code = response.getString("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code.equals("1")){
                    todoList.clear();
                    seeAllTodos(taskId);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        todoRv.setHasFixedSize(true);
        todoRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        todoList = new ArrayList<>();
    }

    @Override
    public void onTodoChecked(final boolean isDone, final Todo todo, final int position) {
        String URL = GlobalData.getCheckTodoUrl();

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
                int code = 0;
                try {
                    code = response.getInt("nModified");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("paul ", "onResponse: code type " + code);
                if (code == 1){
                    Todo newTodo = new Todo(todo.getTodoId(), todo.getTodoTitle(), isDone);
                    todoList.set(position, newTodo);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("paul", "onTodoChecked:  ResponseError:  " + error);

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onTodoDelete(String todoId, final int position) {
        String URL = GlobalData.getDeleteTodoUrl();

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
                String code = "", n = "";
                try {
                    code = response.getString("deletedCount");
                    n = response.getString("n");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("paul", "onResponse: code :  " + code.equals("1") + "   n   " + n.equals("1"));
                if (code.equals("1") && n.equals("1")){
                    Log.d("paul", "onResponse: Deleted done ");
                    todoAdapter.delete(position);

                }else {
                    ToastUtils.showToastError(DetailsTaskActivity.this, "Item Previously deleted");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtils.showToastError(DetailsTaskActivity.this, "Cannot deleted. Try again");

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }
}
