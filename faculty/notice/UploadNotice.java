package com.example.kayit.faculty.notice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kayit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UploadNotice extends AppCompatActivity {

    private CardView addImage;
    private ImageView noticeImageView;
    private EditText noticeTitle;
    private Button uploadBtnNotice;

    private final int REQ=1;
    private Bitmap bitmap;
    private DatabaseReference reference,dbRef;
    private StorageReference storageReference;
    String downloadUrl = "";
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notice);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Not Yükle");

        //firebase veritabanı initialize edildi
        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);

        addImage = findViewById(R.id.addImage);
        noticeImageView = findViewById(R.id.noticeImageView);
        noticeTitle = findViewById(R.id.noticeTitle);
        uploadBtnNotice = findViewById(R.id.uploadBtnNotice);

        uploadBtnNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        //resim ekle butonuna basılınca çağırılacak method
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }

    private void uploadImage() {
        //progres dialog çağırıldı
        pd.setMessage("Yükleniyor...");
        pd.show();
        //veritabanında daha az yer kaplaması için


        //byte dizisi oluşturarak firebase'e transfer
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //resim bitmap türüne çevirildi
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);

        byte[] finalImg = baos.toByteArray();
        final StorageReference filePath;

        //resimler veritabanına .jp olarak kayıt oldu
        filePath = storageReference.child("Notice").child(finalImg+".jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImg);
        uploadTask.addOnCompleteListener(UploadNotice.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                //Veri tabanına yüklenirse
                if(task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //basarılı bir şekilde veritabanına eklenirse
                                    downloadUrl = String.valueOf(uri);
                                    uploadData();
                                    noticeTitle.setText("");
                                }
                            });
                        }
                    });
                }else{
                    //Eğer yükleme başarısız olursa kullanıcı bir mesaj alır
                    Toast.makeText(UploadNotice.this, "Bir hata oluştu!", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(UploadNotice.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                noticeTitle.setText("");
                //Eğer yükleme başarısız olursa kullanıcı bir mesaj alır
                Toast.makeText(UploadNotice.this, "Bir hata oluştu!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void uploadData() {
        //Notice adlı veritabanına veri yükleme fonksiyonu
        dbRef = reference.child("Notice");
        final String uniqueKey = dbRef.push().getKey();
        String title = noticeTitle.getText().toString();


        //Veritabanında yüklenen resmin tarihini alabilmek için bu methodu kullandık
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentData = new SimpleDateFormat("dd-MM-yy");
        String date = currentData.format(calForDate.getTime());

        //Veritabanında yüklenen resmin saatini alabilmek için bu methodu kullandık
        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(calForDate.getTime());

        NoticeData noticeData = new NoticeData(title,downloadUrl,date,time,uniqueKey);

        dbRef.child(uniqueKey).setValue(noticeData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //yüklenince alınacak mesaj
                pd.dismiss();
                Toast.makeText(UploadNotice.this, "Yüklendi.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //hata olursa alınacak mesaj
                pd.dismiss();
                Toast.makeText(UploadNotice.this, "Bir hata oluştu!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //galeriye intent methodu
    private void openGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage,REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==REQ && resultCode == RESULT_OK){
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            noticeImageView.setImageBitmap(bitmap);
        }
    }

}