package com.example.kayit.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.kayit.R;
import com.example.kayit.classes.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class AdminKayit extends AppCompatActivity {

    EditText email,password,cnfPassword,name;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String email2,password2,cnfPassword2,name2;
    ProgressDialog pd;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_kayit);
        getSupportActionBar().setTitle("Öğretmen Kaydı");

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //firebase kullanıcı işlemleri initialize edildi
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        pd = new ProgressDialog(this);

        email = findViewById(R.id.kayıtYapEmail);
        password = findViewById(R.id.KayıtyapSifre);
        cnfPassword = findViewById(R.id.KayıtyapSifreTekrar);
        name = findViewById(R.id.kayıtYapName);
    }

    public void btnKayıtYap(View view){

        email2 = email.getText().toString();
        password2 = password.getText().toString();
        cnfPassword2 = cnfPassword.getText().toString();
        name2 = name.getText().toString();
        if(!(email2.equals("")) && !(password2.equals("")) && !(cnfPassword2.equals(""))){
            if(password2.equals(cnfPassword2)){
                //progress dialog çağırıldı
                pd.setMessage("Kayıt Olunuyor...");
                pd.show();

                //Real Time Database'e admin kayıdı
                Users user = new Users(name2,email2,password2);
                firebaseUser = auth.getCurrentUser();
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("Admins");
                databaseReference.push().setValue(user);

                //kullanıcı veri tabanına doğrudan kayıt
                auth.createUserWithEmailAndPassword(email2,password2).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //basarılı admin kaydı
                        totalPerson();
                        pd.dismiss();
                        Toast.makeText(AdminKayit.this, "Öğretmen Kaydı Oluşturuldu.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminKayit.this,Admin.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //hata olduğunda çalışacak kod bloğu
                        pd.dismiss();
                        Toast.makeText(AdminKayit.this, "Bir hata oluştu!", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                //hata olduğunda çalışacak kod bloğu
                Toast.makeText(this, "Şifreler Uyuşmuyor!", Toast.LENGTH_LONG).show();
            }
        }else{
            //Gerekli alanlar boş kaldığında çağırılan mesaj
            Toast.makeText(this, "Gerekli Alanları Doldurunuz!", Toast.LENGTH_LONG).show();
        }
    }

    private void totalPerson(){
        Users user = new Users(name2,email2,password2);
        firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("TotalPerson");
        databaseReference.push().setValue(user);
    }
}