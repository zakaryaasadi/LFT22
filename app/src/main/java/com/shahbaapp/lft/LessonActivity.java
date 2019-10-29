package com.shahbaapp.lft;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import Adapter.LessonAdapter;
import Controller.Common;
import Models.LessonClass;

public class LessonActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LessonAdapter bAdapter;
    private List<LessonClass> lessonClasses = new ArrayList<>();
    private Toolbar toolbar;
    private int sessionId;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        sessionId = i.getIntExtra("id", 0);
        String title = i.getStringExtra("title");
        String strJson = i.getStringExtra("lessons");

        toolbar.setTitle(title);
        lessonClasses = new Gson().fromJson(strJson, new TypeToken<List<LessonClass>>(){}.getType());


        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LessonActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        bAdapter = new LessonAdapter(LessonActivity.this, lessonClasses);
        recyclerView.setAdapter(bAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LessonActivity.this, ExistActivity.class);
                i.putExtra("id", sessionId);
                startActivity(i);
            }
        });


        if(true){
            fab.setVisibility(View.VISIBLE);
        }else{
            fab.setVisibility(View.VISIBLE);
        }


    }


}
