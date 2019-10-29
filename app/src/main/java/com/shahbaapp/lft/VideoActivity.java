package com.shahbaapp.lft;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.khizar1556.mkvideoplayer.MKPlayerActivity;

import Controller.Api;

public class VideoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        int id = getIntent().getIntExtra("id", 0);

        MKPlayerActivity.configPlayer(this).play(Api.ROOT + "videos/" + id + ".mp4");



    }

}
