package com.example.kayit.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.kayit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OgretmenGirisi extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private LinearLayout mLinear;
    private FirebaseUser firebaseUser;
    private EditText editEmail,editSifre;
    private String txtEmail,txtSifre;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ogretmen_girisi);

        pd = new ProgressDialog(this);

        init();
    }

    private void init(){
        //firebase kullanıcı işlemleri initialize edilir
        mLinear=(LinearLayout)findViewById(R.id.ogretmenLinear);
        mAuth= FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        //kullanıcı girişi yapılmış ise kontrol edilir
        if(firebaseUser != null){
            Intent intent = new Intent(OgretmenGirisi.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }


        editEmail = findViewById(R.id.GirisYapEmail);
        editSifre = findViewById(R.id.GirisyapSifre);
    }

    public void btnGirisYap(View view){
        //Nesnelerimizi çağırdık

        txtEmail = editEmail.getText().toString();
        txtSifre = editSifre.getText().toString();

        //şifre ve email boş değilse
        if(!TextUtils.isEmpty(txtEmail) && !TextUtils.isEmpty(txtSifre)) {
            pd.setMessage("Giriş yapılıyor...");
            pd.show();

            //signIn methodu ile giriş yapıldı
            mAuth.signInWithEmailAndPassword(txtEmail,txtSifre).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    //Boş bırakabiliriz, sadece sigIn yaptık
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

            //Admin veritabanından veri çekildi
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Admins");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds : snapshot.getChildren()){
                        if(ds.child("email").getValue().equals(txtEmail)){
                            final String getPassword = (String) ds.child("password").getValue();
                            if(getPassword.equals(txtSifre)){
                                //şifre ve email doğru olursa admin girişi olacak
                                Intent intent = new Intent(OgretmenGirisi.this,Admin.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    //öğrenci giriş sayfasına gidilen method
    public void ogrenciGirisSayfasi(View view){
        startActivity(new Intent(OgretmenGirisi.this,OgrenciGirisi.class));
    }

    //şifre unutuldu textine tıklanınca çağırılacak fonksyion
    public void forgotPasswordTeacher(View view){
        showRecoverPasswordDialog();
    }

    ProgressDialog loadingBar;

    private void showRecoverPasswordDialog() {
        //Alertdialog oluşturuldu, kullanıcı mesaj alması için
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Şifre Sıfırla");
        LinearLayout linearLayout=new LinearLayout(this);
        final EditText emailet= new EditText(this);

        // kayıt olunan email adresi yazılır ve sıfırla butonuna basılır
        emailet.setHint("Email adresi");
        emailet.setMinEms(16);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailet);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);

        //sıfırla tusuna basılınca mail gönderilecek
        builder.setPositiveButton("Sıfırla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email=emailet.getText().toString().trim();
                beginRecovery(email);
            }
        });

        //iptal edilmek istenirse
        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void beginRecovery(String email) {
        //emailin firebase aracılığıyla kullanıcıya gönderildiği fonksyion
        loadingBar=new ProgressDialog(this);
        loadingBar.setMessage("Email gönderiliyor...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        //kullanıcıya tekrardan şifre atılır
        //firebase üzerinden yeni şifresi sıfırlanır
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingBar.dismiss();
                if(task.isSuccessful())
                {
                    //şifre değiştirme maili gönderilirse
                    Toast.makeText(OgretmenGirisi.this,"Gönderildi.",Toast.LENGTH_LONG).show();
                }
                else {
                    //hata oluştuğunda alınacak mesaj
                    Toast.makeText(OgretmenGirisi.this,"Bir hata oluştu!",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //hata oluştuğunda alınacak mesaj
                loadingBar.dismiss();
                Toast.makeText(OgretmenGirisi.this,"Bir hata oluştu!",Toast.LENGTH_LONG).show();
            }
        });
    }
}