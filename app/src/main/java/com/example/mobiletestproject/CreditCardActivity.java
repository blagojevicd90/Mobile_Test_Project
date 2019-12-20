package com.example.mobiletestproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import co.omise.android.api.Client;
import co.omise.android.api.Request;
import co.omise.android.api.RequestListener;
import co.omise.android.models.CardParam;
import co.omise.android.models.Model;
import co.omise.android.models.Token;

public class CreditCardActivity extends AppCompatActivity {

    private EditText edit_card_number, edit_card_name, edit_city_name, edit_postal_code,
            edit_expiry_date, edit_security_code;
    private Button button_submit;
    private ImageButton img_button_back;
    private String name, number, securityCode, postalCode, city;
    private int expirationMonth, expirationYear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credit_card_layout);
        initComponents();
        addListener();
    }

    private void initComponents() {
        img_button_back = (ImageButton) findViewById(R.id.img_button_back);
        edit_card_number = (EditText) findViewById(R.id.edit_card_number);
        edit_card_name = (EditText) findViewById(R.id.edit_card_name);
        edit_city_name = (EditText) findViewById(R.id.edit_city_name);
        edit_postal_code = (EditText) findViewById(R.id.edit_postal_code);
        edit_expiry_date = (EditText) findViewById(R.id.edit_expiry_date);
        edit_security_code = (EditText) findViewById(R.id.edit_security_code);
        button_submit = (Button) findViewById(R.id.button_submit);
    }

    private void getToken() {
        getCardParams();
        Client client = new Client(getResources().getString(R.string.api_key));
        CardParam cardParam = new CardParam(name, number, expirationMonth, expirationYear, securityCode, postalCode, city);
        Request request = new Token.CreateTokenRequestBuilder(cardParam).build();
        sendRequestForToken(request, client);
    }

    private void sendRequestForToken(Request request, Client client) {
        client.send(request, new RequestListener() {
            @Override
            public void onRequestSucceed(@NotNull Model model) {
                String token = model.getId();
            }

            @Override
            public void onRequestFailed(@NotNull Throwable throwable) {
            }
        });
    }

    private void getCardParams() {
        name = edit_card_name.getText().toString();
        number = edit_card_number.getText().toString();
        expirationMonth = Integer.parseInt(edit_expiry_date.getText().toString().substring(0, 2));
        expirationYear = 2020;
        securityCode = edit_security_code.getText().toString();
        postalCode = edit_postal_code.getText().toString();
        city = edit_city_name.getText().toString();
    }

    private void addListener() {
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getToken();
            }
        });
    }
}
