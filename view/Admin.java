package com.example.kayit.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.kayit.R;
import com.example.kayit.faculty.UpdateFaculty;
import com.example.kayit.faculty.notice.DeleteNoticeActivity;
import com.example.kayit.faculty.notice.UploadNotice;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Admin extends AppCompatActivity implements View.OnClickListener {

    CardView uploadNotice,addGallery,addEbook,faculty, deleteNotice,uploadVideo;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //Firebase kullanıcı işlemleri initialize edildi
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        uploadNotice=findViewById(R.id.addNotice);
        addGallery=findViewById(R.id.addGallery);
        addEbook=findViewById(R.id.addEbook);
        deleteNotice=findViewById(R.id.deleteNotice);
        faculty=findViewById(R.id.faculty);

        uploadNotice.setOnClickListener(this);
        addGallery.setOnClickListener(this);
        addEbook.setOnClickListener(this);
        faculty.setOnClickListener(this);
        deleteNotice.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            //kullanıcı duyuru eklemek istediği zaman çağırılacak method

            case R.id.addNotice:
                intent = new Intent(Admin.this, UploadNotice.class);
                startActivity(intent);
                break;
            case R.id.addGallery:
                intent = new Intent(Admin.this, UploadImage.class);
                startActivity(intent);
                break;
            case R.id.addEbook:
                intent = new Intent(Admin.this, UploadPdf.class);
                startActivity(intent);
                break;
            case R.id.faculty:
                intent = new Intent(Admin.this, UpdateFaculty.class);
                startActivity(intent);
                break;
            case R.id.deleteNotice:
                intent = new Intent(Admin.this, DeleteNoticeActivity.class);
                startActivity(intent);
                break;
        }

    }

    //Admin hesabından kullanıcı hesabına giriş
    public void btnKullaniciBakisi(View view){
        startActivity(new Intent(Admin.this, MainActivity.class));
    }

    //oluşturulan menu initialize edildi
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_student,menu);
        return true;
    }


    //menude bulunan verilenlere erişmek için switch kullanıldı
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.navigation_add_student:
                //öğrenci ekleme sayfası
                startActivity(new Intent(Admin.this,AddStudentActivity.class));
                break;
            case R.id.navigation_add_teacher:
                //öğretmen ekleme sayfası
                startActivity(new Intent(Admin.this,AdminKayit.class));
                break;
            case R.id.nav_logout_admin:
                //Admin çıkış yapmak istediğinde
                auth.signOut();
                startActivity(new Intent(Admin.this,OgretmenGirisi.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}