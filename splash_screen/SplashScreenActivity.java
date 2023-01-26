package com.example.kayit.splash_screen;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.kayit.R;
import com.example.kayit.view.MainActivity;
import com.example.kayit.view.OgretmenGirisi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //app bar kaldırıldı
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        //thread obje oluşturuldu
        Thread background = new Thread() {
            public void run() {
                try {
                    // 3 saniye thread bekler
                    sleep(3*1000);

                    // 3 saniye sonra intent gerçekleşir

                    if(firebaseUser != null){
                        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                        finish();
                    }else{
                        startActivity(new Intent(SplashScreenActivity.this,OgretmenGirisi.class));
                        finish();
                    }

                    //Aktivite sonlanması
                    finish();
                } catch (Exception e) {
                }
            }
        };
        // thread başlangıcı
        background.start();
    }
}