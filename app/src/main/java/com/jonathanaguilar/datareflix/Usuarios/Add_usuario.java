package com.jonathanaguilar.datareflix.Usuarios;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jonathanaguilar.datareflix.R;

public class Add_usuario extends AppCompatActivity {

    Spinner spinner_tipo;
    ArrayAdapter<CharSequence> adapterspinner_tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_usuario);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        spinner_tipo = findViewById(R.id.spinner_tipo);

        adapterspinner_tipo = ArrayAdapter.createFromResource(this, R.array.rol, android.R.layout.simple_spinner_item);
        adapterspinner_tipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo.setAdapter(adapterspinner_tipo);


    }
}