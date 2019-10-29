package com.shahbaapp.lft;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Adapter.ClassSubjectAdapter;
import Adapter.SubjectAdapter;
import Controller.Api;
import Controller.Common;
import Controller.DataFromApi;
import Models.ClassSubjectClass;
import Models.ClassSubjectResult;
import Models.SubjectClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private Api api;
    private SwipeRefreshLayout swipe;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Subjects");

        swipe = findViewById(R.id.swipe);
        swipe.setColorSchemeResources(R.color.orange, R.color.twittercolor, R.color.redBg);
        swipe.setOnRefreshListener(onRefreshListener);
        swipe.setRefreshing(true);


        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(SubjectActivity.this,3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        List<ClassSubjectClass> classSubject = new ArrayList<>();
        ClassSubjectAdapter bAdapter = new ClassSubjectAdapter(SubjectActivity.this, classSubject);
        recyclerView.setAdapter(bAdapter);

        fetchDataFromApi();


    }




    private void fetchDataFromApi(){
        api = DataFromApi.getApi();

        Call<ClassSubjectResult> call = api.GetClasses(Common.getUser().id); //5552

        call.enqueue(new Callback<ClassSubjectResult>() {
            @Override
            public void onResponse(Call<ClassSubjectResult> call, Response<ClassSubjectResult> response) {

                swipe.setRefreshing(false);

                ClassSubjectResult fetch = response.body();
                if(fetch != null){
                    if(fetch.results.size() == 0){
                        Toast.makeText(getApplicationContext(), "No subject or class for you",Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else if(fetch.results.size() == 1){
                        List<SubjectClass> subjectClasses = new ArrayList<>();
                        subjectClasses.addAll(fetch.results.get(0).subjects);
                        SubjectAdapter bAdapter = new SubjectAdapter(SubjectActivity.this, subjectClasses);
                        recyclerView.setAdapter(bAdapter);
                    }
                    else if(fetch.results.size() > 1){
                        List<ClassSubjectClass> classSubject = new ArrayList<>();
                        classSubject.addAll(fetch.results);
                        ClassSubjectAdapter bAdapter = new ClassSubjectAdapter(SubjectActivity.this, classSubject);
                        recyclerView.setAdapter(bAdapter);
                    }


                }
            }


            @Override
            public void onFailure(Call<ClassSubjectResult> call, Throwable t) {
                swipe.setRefreshing(false);
                Toast.makeText(getApplicationContext(), t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            swipe.setRefreshing(true);
            fetchDataFromApi();
        }
    };

}
