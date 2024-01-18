package com.jonathanaguilar.datareflix.Horarios;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TimePicker;

import com.jonathanaguilar.datareflix.Actividades.Ver_actividades;
import com.jonathanaguilar.datareflix.Controllers.Alert_dialog;
import com.jonathanaguilar.datareflix.Controllers.Progress_dialog;
import com.jonathanaguilar.datareflix.Fragments.Fragment_Horario;
import com.jonathanaguilar.datareflix.Objetos.Ob_actividad;
import com.jonathanaguilar.datareflix.Objetos.Ob_horario;
import com.jonathanaguilar.datareflix.Principal;
import com.jonathanaguilar.datareflix.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Add_horario extends AppCompatActivity {

    CalendarView cal_inicio;
    TimePicker time_inicio, time_fin;
    Alert_dialog alertDialog;
    Progress_dialog dialog;
    long fecha_cal_ini;
    String hora_time_inicio, hora_time_fin;
    Button btn_add_horario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_horario);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        btn_add_horario = findViewById(R.id.btn_add_horario);

        cal_inicio = findViewById(R.id.fecha_inicio);
        time_inicio = findViewById(R.id.hora_inicio);
        time_fin = findViewById(R.id.hora_fin);

        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);

        Date dia = new Date();
        fecha_cal_ini = dia.getTime();

        hora_time_inicio = String.format("%02d:%02d",dia.getHours(),dia.getMinutes()) + " "+ ((dia.getHours()<12) ? "am":"pm");
        hora_time_fin = String.format("%02d:%02d",dia.getHours()+1,dia.getMinutes()) + " "+ ((dia.getHours()<12) ? "am":"pm");

        cal_inicio.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            fecha_cal_ini = view.getDate();
        });

        time_inicio.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            hora_time_inicio = String.format("%02d:%02d",hourOfDay,minute) + " "+ ((hourOfDay<12) ? "am":"pm");
        });

        time_fin.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            hora_time_fin = String.format("%02d:%02d",hourOfDay,minute) + " "+ ((hourOfDay<12) ? "am":"pm");
        });


        btn_add_horario.setOnClickListener(view -> {

            dialog.mostrar_mensaje("Creando Horario...");

            Ob_horario obHorario = new Ob_horario();
            obHorario.fecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha_cal_ini);
            obHorario.hora_inicio = hora_time_inicio;
            obHorario.hora_fin = hora_time_fin;

            Fragment_Horario.ctlHorario.crear_horario(obHorario);

            dialog.ocultar_mensaje();
            alertDialog.crear_mensaje("Correcto", "Horario Creado Correctamente", builder -> {
                builder.setCancelable(false);
                builder.setNeutralButton("Aceptar", (dialogInterface, i) -> finish());
                builder.create().show();
            });

        });


    }
}