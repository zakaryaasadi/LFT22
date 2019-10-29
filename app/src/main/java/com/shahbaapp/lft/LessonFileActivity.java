package com.shahbaapp.lft;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import Adapter.LessonFileAdapter;
import Models.FileClass;

public class LessonFileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LessonFileAdapter bAdapter;
    private List<FileClass> fileClasses = new ArrayList<>();
    private Toolbar toolbar;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        String title = i.getStringExtra("title");
        String strJson = i.getStringExtra("files");

        toolbar.setTitle(title);
        fileClasses = new Gson().fromJson(strJson, new TypeToken<List<FileClass>>(){}.getType());


        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LessonFileActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        bAdapter = new LessonFileAdapter(LessonFileActivity.this, fileClasses);
        recyclerView.setAdapter(bAdapter);


    }


}
