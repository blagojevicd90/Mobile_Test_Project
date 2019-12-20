package com.example.mobiletestproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CreditCardActivity extends AppCompatActivity {

    private EditText edit_card_number, edit_card_name, edit_city_name, edit_postal_code,
            edit_expiry_date, edit_security_code;
    private Button button_submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credit_card_layout);
        initComponents();
    }

    private void initComponents() {
        edit_card_number = (EditText) findViewById(R.id.edit_card_number);
        edit_card_name = (EditText) findViewById(R.id.edit_card_name);
        edit_city_name = (EditText) findViewById(R.id.edit_city_name);
        edit_postal_code = (EditText) findViewById(R.id.edit_postal_code);
        edit_expiry_date = (EditText) findViewById(R.id.edit_expiry_date);
        edit_security_code = (EditText) findViewById(R.id.edit_security_code);
        button_submit = (Button) findViewById(R.id.button_submit);
    }
}
