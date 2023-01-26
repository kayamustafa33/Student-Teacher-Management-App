package com.example.kayit.view;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.lang.ref.Reference;

public class UploadImage extends AppCompatActivity {

    private static final int REQ = 1;
    private Spinner imageCategory;
    private CardView selectImages;
    private Button uploadImage;
    private ImageView galleryImageView;
    private String catagory;
    private Bitmap bitmap;
    ProgressDialog pd;

    private DatabaseReference reference;
    private StorageReference storageReference;
    String downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        getSupportActionBar().setTitle("Resim Yükle");

        //elemanlar initialize edildi
        selectImages = findViewById(R.id.addGalleryImage);
        imageCategory = findViewById(R.id.image_category);
        uploadImage = findViewById(R.id.uploadImageBtn);
        galleryImageView = findViewById(R.id.galleryImageView);

        //veritabanı initialize edildi
        reference = FirebaseDatabase.getInstance().getReference().child("gallery");
        storageReference = FirebaseStorage.getInstance().getReference().child("gallery");

        pd = new ProgressDialog(this);

        //elemanlar dizisi oluşturuldu
        String[] items = new String[]{"Select Category","Toplantı","Tatil Günü","Ders","Genel"};
        imageCategory.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items));
        imageCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                catagory = imageCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //
        selectImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }});

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap == null){
                    Toast.makeText(UploadImage.this, "Lütfen resim yükleyin",Toast.LENGTH_LONG).show();
                }else if (catagory.equals("Kategori Seçiniz")){
                    Toast.makeText(UploadImage.this,"Resmin Kategorisini Seçiniz",Toast.LENGTH_LONG).show();
                }else{
                    pd.setMessage("Yükleniyor...");
                    pd.show();
                    uploadImage();
                }
            }
        });
    }


    private void uploadImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //resim bitmap türüne çevirildi
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);

        byte[] finalImg = baos.toByteArray();
        final StorageReference filePath;

        //resimler veritabanına .jp olarak kayıt oldu
        filePath = storageReference.child(finalImg+".jpg");
        final UploadTask uploadTask = filePath.putBytes(finalImg);
        uploadTask.addOnCompleteListener(UploadImage.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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

                                        }
                                    });
                                }
                            });
                        }else{
                            //Eğer yükleme başarısız olursa kullanıcı bir mesaj alır
                            Toast.makeText(UploadImage.this, "Bir hata oluştu!", Toast.LENGTH_LONG).show();
                        }
                    }});
    }

    private void uploadData() {
        reference = reference.child(catagory);
        final String uniqueKey = reference.push().getKey();

        reference.child(uniqueKey).setValue(downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();

                Toast.makeText(UploadImage.this, "Resim Başarıyla Yüklendi", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadImage.this,"Bir Problem var",Toast.LENGTH_LONG).show();
            }
        });
    }


    //galeri açılma fonksiyonu
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
            galleryImageView.setImageBitmap(bitmap);
        }
    }

}