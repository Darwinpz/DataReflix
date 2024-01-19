package com.jonathanaguilar.datareflix.Reportes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jonathanaguilar.datareflix.Adaptadores.Adapter_marcacion;
import com.jonathanaguilar.datareflix.Controllers.Ctl_marcacion;
import com.jonathanaguilar.datareflix.Marcacion.Add_marcacion;
import com.jonathanaguilar.datareflix.Principal;
import com.jonathanaguilar.datareflix.R;

public class Rpt_marcacion extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    ImageView filtro;
    TextView txt_sinresultados, txt_contador;
    CardView card_filtro;
    Adapter_marcacion adapterMarcacion;
    Ctl_marcacion ctlMarcacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rpt_marcacion);

        recyclerView = findViewById(R.id.recyclerview_marcaciones);
        txt_sinresultados = findViewById(R.id.txt_sinresultados);
        progressBar = findViewById(R.id.progressBar);
        txt_contador = findViewById(R.id.txt_contador);
        filtro = findViewById(R.id.filtro);
        card_filtro = findViewById(R.id.card_filtro);

        adapterMarcacion = new Adapter_marcacion(this);
        ctlMarcacion = new Ctl_marcacion(Principal.databaseReference);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterMarcacion);

        card_filtro.setVisibility(View.GONE);

        filtro.setOnClickListener(v -> {

            if(card_filtro.getVisibility() == View.VISIBLE){
                card_filtro.setVisibility(View.GONE);
            }else{
                card_filtro.setVisibility(View.VISIBLE);
            }

        });


        ctlMarcacion.VerMarcaciones(adapterMarcacion, txt_sinresultados, progressBar, txt_contador);

    }
}