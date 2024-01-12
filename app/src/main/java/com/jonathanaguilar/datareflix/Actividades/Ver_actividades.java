package com.jonathanaguilar.datareflix.Actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.jonathanaguilar.datareflix.R;

public class Ver_actividades extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_actividades);

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setOnClickListener(view -> finish());


    }
}