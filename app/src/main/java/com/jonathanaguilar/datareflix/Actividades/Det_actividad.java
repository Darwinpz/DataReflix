package com.jonathanaguilar.datareflix.Actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jonathanaguilar.datareflix.Controllers.Alert_dialog;
import com.jonathanaguilar.datareflix.Controllers.Progress_dialog;
import com.jonathanaguilar.datareflix.Objetos.Ob_actividad;
import com.jonathanaguilar.datareflix.Principal;
import com.jonathanaguilar.datareflix.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Det_actividad extends AppCompatActivity {

    ArrayAdapter<CharSequence> adapterspinner_tipo, adapterspinner_estado;
    Spinner spinner_tipo, spinner_estado;
    EditText editTextActividad;
    CalendarView cal_inicio, cal_fin;
    TimePicker time_inicio, time_fin;
    Alert_dialog alertDialog;
    Progress_dialog dialog;
    long fecha_cal_ini, fecha_cal_fin;
    String hora_time_inicio, hora_time_fin;
    TextView card_nombre, card_cedula;
    String uid = "", ced_empleado = "", nom_empleado ="",uid_empleado = "";
    Button btn_edit_actividad, btn_del_actividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_actividad);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        spinner_tipo = findViewById(R.id.spinner_tipo);
        spinner_estado = findViewById(R.id.spinner_estado);
        editTextActividad = findViewById(R.id.editTextActividad);
        card_nombre = findViewById(R.id.card_nombre);
        card_cedula = findViewById(R.id.card_cedula);

        cal_inicio = findViewById(R.id.fecha_inicio);
        cal_fin = findViewById(R.id.fecha_fin);
        time_inicio = findViewById(R.id.hora_inicio);
        time_fin = findViewById(R.id.hora_fin);

        btn_edit_actividad = findViewById(R.id.btn_edit_actividad);
        btn_del_actividad = findViewById(R.id.btn_del_actividad);

        uid = Objects.requireNonNull(getIntent().getExtras()).getString("uid","");
        uid_empleado = Objects.requireNonNull(getIntent().getExtras()).getString("uid_empleado","");
        ced_empleado = Objects.requireNonNull(getIntent().getExtras()).getString("ced_empleado","");
        nom_empleado = Objects.requireNonNull(getIntent().getExtras()).getString("nom_empleado","");

        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);

        adapterspinner_tipo = ArrayAdapter.createFromResource(this, R.array.tipo_actividad, android.R.layout.simple_spinner_item);
        adapterspinner_tipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo.setAdapter(adapterspinner_tipo);

        adapterspinner_estado = ArrayAdapter.createFromResource(this, R.array.estado_actividad, android.R.layout.simple_spinner_item);
        adapterspinner_estado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_estado.setAdapter(adapterspinner_estado);

        Date dia = new Date();

        fecha_cal_ini = dia.getTime();
        fecha_cal_fin = dia.getTime();

        hora_time_inicio = String.format("%02d:%02d",dia.getHours(),dia.getMinutes()) + " "+ ((dia.getHours()<12) ? "am":"pm");
        hora_time_fin = String.format("%02d:%02d",dia.getHours()+1,dia.getMinutes()) + " "+ ((dia.getHours()<12) ? "am":"pm");

        assert uid !=null;
        if(!uid.isEmpty() && !uid_empleado.isEmpty()) {

            card_cedula.setText(ced_empleado);
            card_nombre.setText(nom_empleado);

            if(Principal.rol.equals("Administrador")){
                btn_del_actividad.setVisibility(View.VISIBLE);
                spinner_tipo.setEnabled(true);
                editTextActividad.setEnabled(true);
                time_inicio.setEnabled(true);
                time_fin.setEnabled(true);

            }else{
                btn_del_actividad.setVisibility(View.GONE);
                spinner_tipo.setEnabled(false);
                editTextActividad.setEnabled(false);
                time_inicio.setEnabled(false);
                time_fin.setEnabled(false);

            }

            btn_del_actividad.setOnClickListener(view -> {

                alertDialog.crear_mensaje("¿Estás Seguro de Eliminar la actividad?", "¡Esta acción no es reversible!", builder -> {
                    builder.setPositiveButton("Aceptar", (dialogInterface, i) -> {
                        dialog.mostrar_mensaje("Eliminando Solicitud...");
                        Ver_actividades.ctlActividad.eliminar_actividad(uid_empleado, uid);
                        dialog.ocultar_mensaje();
                        finish();
                    });
                    builder.setNeutralButton("Cancelar", (dialogInterface, i) -> {});
                    builder.setCancelable(false);
                    builder.create().show();
                });

            });

            btn_edit_actividad.setOnClickListener(view -> {

                dialog.mostrar_mensaje("Actualizando Actividad...");

                if(!editTextActividad.getText().toString().isEmpty() && !spinner_tipo.getSelectedItem().toString().equals("Selecciona") && !spinner_estado.getSelectedItem().toString().equals("Selecciona")) {

                    Ob_actividad obActividad = new Ob_actividad();
                    obActividad.uid = uid;
                    obActividad.estado = spinner_estado.getSelectedItem().toString();
                    obActividad.fecha_inicio = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha_cal_ini);
                    obActividad.fecha_fin = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha_cal_fin);
                    obActividad.hora_inicio = hora_time_inicio;
                    obActividad.hora_fin = hora_time_fin;
                    obActividad.tipo = spinner_tipo.getSelectedItem().toString();
                    obActividad.mensaje = editTextActividad.getText().toString();

                    Ver_actividades.ctlActividad.update_actividad(uid_empleado,obActividad);

                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("Correcto", "Actividad Actualizada Correctamente", builder -> {
                        builder.setCancelable(false);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                        builder.create().show();
                    });

                }else{
                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("¡Advertencia!", "Completa todos los campos", builder -> {
                        builder.setCancelable(true);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                        builder.create().show();
                    });
                }

            });

            Principal.databaseReference.child("usuarios").child(uid_empleado).child("actividades").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){

                        if(snapshot.child("fecha_inicio").exists()) {
                            String f_inicio = Objects.requireNonNull(snapshot.child("fecha_inicio").getValue()).toString();
                            int dia = Integer.parseInt(f_inicio.split("/")[0]);
                            int mes = Integer.parseInt(f_inicio.split("/")[1]) - 1;
                            int anio = Integer.parseInt(f_inicio.split("/")[2]);
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(anio,mes,dia);
                            cal_inicio.setDate(calendar.getTimeInMillis());
                            cal_inicio.setMinDate(calendar.getTimeInMillis());
                            fecha_cal_ini = calendar.getTimeInMillis();

                            if(!Principal.rol.equals("Administrador")){
                                cal_inicio.setMaxDate(fecha_cal_ini);
                            }

                        }
                        if(snapshot.child("fecha_fin").exists()) {
                            String f_fin = Objects.requireNonNull(snapshot.child("fecha_fin").getValue()).toString();
                            int dia2 = Integer.parseInt(f_fin.split("/")[0]);
                            int mes2 = Integer.parseInt(f_fin.split("/")[1]) - 1;
                            int anio2 = Integer.parseInt(f_fin.split("/")[2]);
                            Calendar calendar2 = Calendar.getInstance();
                            calendar2.set(anio2,mes2,dia2);
                            cal_fin.setDate(calendar2.getTimeInMillis());
                            cal_fin.setMinDate(cal_inicio.getMinDate());
                            fecha_cal_fin = calendar2.getTimeInMillis();

                            if(!Principal.rol.equals("Administrador")){
                                cal_fin.setMaxDate(fecha_cal_fin);
                            }

                        }

                        if(snapshot.child("hora_inicio").exists()) {
                            String h_inicio = Objects.requireNonNull(snapshot.child("hora_inicio").getValue()).toString();
                            int horas = Integer.parseInt(h_inicio.split(":")[0]);
                            int minutos = Integer.parseInt(h_inicio.split(":")[1].split(" ")[0]);
                            String ampm = h_inicio.split(":")[1].split(" ")[1];

                            if(ampm.equalsIgnoreCase("pm") && horas != 12){
                                horas  += 12;
                            }
                            time_inicio.setHour(horas);
                            time_inicio.setMinute(minutos);

                            hora_time_inicio = h_inicio;
                        }

                        if(snapshot.child("hora_fin").exists()) {
                            String h_fin = Objects.requireNonNull(snapshot.child("hora_fin").getValue()).toString();
                            int horas2 = Integer.parseInt(h_fin.split(":")[0]);
                            int minutos2 = Integer.parseInt(h_fin.split(":")[1].split(" ")[0]);
                            String ampm2 = h_fin.split(":")[1].split(" ")[1];

                            if(ampm2.equalsIgnoreCase("pm") && horas2 != 12){
                                horas2  += 12;
                            }
                            time_fin.setHour(horas2);
                            time_fin.setMinute(minutos2);
                            hora_time_fin = h_fin;

                        }

                        if(snapshot.child("mensaje").exists()){
                            editTextActividad.setText(Objects.requireNonNull(snapshot.child("mensaje").getValue()).toString());
                        }

                        if(snapshot.child("estado").exists()){
                            String estado = Objects.requireNonNull(snapshot.child("estado").getValue()).toString();
                            int spinnerPosition = adapterspinner_estado.getPosition(estado);
                            spinner_estado.setSelection(spinnerPosition);
                        }
                        if(snapshot.child("tipo").exists()){
                            String tipo = Objects.requireNonNull(snapshot.child("tipo").getValue()).toString();
                            int spinnerPosition = adapterspinner_tipo.getPosition(tipo);
                            spinner_tipo.setSelection(spinnerPosition);
                        }

                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            cal_inicio.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,dayOfMonth);
                view.setDate(calendar.getTimeInMillis());
                fecha_cal_ini = view.getDate();
            });

            cal_fin.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,dayOfMonth);
                view.setDate(calendar.getTimeInMillis());
                fecha_cal_fin = view.getDate();
            });

            time_inicio.setOnTimeChangedListener((view, hourOfDay, minute) -> {
                hora_time_inicio = String.format("%02d:%02d",hourOfDay,minute) + " "+ ((hourOfDay<12) ? "am":"pm");
            });

            time_fin.setOnTimeChangedListener((view, hourOfDay, minute) -> {
                hora_time_fin = String.format("%02d:%02d",hourOfDay,minute) + " "+ ((hourOfDay<12) ? "am":"pm");
            });


        }



    }
}