package com.example.kayit.faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.EditText;
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

public class AddTeachers extends AppCompatActivity {

    private ImageView addTeacherImage;
    private EditText addTeacherName, addTeacherEmail, addTeacherPost;
    private Spinner addTeacherCategory;
    private Button addTeacherBtn;
    private final int REQ = 1;
    private Bitmap bitmap = null;
    private String category;
    private String name,email,post,downloadUrl = "";
    private ProgressDialog pd;

    private StorageReference storageReference;
    private DatabaseReference reference,dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teachers);

        addTeacherImage = findViewById(R.id.addTeacherImage);
        addTeacherName = findViewById(R.id.addTeacherName);
        addTeacherEmail = findViewById(R.id.addTeacherEmail);
        addTeacherPost = findViewById(R.id.addTeacherPost);
        addTeacherCategory = findViewById(R.id.addTeacherCategory);
        addTeacherBtn = findViewById(R.id.addTeacherBtn);

        pd = new ProgressDialog(this);

        reference = FirebaseDatabase.getInstance().getReference().child("????retmen");
        storageReference = FirebaseStorage.getInstance().getReference();


        //kategori dizisi olu??turuldu
        String[] items = new String[]{"Kategori Se??","Bilgisayar Programc??l??????","Grafik Tasar??m","Fizyoterapi","Muhasebe"};
        addTeacherCategory.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items));
        addTeacherCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = addTeacherCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        addTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addTeacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkValidation();
            }
        });

    }

    private void checkValidation() {
        name = addTeacherName.getText().toString();
        email = addTeacherEmail.getText().toString();
        post = addTeacherPost.getText().toString();

        if (name.isEmpty()){
            addTeacherName.setError("Bo??");
            addTeacherName.requestFocus();
        }else if (email.isEmpty()){
            addTeacherEmail.setError("Bo??");
            addTeacherEmail.requestFocus();
        }else if (post.isEmpty()){
            addTeacherPost.setError("Bo??");
            addTeacherPost.requestFocus();
        }else if (category.equals("Kategori Se??")){
            Toast.makeText(this, "L??tfen ????retmen Kategorisi Ekleyin", Toast.LENGTH_SHORT).show();
        }else if (bitmap == null){
            pd.setMessage("Y??kleniyor...");
            pd.show();
            insertData();
        }else{
            pd.setMessage("Y??kleniyor...");
            pd.show();
            uploadImage();

        }
    }

    private void uploadImage() {
        //progres dialog ??a????r??ld??

        //veritaban??nda daha az yer kaplamas?? i??in


        //byte dizisi olu??turarak firebase'e transfer
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //resim bitmap t??r??ne ??evirildi
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);

        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;

        //resimler veritaban??na .jp olarak kay??t oldu
        filePath = storageReference.child("????retmenler").child(finalimg+".jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(AddTeachers.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                //Veri taban??na y??klenirse
                if(task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //basar??l?? bir ??ekilde veritaban??na eklenirse
                                    downloadUrl = String.valueOf(uri);
                                    insertData();

                                }
                            });
                        }
                    });
                }else{
                    //E??er y??kleme ba??ar??s??z olursa kullan??c?? bir mesaj al??r
                    Toast.makeText(AddTeachers.this, "Bir hata olu??tu!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void insertData() {
        //Teacher adl?? veritaban??na veri y??kleme fonksiyonu
        dbRef = reference.child(category);
        final String uniqueKey = dbRef.push().getKey();


        TeacherData teacherData = new TeacherData(name,email,post,downloadUrl,uniqueKey);

        dbRef.child(uniqueKey).setValue(teacherData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //y??klenince al??nacak mesaj
                pd.dismiss();
                Toast.makeText(AddTeachers.this, "Y??klendi.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //hata olursa al??nacak mesaj
                pd.dismiss();
                Toast.makeText(AddTeachers.this, "Bir hata olu??tu!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void openGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage, REQ);
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
            addTeacherImage.setImageBitmap(bitmap);
        }
    }



}