package com.example.homesecurity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    Uri imguri;
    WebView webView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button but1 = (Button) findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        textView.setText("");
        TextView textoverimageview = findViewById(R.id.textView2);
        TextView textoverwebview = findViewById(R.id.textView3);
        textoverimageview.setText("");
        textoverwebview.setText("");
        webView = findViewById(R.id.webView);
        if (getIntent().hasExtra("Image")) {

            //firebase initialisation
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            //to notify the user that the image is on the way
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Downloading image...");
            progressDialog.show();
            storageRef.child("image.png").getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Log.i("TAG", String.valueOf(uri));
                            imguri = uri;
                            progressDialog.dismiss();
                            textView.setText("click me to open the image in browser");
                            textoverimageview.setText("Intruder photo:");
                            textoverwebview.setText("Intruder bigger photo in webView:");
                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);
                            Glide.with(getApplicationContext())
                                    .load(imguri)
                                    .override(metrics.widthPixels, metrics.heightPixels)
                                    .into(imageView);
                            webView.loadUrl(String.valueOf(imguri));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Log.i("TAG", "Failed to load the image from the Firebase storage");
                        }
                    });
        } else {
            //running application for the first time, didn't click on the Alarm pushnotification
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opening the intruder picture in the default browser
                Intent intent = new Intent(Intent.ACTION_VIEW, imguri);
                intent.addFlags(Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER);
                startActivity(Intent.createChooser(intent, "Browse with"));

            }
        });
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMessaging.getInstance().subscribeToTopic("alarm");
                Task<String> tkn = FirebaseMessaging.getInstance().getToken();
                Toast.makeText(MainActivity.this,"Device subscribed to the alarm channel",
                        Toast.LENGTH_LONG).show();
                Log.d("App", "Token ["+tkn+"]");
            }
        });
    }
}