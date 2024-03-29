package com.jonathanaguilar.datareflix.Solicitudes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jonathanaguilar.datareflix.Adaptadores.Adapter_solicitud;
import com.jonathanaguilar.datareflix.Controllers.Ctl_solicitud;
import com.jonathanaguilar.datareflix.Principal;
import com.jonathanaguilar.datareflix.R;

public class Ver_solicitudes extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView txt_sinresultados, txt_contador, txt_nombre;
    Adapter_solicitud adapterSolicitud;
    public static Ctl_solicitud ctlSolicitud;
    CardView cardview_nombre;
    Button btn_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_solicitudes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        recyclerView = findViewById(R.id.recyclerview_solicitudes);
        txt_sinresultados = findViewById(R.id.txt_sinresultados);
        progressBar = findViewById(R.id.progressBar);
        txt_contador = findViewById(R.id.txt_contador);
        txt_nombre = findViewById(R.id.txt_nombre);
        cardview_nombre = findViewById(R.id.cardview_nombre);

        btn_add = findViewById(R.id.add_solicitudes);

        adapterSolicitud = new Adapter_solicitud(this);
        ctlSolicitud = new Ctl_solicitud(Principal.databaseReference);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterSolicitud);

        btn_add.setOnClickListener(view -> {
            startActivity(new Intent(this, Add_solicitud.class));
        });

        if(!Principal.id.isEmpty() && !Principal.Nombre.isEmpty()) {

            if(Principal.rol.equals("Administrador")){
                cardview_nombre.setVisibility(View.GONE);
                txt_nombre.setText("");
                ctlSolicitud.VerSolicitudes(adapterSolicitud, txt_sinresultados, progressBar, txt_contador);
            }else{
                cardview_nombre.setVisibility(View.VISIBLE);
                txt_nombre.setText(Principal.Nombre);
                ctlSolicitud.Ver_my_Solicitudes(adapterSolicitud, Principal.id, txt_sinresultados, progressBar, txt_contador);
            }
        }

    }
}