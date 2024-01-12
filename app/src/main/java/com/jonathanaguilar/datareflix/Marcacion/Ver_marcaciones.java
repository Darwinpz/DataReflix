package com.jonathanaguilar.datareflix.Marcacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.jonathanaguilar.datareflix.R;

public class Ver_marcaciones extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_marcaciones);

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setOnClickListener(view -> finish());


    }
}