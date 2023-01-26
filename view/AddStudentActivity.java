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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddStudentActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    EditText email,password,cnfPassword,name;
    String email2,password2,cnfPassword2,name2;
    private ProgressDialog pd;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Öğrenci Kaydı");

        //firebase giriş işlemleri initialize edildi
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        pd = new ProgressDialog(this);

        email = findViewById(R.id.ogrencikayıtYapEmail);
        password = findViewById(R.id.ogrenciKayıtyapSifre);
        cnfPassword = findViewById(R.id.ogrenciKayıtyapSifreTekrar);
        name = findViewById(R.id.ogrenciKayıtYapName);
    }

    public void btnOgrenciKayitYap(View view){
        email2 = email.getText().toString();
        password2 = password.getText().toString();
        cnfPassword2 = cnfPassword.getText().toString();
        name2 = name.getText().toString();

        //Eğer şifre ve email boş değilse
        if(!(email2.equals("")) && !(password2.equals("")) && !(cnfPassword2.equals(""))){
            //her iki şifrede birbirine eşit ise
            if(password2.equals(cnfPassword2)){
                //progress dialog çağırıldı
                pd.setMessage("Öğrenci kaydı yapılıyor...");
                pd.show();

                //Users sınıfından kullanıcı oluşturuldu
                Users user = new Users(name2,email2,password2);

                //Oluşturulan kullanıcılar real time database'e kayıt edildi
                firebaseUser = auth.getCurrentUser();
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("Students");
                databaseReference.push().setValue(user);

                //sigIn ve sigOut kullanmak için kullanıcı veri deposuna kayıt edildi.
                auth.createUserWithEmailAndPassword(email2,password2).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        pd.dismiss();
                        totalPerson();
                        Toast.makeText(AddStudentActivity.this, "Öğrenci Kaydı Yapıldı.", Toast.LENGTH_LONG).show();
                        email.setText("");
                        password.setText("");
                        cnfPassword.setText("");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //hata olursa kullanıcıya mesaj
                        pd.dismiss();
                        Toast.makeText(AddStudentActivity.this, "Bir hata oluştu!", Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                //hata olursa kullanıcıya mesaj
                Toast.makeText(this, "Şifreler Uyuşmuyor!", Toast.LENGTH_LONG).show();
            }
        }else{
            //Alanlar boş ise kullanıcıya mesaj
            Toast.makeText(this, "Gerekli alanları doldurunuz!", Toast.LENGTH_LONG).show();
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