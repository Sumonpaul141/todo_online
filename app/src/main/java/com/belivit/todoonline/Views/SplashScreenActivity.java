package com.belivit.todoonline.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.belivit.todoonline.R;
import com.belivit.todoonline.Utils.SharedPref;


public class SplashScreenActivity extends AppCompatActivity {
    boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isLogin = SharedPref.getIsLoggedIn(SplashScreenActivity.this, SharedPref.IS_LOGGED_IN);
                if (isLogin) {
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);

    }
}
