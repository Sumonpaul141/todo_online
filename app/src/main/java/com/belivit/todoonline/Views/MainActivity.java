package com.belivit.todoonline.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.belivit.todoonline.R;
import com.belivit.todoonline.Utils.SharedPref;

public class MainActivity extends AppCompatActivity {
    TextView nameEt;
    Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        String name = SharedPref.getShared(MainActivity.this, SharedPref.USER_NAME);
        String email = SharedPref.getShared(MainActivity.this, SharedPref.USER_EMAIL);
        nameEt.setText(email + "(" + name + ")");

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.removeSharedAll(MainActivity.this);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void init() {
        nameEt = findViewById(R.id.nameEt);
        logoutBtn = findViewById(R.id.logoutBtn);
    }
}
