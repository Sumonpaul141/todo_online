package com.belivit.todoonline.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.belivit.todoonline.R;

public class DetailsTaskActivity extends AppCompatActivity {
    TextView detailsTaskDescriptionTv, detailsTaskTitleTV;
    String title= "", description ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_task);
        init();
        if (getIntent().getStringExtra("title")  != null && getIntent().getStringExtra("description") != null){
            title = getIntent().getStringExtra("title");
            description = getIntent().getStringExtra("description");
        }
        detailsTaskDescriptionTv.setText(description);
        detailsTaskTitleTV.setText(title);


    }

    private void init() {
        detailsTaskDescriptionTv = findViewById(R.id.detailsTaskDescriptionTv);
        detailsTaskTitleTV = findViewById(R.id.detailsTaskTitleTV);
    }
}
