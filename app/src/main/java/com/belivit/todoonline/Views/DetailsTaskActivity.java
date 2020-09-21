package com.belivit.todoonline.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.belivit.todoonline.R;
import com.belivit.todoonline.Utils.ToastUtils;

public class DetailsTaskActivity extends AppCompatActivity {
    TextView detailsTaskDescriptionTv, detailsTaskTitleTV;
    String title= "", description ="", taskId = "";
    EditText todoEt;
    ImageButton addTodoIb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_task);
        init();
        if (getIntent().getStringExtra("title")  != null && getIntent().getStringExtra("description") != null){
            title = getIntent().getStringExtra("title");
            description = getIntent().getStringExtra("description");
            taskId = getIntent().getStringExtra("taskId");
        }
        detailsTaskDescriptionTv.setText(description);
        detailsTaskTitleTV.setText(title);
        addTodoIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todo = todoEt.getText().toString().trim();
                addTodoToDb(todo);
            }
        });

    }

    private void addTodoToDb(String todo) {
        
    }

    private void init() {
        detailsTaskDescriptionTv = findViewById(R.id.detailsTaskDescriptionTv);
        detailsTaskTitleTV = findViewById(R.id.detailsTaskTitleTV);
        todoEt = findViewById(R.id.todoEt);
        addTodoIb = findViewById(R.id.addTodoIb);
    }
}
