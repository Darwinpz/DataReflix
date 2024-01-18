package com.jonathanaguilar.datareflix.Actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jonathanaguilar.datareflix.Controllers.Alert_dialog;
import com.jonathanaguilar.datareflix.Controllers.Progress_dialog;
import com.jonathanaguilar.datareflix.Fragments.Dialog_Fragment_Usuarios;
import com.jonathanaguilar.datareflix.Objetos.Ob_actividad;
import com.jonathanaguilar.datareflix.Principal;
import com.jonathanaguilar.datareflix.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Add_actividad extends AppCompatActivity {

    Spinner spinner_tipo;
    Button add_empleado, btn_add_actividad;
    public static TextView card_nombre, card_cedula;
    ArrayAdapter<CharSequence> adapterspinner_tipo;
    public static String UID_EMPLEADO;
    EditText editTextActividad;
    CalendarView cal_inicio, cal_fin;
    TimePicker time_inicio, time_fin;
    Alert_dialog alertDialog;
    Progress_dialog dialog;
    long fecha_cal_ini, fecha_cal_fin;
    String hora_time_inicio, hora_time_fin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_actividad);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        add_empleado = findViewById(R.id.add_empleado);
        btn_add_actividad = findViewById(R.id.btn_add_actividad);

        card_nombre = findViewById(R.id.card_nombre);
        card_cedula = findViewById(R.id.card_cedula);

        editTextActividad = findViewById(R.id.editTextActividad);
        cal_inicio = findViewById(R.id.fecha_inicio);
        cal_fin = findViewById(R.id.fecha_fin);
        time_inicio = findViewById(R.id.hora_inicio);
        time_fin = findViewById(R.id.hora_fin);

        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);

        spinner_tipo = findViewById(R.id.spinner_tipo);

        adapterspinner_tipo = ArrayAdapter.createFromResource(this, R.array.tipo_actividad, android.R.layout.simple_spinner_item);
        adapterspinner_tipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo.setAdapter(adapterspinner_tipo);

        Date dia = new Date();

        cal_inicio.setMinDate(dia.getTime());
        cal_fin.setMinDate(dia.getTime());

        fecha_cal_ini = dia.getTime();
        hora_time_inicio = String.format("%02d:%02d",dia.getHours(),dia.getMinutes()) + " "+ ((dia.getHours()<12) ? "am":"pm");
        hora_time_fin = String.format("%02d:%02d",dia.getHours(),dia.getMinutes()) + " "+ ((dia.getHours()<12) ? "am":"pm");

        cal_inicio.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            fecha_cal_ini = view.getDate();
        });

        cal_fin.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            fecha_cal_fin = view.getDate();
        });

        time_inicio.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            hora_time_inicio = String.format("%02d:%02d",hourOfDay,minute) + " "+ ((hourOfDay<12) ? "am":"pm");
        });

        time_fin.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            hora_time_fin = String.format("%02d:%02d",hourOfDay,minute) + " "+ ((hourOfDay<12) ? "am":"pm");
        });

        add_empleado.setOnClickListener(view -> {

            Dialog_Fragment_Usuarios dialogFragmentUsuarios = new Dialog_Fragment_Usuarios();
            dialogFragmentUsuarios.show(getSupportFragmentManager(),"EMPLEADOS");

        });

        btn_add_actividad.setOnClickListener(view -> {

            dialog.mostrar_mensaje("Creando Actividad...");

            if(!editTextActividad.getText().toString().isEmpty()){

                if (!spinner_tipo.getSelectedItem().toString().equals("Selecciona")) {

                    Ob_actividad obActividad = new Ob_actividad();
                    obActividad.estado = "Pendiente";
                    obActividad.fecha_inicio = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha_cal_ini);
                    obActividad.fecha_fin = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha_cal_fin);
                    obActividad.hora_inicio = hora_time_inicio;
                    obActividad.hora_fin = hora_time_fin;
                    obActividad.tipo = spinner_tipo.getSelectedItem().toString();
                    obActividad.mensaje = editTextActividad.getText().toString();

                    Ver_actividades.ctlActividad.crear_actividad(Principal.id,obActividad);

                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("Correcto", "Actividad Creada Correctamente", builder -> {
                        builder.setCancelable(false);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> finish());
                        builder.create().show();
                    });

                }else{
                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("¡Advertencia!", "Selecciona un Tipo de Actividad", builder -> {
                        builder.setCancelable(true);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                        builder.create().show();
                    });
                }
            }else{
                dialog.ocultar_mensaje();
                alertDialog.crear_mensaje("¡Advertencia!", "Completa todos los campos", builder -> {
                    builder.setCancelable(true);
                    builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                    builder.create().show();
                });
            }

        });


    }
}