package com.shahbaapp.lft;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Adapter.SessionAdapter;
import Controller.Api;
import Controller.Common;
import Controller.DataFromApi;
import Models.SessionClass;
import Models.SessionResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SessionAdapter bAdapter;
    private List<SessionClass> sessionClasses = new ArrayList<>();
    private Toolbar toolbar;
    private Api api;
    private SwipeRefreshLayout swipe;
    private long subjectId;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Sessions");

        Intent i = getIntent();
        subjectId = i.getLongExtra("id",0);

        swipe = findViewById(R.id.swipe);
        swipe.setColorSchemeResources(R.color.orange, R.color.twittercolor, R.color.redBg);
        swipe.setOnRefreshListener(onRefreshListener);
        swipe.setRefreshing(true);


        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SessionActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        bAdapter = new SessionAdapter(SessionActivity.this, sessionClasses);
        recyclerView.setAdapter(bAdapter);

        fetchDataFromApi();


    }




    private void fetchDataFromApi(){
        api = DataFromApi.getApi();

        Call<SessionResult> call = api.GetSession(Common.getUser().id, subjectId);

        call.enqueue(new Callback<SessionResult>() {
            @Override
            public void onResponse(Call<SessionResult> call, Response<SessionResult> response) {

                swipe.setRefreshing(false);

                SessionResult fetch = response.body();
                if(sessionClasses != null && fetch != null){
                    sessionClasses.clear();
                    sessionClasses.addAll(fetch.results);
                    bAdapter.notifyDataSetChanged();

                }
            }


            @Override
            public void onFailure(Call<SessionResult> call, Throwable t) {
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
