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

public class OgrenciGirisi extends AppCompatActivity {

    EditText editEmail,editSifre;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    String email,sifre;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ogrenci_girisi);

        //firebase kullanıcı işlemleri için initialize edildi
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        pd = new ProgressDialog(this);

        //eğer kullanıcı mevcut ise tekrardan giriş yapılması engellendi
        if(firebaseUser != null){
            Intent intent = new Intent(OgrenciGirisi.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        editEmail = findViewById(R.id.ogrenciGirisYapEmail);
        editSifre = findViewById(R.id.ogrenciGirisyapSifre);
    }

    public void btnOgrenciGirisiYap(View view){
        email = editEmail.getText().toString();
        sifre = editSifre.getText().toString();

        //email ve şifre boş değil ise
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(sifre)) {
            //progress dialog cagirildi
            pd.setMessage("Giriş yapılıyor...");
            pd.show();

            //sigIn method kullanıldı, kullanıcı her girdiğinde
            //giriş yapmaması için
            auth.signInWithEmailAndPassword(email,sifre).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    //boş bırakabiliriz, real time database kullanıyoruz.
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

            //Real time database'den veri okuma
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Students");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds : snapshot.getChildren()){
                        if(ds.child("email").getValue().equals(email)){
                            final String getPassword = (String) ds.child("password").getValue();
                            if(getPassword.equals(sifre)){
                                //şifre ve email uyuşursa kullanıcı girişi olur
                                Intent intent = new Intent(OgrenciGirisi.this,MainActivity.class);
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

    public void ogretmenGirisSayfası(View view){
        //öğretmen girişi sayfasına geçmek için kullanıldı
        startActivity(new Intent(OgrenciGirisi.this,OgretmenGirisi.class));
    }

    public void forgotPasswordOgrenci(View view){
        //şifre unutuldu textine tıklanınca çağırılacak fonksiyon
        showRecoverPasswordDialog();
    }

    ProgressDialog loadingBar;

    private void showRecoverPasswordDialog() {
        //Alertdialog oluşturuldu, kullanıcı mesaj alması için
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Şifre sıfırla");
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
                String new_email=emailet.getText().toString().trim();
                beginRecovery(new_email);
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
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingBar.dismiss();
                if(task.isSuccessful())
                {
                    //şifre değiştirme maili gönderilirse
                    Toast.makeText(OgrenciGirisi.this,"Gönderildi.",Toast.LENGTH_LONG).show();
                }
                else {
                    //hata oluştuğunda alınacak mesaj
                    Toast.makeText(OgrenciGirisi.this,"Bir hata oluştu!",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //hata oluştuğunda alınacak mesaj
                loadingBar.dismiss();
                Toast.makeText(OgrenciGirisi.this,"Bir hata oluştu!",Toast.LENGTH_LONG).show();
            }
        });
    }
}