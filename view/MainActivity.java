package com.example.kayit.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.kayit.R;
import com.example.kayit.ebook.EbookActivity;
import com.example.kayit.message.MessageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private NavController navController;

    DrawerLayout drawerLayout,devDrawer;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    View dialogView;

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private ImageSlider imageSlider;
    private ImageView map;
    int number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this,R.id.frame_layout);

        NavigationUI.setupWithNavController(bottomNavigationView,navController);

        //drawer layour initialize edildi
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_ebook:
                        drawerLayout.close();
                        startActivity(new Intent(MainActivity.this,EbookActivity.class));
                        break;
                    case R.id.nav_message:
                        drawerLayout.close();
                        Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_themes:
                        if(number%2 == 0){
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            number++;
                        }else if(number%2 == 1){
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            number++;
                        }
                        drawerLayout.close();
                        break;
                    case R.id.nav_website:
                        String url = "https://www.arel.edu.tr/";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        drawerLayout.close();
                        break;
                    case R.id.nav_share:
                        Toast.makeText(MainActivity.this, "Uygulama Play Store'da olmadığı için paylaşamazsın.", Toast.LENGTH_SHORT).show();
                        drawerLayout.close();
                        break;
                    case R.id.nav_rate:
                        Toast.makeText(MainActivity.this, "Teşekkürler.", Toast.LENGTH_SHORT).show();
                        drawerLayout.close();
                        break;
                    case R.id.nav_developers:
                        developerLayout();
                        drawerLayout.close();
                        break;
                }
                return false;
            }
        });

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        imageSlider = findViewById(R.id.imageSlider);

        //arrayList initialize edildi
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        //slide resimleri eklendi
        slideModels.add(new SlideModel("https://img.piri.net/mnresize/840/-/resim/imagecrop/2021/10/08/04/08/resized_3f478-12642c851myt.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://gazeteistanbul.com/wp-content/uploads/2018/07/ana-g%C3%B6rsel.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://i4.hurimg.com/i/hurriyet/75/438x246/623ad7344e3fe0173c169318.jpg", ScaleTypes.FIT));

        imageSlider.setImageList(slideModels,ScaleTypes.FIT);

        //harita initialize edildi
        map = findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });

    }

    //google map resime tıklanınca çağırılacak method
    private void openMap() {
        Uri uri = Uri.parse("geo:41.05571347786993, 28.500222257463427,0?q=Arel Üniversitesi");
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    public void checkThePerson(){

        //Admin veri tabanından referans alındı
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Admins");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int flag = 0;
                for(DataSnapshot ds : snapshot.getChildren()){
                    if(ds.child("email").getValue().equals(firebaseUser.getEmail())){
                        //Eğer email bir admin hesap ise
                        flag = 1;
                        startActivity(new Intent(MainActivity.this,Admin.class));
                        finish();
                    }
                }
                if(flag == 0){
                    //öğrenciler admin hesaba erişemez
                    Toast.makeText(MainActivity.this, "Öğretmen hesabına geçiş yapamazsınız!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //oluşturulan menu initialize edildi
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.go_to_admin,menu);
        return true;
    }


    //menu de bulunan elemanlara tıklama kod bloğu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_goToAdmin:
                //kullanıcı admin mi yoksa öğrenci mi kontrol edildi
                checkThePerson();
                break;
            case R.id.nav_logout:
                //çıkış yapıldı
                auth.signOut();
                startActivity(new Intent(MainActivity.this,OgretmenGirisi.class));
                finish();
        }

        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    //geliştiriciler layoutu çağırıldı
    public void developerLayout(){
        drawerLayout.closeDrawers();
        LayoutInflater inflater = this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.developers_layout, null);

        //dialog initialize edildi
        Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}