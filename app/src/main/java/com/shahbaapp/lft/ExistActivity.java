package com.shahbaapp.lft;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import Adapter.ExitStudentAdapter;
import Adapter.SessionAdapter;
import Controller.Api;
import Controller.DataFromApi;
import Models.ExistStudentClass;
import Models.ExistStudentResult;
import Models.Result;
import Models.SessionClass;
import Models.SessionResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExistActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExitStudentAdapter bAdapter;
    private List<ExistStudentClass> existStudentClasses = new ArrayList<>();
    private Toolbar toolbar;
    private Api api;
    private SwipeRefreshLayout swipe;
    private long sessionId;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exist);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Exists and Laters");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                bAdapter.onClick();
                api = DataFromApi.getApi();

                Call<Result> call = api.AddExist(sessionId, existStudentClasses);
                call.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        finish();
                        Toast.makeText(getApplicationContext(), response.body().status ,Toast.LENGTH_SHORT).show();
                    }


                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {

                        Toast.makeText(getApplicationContext(), t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Intent i = getIntent();
        sessionId = i.getIntExtra("id",0);

        swipe = findViewById(R.id.swipe);
        swipe.setColorSchemeResources(R.color.orange, R.color.twittercolor, R.color.redBg);
        swipe.setOnRefreshListener(onRefreshListener);
        swipe.setRefreshing(true);


        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ExistActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        bAdapter = new ExitStudentAdapter(ExistActivity.this, existStudentClasses);
        recyclerView.setAdapter(bAdapter);

        fetchDataFromApi();


    }




    private void fetchDataFromApi(){
        api = DataFromApi.getApi();

        Call<ExistStudentResult> call = api.GetExist(sessionId);

        call.enqueue(new Callback<ExistStudentResult>() {
            @Override
            public void onResponse(Call<ExistStudentResult> call, Response<ExistStudentResult> response) {

                swipe.setRefreshing(false);

                ExistStudentResult fetch = response.body();
                if(fetch != null){
                    existStudentClasses.clear();
                    existStudentClasses.addAll(fetch.results);
                    bAdapter.notifyDataSetChanged();

                }
            }


            @Override
            public void onFailure(Call<ExistStudentResult> call, Throwable t) {
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
