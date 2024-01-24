package com.jonathanaguilar.datareflix.Tiendas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jonathanaguilar.datareflix.Adaptadores.Adapter_tienda;
import com.jonathanaguilar.datareflix.Objetos.Ob_tienda;
import com.jonathanaguilar.datareflix.Principal;
import com.jonathanaguilar.datareflix.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Ver_tienda extends AppCompatActivity {

    TextView detalle_dia;
    ImageView btn_antes, btn_despues;
    Calendar selectedDate;
    RecyclerView recyclerView;
    Adapter_tienda adapterTienda;
    ProgressBar progressBar;
    TextView txt_sinresultados, txt_contador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_tienda);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        detalle_dia = findViewById(R.id.detalle_dia);
        btn_antes = findViewById(R.id.btn_antes);
        btn_despues = findViewById(R.id.btn_despues);
        txt_sinresultados = findViewById(R.id.txt_sinresultados);
        progressBar = findViewById(R.id.progressBar);
        txt_contador = findViewById(R.id.txt_contador);

        recyclerView = findViewById(R.id.recyclerview_diario);
        adapterTienda = new Adapter_tienda(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterTienda);

        selectedDate = Calendar.getInstance();

        SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        detalle_dia.setText(fecha.format(selectedDate.getTime()));

        btn_antes.setOnClickListener(v -> {

            selectedDate.add(Calendar.DAY_OF_MONTH, -1);
            detalle_dia.setText(fecha.format(selectedDate.getTime()));
            GetRegistros();

        });

        btn_despues.setOnClickListener(v -> {

            selectedDate.add(Calendar.DAY_OF_MONTH, 1);
            detalle_dia.setText(fecha.format(selectedDate.getTime()));

            GetRegistros();

        });


        GetRegistros();

    }

    private void GetRegistros(){
        progressBar.setVisibility(View.VISIBLE);
        txt_sinresultados.setVisibility(View.VISIBLE);

        Principal.databaseReference.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                if(datasnapshot.exists()){

                    adapterTienda.ClearTienda();
                    int contador = 0;

                    for (DataSnapshot snapshot : datasnapshot.getChildren()) {

                        if (snapshot.child("actividades").exists()) {

                            for (DataSnapshot datos : snapshot.child("actividades").getChildren()) {

                                if (datos.child("fecha_inicio").exists()) {

                                    String fecha_inicio = Objects.requireNonNull(datos.child("fecha_inicio").getValue()).toString();

                                    if(detalle_dia.getText().toString().equals(fecha_inicio)){

                                        Ob_tienda tienda = new Ob_tienda();

                                        if(snapshot.child("nombre").exists()) {
                                            tienda.nombre = Objects.requireNonNull(snapshot.child("nombre").getValue()).toString();
                                        }
                                        if(snapshot.child("telefono").exists()) {
                                            tienda.telefono =  Objects.requireNonNull(snapshot.child("telefono").getValue()).toString();
                                        }

                                        if(datos.child("hora_inicio").exists() && datos.child("hora_fin").exists()) {
                                            tienda.horas =  Objects.requireNonNull(datos.child("hora_inicio").getValue()).toString() +" - "+  Objects.requireNonNull(datos.child("hora_fin").getValue()).toString();
                                        }

                                        if(datos.child("tipo").exists()) {
                                            tienda.tipo =  Objects.requireNonNull(datos.child("tipo").getValue()).toString();
                                        }
                                        adapterTienda.AddTienda(tienda);
                                        contador++;

                                    }

                                }

                            }

                        }

                    }

                    txt_contador.setText(contador + " Registros");
                    progressBar.setVisibility(View.GONE);

                    txt_sinresultados.setVisibility(adapterTienda.getItemCount() == 0 ? View.VISIBLE : View.GONE);

                    adapterTienda.notifyDataSetChanged();

                }else{
                    adapterTienda.ClearTienda();
                    adapterTienda.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    txt_sinresultados.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}