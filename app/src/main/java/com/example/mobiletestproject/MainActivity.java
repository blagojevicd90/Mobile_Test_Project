package com.example.mobiletestproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.mobiletestproject.Adapter.CharityAdapter;
import com.example.mobiletestproject.Model.CharityModel;
import com.example.mobiletestproject.Retrofit.RetrofitClient;
import com.example.mobiletestproject.Retrofit.omiseApi;
import com.google.android.material.snackbar.Snackbar;


import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable;
    private Retrofit retrofit;
    private Button btn_try_again;
    private omiseApi mService;
    private ProgressBar progress_bar;
    private RecyclerView recyclerView;
    private ImageView img_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        loadCharityList();
        addListener();
    }

    private void initComponents() {
        retrofit = RetrofitClient.getInstance();
        compositeDisposable = new CompositeDisposable();
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        img_error = (ImageView) findViewById(R.id.img_error);
        btn_try_again = (Button) findViewById(R.id.btn_try_again);
        mService = retrofit.create(omiseApi.class);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void displayCharityList(List<CharityModel> mList) {
        progress_bar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        CharityAdapter charityAdapter = new CharityAdapter(getApplicationContext(), mList);
        recyclerView.setAdapter(charityAdapter);
    }

    private void loadCharityList() {
        compositeDisposable.add(mService.getCharities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<CharityModel>>() {
                    @Override
                    public void accept(List<CharityModel> charityModels) throws Exception {
                        displayCharityList(charityModels);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Snackbar.make(progress_bar, getResources().getString(R.string.server_down), Snackbar.LENGTH_LONG).show();
                        setVisibilityOnError();
                    }
                }));
    }

    private void setVisibilityOnError() {
        progress_bar.setVisibility(View.GONE);
        img_error.setVisibility(View.VISIBLE);
        btn_try_again.setVisibility(View.VISIBLE);
    }

    private void resetVisibility() {
        img_error.setVisibility(View.GONE);
        btn_try_again.setVisibility(View.GONE);
        progress_bar.setVisibility(View.VISIBLE);
    }

    private void addListener() {
        btn_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetVisibility();
                loadCharityList();
            }
        });
    }
}
