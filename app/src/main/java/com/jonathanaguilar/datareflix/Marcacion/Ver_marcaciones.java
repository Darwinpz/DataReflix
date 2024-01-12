package com.jonathanaguilar.datareflix.Marcacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jonathanaguilar.datareflix.Adaptadores.Adapter_marcacion;
import com.jonathanaguilar.datareflix.Controllers.Ctl_marcacion;
import com.jonathanaguilar.datareflix.MainActivity;
import com.jonathanaguilar.datareflix.Principal;
import com.jonathanaguilar.datareflix.R;

public class Ver_marcaciones extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView txt_sinresultados, txt_contador, txt_nombre;
    Adapter_marcacion adapterMarcacion;
    Ctl_marcacion ctlMarcacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_marcaciones);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        recyclerView = findViewById(R.id.recyclerview_marcaciones);
        txt_sinresultados = findViewById(R.id.txt_sinresultados);
        progressBar = findViewById(R.id.progressBar);
        txt_contador = findViewById(R.id.txt_contador);
        txt_nombre = findViewById(R.id.txt_nombre);

        adapterMarcacion = new Adapter_marcacion(this);
        ctlMarcacion = new Ctl_marcacion(Principal.databaseReference);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterMarcacion);

        if(!Principal.id.isEmpty() && !Principal.Nombre.isEmpty()) {
            txt_nombre.setText(Principal.Nombre);
            ctlMarcacion.VerMarcaciones(adapterMarcacion, Principal.id, txt_sinresultados, progressBar, txt_contador);
        }

    }
}