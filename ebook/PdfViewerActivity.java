package com.example.kayit.ebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.kayit.R;

import java.net.URLEncoder;

public class PdfViewerActivity extends AppCompatActivity {

    String url;
    WebView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //pdf url intent ile alındı
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("pdfUrl");
        }

        pdfView = findViewById(R.id.pdfView);
        pdfView.getSettings().setJavaScriptEnabled(true);
        pdfView.setWebViewClient(new WebViewClient());

        //progress dialog oluşturuldu
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Açılıyor!");


        pdfView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pd.dismiss();
            }
        });

        String fileUrl = "";

        //url encode edildi
        try {
            fileUrl = URLEncoder.encode(url,"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }

        //pdf url si google docs ile açıldı
        pdfView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + fileUrl);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //menu initialize edildi
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.download_file,menu);

        return super.onCreateOptionsMenu(menu);
    }

    //itemlere tıklayınca çalışacak methodlar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.nav_download){
            //pdf download edildi
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url)).setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                    DownloadManager.Request.NETWORK_MOBILE);
            // in order for this if to run, you must use the android 3.2 to compile your app
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setTitle("Döküman");
            }
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, url.substring(url.lastIndexOf('/')));

            // get download service and enqueue file
            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
        }
        return super.onOptionsItemSelected(item);
    }
}