package com.example.android.javadevelopers;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

public class DevelopersActivity extends AppCompatActivity {

    //Variables that will hold the values sent zith the intent
    String dev_username;
    String github_url;
    String profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize Fresco
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_developers);

        Intent intent = getIntent();
        dev_username = intent.getStringExtra("username");
        github_url = intent.getStringExtra("githubUrl");
        profile_image = intent.getStringExtra("imageUrl");

        TextView devNameView = (TextView) findViewById(R.id.dev_name);
        ImageView devImageView = (ImageView) findViewById(R.id.dev_profile);
        TextView devUrlView = (TextView) findViewById(R.id.dev_url);

        String at_username = "@" + dev_username;
        devNameView.setText(at_username);
        devUrlView.setText(github_url);

        TextView shareView = (TextView) findViewById(R.id.share);
        shareView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent  i = new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(android.content.Intent.EXTRA_TEXT, "Check out this awesome developer" +" @<"+ dev_username +">"+ "," +" <"+ profile_image +">"+ ".");
                startActivity(Intent.createChooser(i, "Share via"));
            }
        });


        Uri uri = Uri.parse(profile_image);
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.dev_profile);
        draweeView.setImageURI(uri);

        devUrlView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(github_url));
                startActivity(browserIntent);
            }
        });
    }
}