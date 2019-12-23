package com.example.mobiletestproject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobiletestproject.Model.DonationModel;
import com.example.mobiletestproject.Retrofit.RetrofitClient;
import com.example.mobiletestproject.Retrofit.omiseApi;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import co.omise.android.api.Client;
import co.omise.android.api.Request;
import co.omise.android.api.RequestListener;
import co.omise.android.models.CardParam;
import co.omise.android.models.Model;
import co.omise.android.models.Token;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class CreditCardActivity extends AppCompatActivity {

    private EditText edit_card_number, edit_card_name, edit_city_name, edit_postal_code,
            edit_expiry_date, edit_security_code, edit_amount_in_thb;
    private Button button_submit;
    private ImageButton img_button_back;
    private CompositeDisposable compositeDisposable;
    private Retrofit retrofit;
    private omiseApi mService;
    private TextView txt_donation;
    private Button btn_back;
    private ScrollView scrollview;
    private ProgressBar progress_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credit_card_layout);
        initComponents();
        addListener();
    }

    private void initComponents() {
        img_button_back = (ImageButton) findViewById(R.id.img_button_back);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        edit_amount_in_thb = (EditText) findViewById(R.id.edit_amount_in_thb);
        txt_donation = (TextView) findViewById(R.id.txt_donation);
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        btn_back = (Button) findViewById(R.id.btn_back);
        edit_card_number = (EditText) findViewById(R.id.edit_card_number);
        edit_card_name = (EditText) findViewById(R.id.edit_card_name);
        edit_city_name = (EditText) findViewById(R.id.edit_city_name);
        edit_postal_code = (EditText) findViewById(R.id.edit_postal_code);
        edit_expiry_date = (EditText) findViewById(R.id.edit_expiry_date);
        edit_security_code = (EditText) findViewById(R.id.edit_security_code);
        button_submit = (Button) findViewById(R.id.button_submit);
        compositeDisposable = new CompositeDisposable();
        retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(omiseApi.class);
    }

    private void getToken() {
        getCardParams();
    }

    private void sendRequestForToken(Request request, Client client) {
        client.send(request, new RequestListener() {
            @Override
            public void onRequestSucceed(@NotNull Model model) {
                makeDonation(model.getId());
            }

            @Override
            public void onRequestFailed(@NotNull Throwable throwable) {
                Snackbar.make(button_submit, getResources().getString(R.string.error), Snackbar.LENGTH_LONG).show();
                resetVisibilityOnError();
            }
        });
    }

    private void makeDonation(String token) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", edit_card_name.getText().toString());
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("amount", Integer.parseInt(edit_amount_in_thb.getText().toString()));

        compositeDisposable.add(mService.getDonations(jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DonationModel>() {
                    @Override
                    public void accept(DonationModel donationModel) throws Exception {
                        setVisibilityOnSucess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Snackbar.make(button_submit, getResources().getString(R.string.greater_donation), Snackbar.LENGTH_LONG).show();
                        resetVisibilityOnError();
                    }
                }));

    }

    private void getCardParams() {
        try {
            String name = edit_card_name.getText().toString();
            String number = edit_card_number.getText().toString();
            int expirationMonth = Integer.parseInt(edit_expiry_date.getText().toString().substring(0, 2));
            int expirationYear = 2020;
            String securityCode = edit_security_code.getText().toString();
            String postalCode = edit_postal_code.getText().toString();
            String city = edit_city_name.getText().toString();
            CardParam cardParam = new CardParam(name, number, expirationMonth, expirationYear, securityCode, postalCode, city);
            createRequestWithCardParam(cardParam);
        } catch (StringIndexOutOfBoundsException e) {
            Snackbar.make(button_submit, getResources().getString(R.string.error), Snackbar.LENGTH_LONG).show();
            resetVisibilityOnError();
        }
    }

    private void createRequestWithCardParam(CardParam cardParam) {
        Client client = new Client(getResources().getString(R.string.api_key));
        Request request = new Token.CreateTokenRequestBuilder(cardParam).build();
        sendRequestForToken(request, client);
    }

    private void visibilityDuringDonationRequest() {
        scrollview.setVisibility(View.INVISIBLE);
        progress_bar.setVisibility(View.VISIBLE);
    }

    private void resetVisibilityOnError() {
        progress_bar.setVisibility(View.GONE);
        scrollview.setVisibility(View.VISIBLE);
    }

    private void setVisibilityOnSucess() {
        progress_bar.setVisibility(View.GONE);
        txt_donation.setVisibility(View.VISIBLE);
        btn_back.setVisibility(View.VISIBLE);
    }

    private void addListener() {
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibilityDuringDonationRequest();
                getToken();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edit_expiry_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 3) {
                    edit_expiry_date.setText(s + "20");
                    edit_security_code.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
