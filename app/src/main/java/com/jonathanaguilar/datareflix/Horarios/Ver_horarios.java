package com.jonathanaguilar.datareflix.Horarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jonathanaguilar.datareflix.Adaptadores.Adapter_semanas;
import com.jonathanaguilar.datareflix.Objetos.Ob_semana;
import com.jonathanaguilar.datareflix.Principal;
import com.jonathanaguilar.datareflix.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class Ver_horarios extends AppCompatActivity {

    ImageView btn_antes, btn_despues;
    private Calendar selectedDate;
    TextView mes, ver_cant_horas;
    Adapter_semanas adapterSemanas;
    RecyclerView recyclerView;
    long cant_horas = 0, cant_minutos = 0, diferencia_milisegundos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_horarios);

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setOnClickListener(view -> finish());

        btn_antes = findViewById(R.id.btn_antes);
        btn_despues = findViewById(R.id.btn_despues);
        mes = findViewById(R.id.mes);
        recyclerView = findViewById(R.id.recyclerview_semanal);
        ver_cant_horas = findViewById(R.id.ver_cant_horas);

        adapterSemanas = new Adapter_semanas(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterSemanas);

        selectedDate = Calendar.getInstance();
        selectedDate.setFirstDayOfWeek(Calendar.MONDAY);
        updateMonthText();

        btn_antes.setOnClickListener(v -> {
            selectedDate.add(Calendar.WEEK_OF_YEAR, -1);
            updateMonthText();

        });

        btn_despues.setOnClickListener(v -> {
            selectedDate.add(Calendar.WEEK_OF_YEAR, 1);
            updateMonthText();

        });

    }
    private void updateMonthText() {
        int numeroSemana = selectedDate.get(Calendar.WEEK_OF_MONTH);
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        mes.setText("Semana " + numeroSemana + " - " + formatter.format(selectedDate.getTime()));

        Calendar fechaClon = (Calendar) selectedDate.clone();
        fechaClon.set(Calendar.DAY_OF_WEEK, fechaClon.getFirstDayOfWeek());

        adapterSemanas.ClearSemana();

        SimpleDateFormat format_mes = new SimpleDateFormat("MMMM", Locale.getDefault());
        SimpleDateFormat format_anio = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        for (int i = 0; i < 7; i++) {

            SimpleDateFormat formateador = new SimpleDateFormat("EEEE", Locale.getDefault());

            Ob_semana obSemana = new Ob_semana();
            obSemana.fecha = formateador.format(fechaClon.getTime()) +" "+ fechaClon.getTime().getDate() + " de "+ format_mes.format(fechaClon.getTime());
            adapterSemanas.AddSemana(obSemana);

            String fecha_comparar = format_anio.format(fechaClon.getTime());

            int finalI = i;

            diferencia_milisegundos = 0;

            Principal.databaseReference.child("horarios").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){

                        for (DataSnapshot datos : snapshot.getChildren()) {

                            if (datos.child("fecha").exists() && datos.child("hora_inicio").exists() && datos.child("hora_fin").exists() ) {

                                if(datos.child("fecha").getValue().toString().equalsIgnoreCase(fecha_comparar)){

                                    String h_inicio = datos.child("hora_inicio").getValue().toString();
                                    String h_fin = datos.child("hora_fin").getValue().toString();

                                    adapterSemanas.getSemana(finalI).hora_inicio = h_inicio;
                                    adapterSemanas.getSemana(finalI).hora_fin = h_fin;

                                    SimpleDateFormat formato24Horas = new SimpleDateFormat("HH:mm", Locale.getDefault());

                                    try {
                                        Date ini = formato24Horas.parse(h_inicio.split(" ")[0]);
                                        Date f = formato24Horas.parse(h_fin.split(" ")[0]);

                                        diferencia_milisegundos += f.getTime() - ini.getTime();

                                    } catch (ParseException e) {
                                        throw new RuntimeException(e);
                                    }


                                }

                            }

                        }

                        cant_horas = diferencia_milisegundos / (60 * 60 * 1000);
                        cant_minutos = (diferencia_milisegundos / (60 * 1000)) % 60;

                        ver_cant_horas.setText(cant_horas + "h:"+ cant_minutos+"min semanales");
                        adapterSemanas.notifyDataSetChanged();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            // Avanzar al siguiente día
            fechaClon.add(Calendar.DAY_OF_WEEK, 1);

        }

        adapterSemanas.notifyDataSetChanged();

    }


}