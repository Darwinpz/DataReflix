package com.jonathanaguilar.datareflix.Usuarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jonathanaguilar.datareflix.Controllers.Alert_dialog;
import com.jonathanaguilar.datareflix.Controllers.Progress_dialog;
import com.jonathanaguilar.datareflix.Fragments.Fragment_Usuarios;
import com.jonathanaguilar.datareflix.Objetos.Ob_usuario;
import com.jonathanaguilar.datareflix.Principal;
import com.jonathanaguilar.datareflix.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Det_usuario extends AppCompatActivity {

    EditText editTextcedula, editTextnombre, editTextTextEmailAddress, editTextTextPhone;
    TextView cant_marcaciones, cant_solicitudes;
    CalendarView fecha_inicio, fecha_fin;
    long fecha_cal_ini = -1, fecha_cal_fin = -1;
    ImageView img_perfil;
    Spinner spinner_tipo, spinner_estado;
    String uid = "";
    Button btn_edit_usuario, btn_del_usuario, btn_add_fecha_fin, btn_add_fecha_inicio, btn_del_fecha_fin;
    ArrayAdapter<CharSequence> adapterspinner_tipo, adapterspinner_estado;
    Alert_dialog alertDialog;
    Progress_dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_usuario);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        editTextcedula = findViewById(R.id.editTextcedula);
        editTextnombre = findViewById(R.id.editTextnombre);
        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        editTextTextPhone = findViewById(R.id.editTextTextPhone);
        img_perfil = findViewById(R.id.img_perfil);

        btn_edit_usuario = findViewById(R.id.btn_edit_usuario);
        btn_del_usuario = findViewById(R.id.btn_del_usuario);
        btn_add_fecha_inicio = findViewById(R.id.btn_add_fecha_inicio);
        btn_add_fecha_fin = findViewById(R.id.btn_add_fecha_fin);
        btn_del_fecha_fin = findViewById(R.id.btn_del_fecha_fin);

        cant_marcaciones = findViewById(R.id.cant_marcaciones);
        cant_solicitudes = findViewById(R.id.cant_solicitudes);

        fecha_inicio = findViewById(R.id.fecha_inicio);
        fecha_fin = findViewById(R.id.fecha_fin);

        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);

        uid = Objects.requireNonNull(getIntent().getExtras()).getString("uid","");

        spinner_tipo = findViewById(R.id.spinner_tipo);
        spinner_estado = findViewById(R.id.spinner_estado);

        adapterspinner_tipo = ArrayAdapter.createFromResource(this, R.array.rol, android.R.layout.simple_spinner_item);
        adapterspinner_tipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo.setAdapter(adapterspinner_tipo);

        adapterspinner_estado = ArrayAdapter.createFromResource(this, R.array.estado_user, android.R.layout.simple_spinner_item);
        adapterspinner_estado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_estado.setAdapter(adapterspinner_estado);

        assert uid!=null;
        if(!uid.isEmpty()) {

            btn_add_fecha_inicio.setOnClickListener(v -> {
                fecha_inicio.setVisibility(View.VISIBLE);
                btn_add_fecha_inicio.setVisibility(View.GONE);
            });

            btn_add_fecha_fin.setOnClickListener(v -> {
                fecha_fin.setVisibility(View.VISIBLE);
                btn_add_fecha_fin.setVisibility(View.GONE);
            });

            btn_del_fecha_fin.setOnClickListener(v -> {
                Fragment_Usuarios.ctlUsuarios.eliminar_fecha_fin_contrato(uid);
                btn_del_fecha_fin.setVisibility(View.GONE);
            });

            btn_del_usuario.setOnClickListener(view -> {

                alertDialog.crear_mensaje("¿Estás Seguro de Eliminar el usuario?", "¡Esta acción no es reversible!", builder -> {
                    builder.setPositiveButton("Aceptar", (dialogInterface, i) -> {
                        dialog.mostrar_mensaje("Eliminando Usuario...");
                        Fragment_Usuarios.ctlUsuarios.eliminar_usuario(uid);
                        dialog.ocultar_mensaje();
                        finish();
                    });
                    builder.setNeutralButton("Cancelar", (dialogInterface, i) -> {});
                    builder.setCancelable(false);
                    builder.create().show();
                });

            });


            btn_edit_usuario.setOnClickListener(view -> {

                dialog.mostrar_mensaje("Actualizando Usuario...");

                if(!editTextcedula.getText().toString().isEmpty() && !editTextnombre.getText().toString().isEmpty() &&
                        !editTextTextEmailAddress.getText().toString().isEmpty() && !editTextTextPhone.getText().toString().isEmpty()
                && !spinner_tipo.getSelectedItem().toString().equals("Selecciona") && !spinner_estado.getSelectedItem().toString().equals("Selecciona")) {

                    Ob_usuario usuario = new Ob_usuario();
                    usuario.uid = uid;
                    usuario.cedula = editTextcedula.getText().toString();
                    usuario.nombre = editTextnombre.getText().toString();
                    usuario.email = editTextTextEmailAddress.getText().toString();
                    usuario.telefono = editTextTextPhone.getText().toString();
                    usuario.rol = spinner_tipo.getSelectedItem().toString();
                    usuario.estado = spinner_estado.getSelectedItem().toString();

                    if (fecha_inicio.getVisibility() == View.VISIBLE) {
                        long fechaContrato = (fecha_cal_ini != -1) ? fecha_cal_ini : new Date().getTime();
                        usuario.fecha_ini_contrato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fechaContrato);
                    }

                    if (fecha_fin.getVisibility() == View.VISIBLE) {
                        long fechaContratoFin = (fecha_cal_fin != -1) ? fecha_cal_fin : new Date().getTime();
                        usuario.fecha_fin_contrato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fechaContratoFin);
                    }

                    Fragment_Usuarios.ctlUsuarios.update_usuario(usuario);

                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("Correcto", "Usuario Actualizado Correctamente", builder -> {
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

            Principal.databaseReference.child("usuarios").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){

                        if(snapshot.child("cedula").exists()) {
                            editTextcedula.setText(Objects.requireNonNull(snapshot.child("cedula").getValue()).toString());
                        }
                        if(snapshot.child("nombre").exists()) {
                            editTextnombre.setText(Objects.requireNonNull(snapshot.child("nombre").getValue()).toString());
                        }
                        if(snapshot.child("url_foto").exists()){
                            String url_foto = Objects.requireNonNull(snapshot.child("url_foto").getValue()).toString();
                            Glide.with(getBaseContext()).load(url_foto).centerCrop().into(img_perfil);
                        }else{
                            img_perfil.setImageResource(R.drawable.perfil);
                        }
                        if(snapshot.child("email").exists()){
                            editTextTextEmailAddress.setText(Objects.requireNonNull(snapshot.child("email").getValue()).toString());
                        }
                        if(snapshot.child("telefono").exists()){
                            editTextTextPhone.setText(Objects.requireNonNull(snapshot.child("telefono").getValue()).toString());
                        }

                        if(snapshot.child("fecha_ini_contrato").exists()){
                            btn_add_fecha_inicio.setVisibility(View.GONE);
                            btn_add_fecha_fin.setVisibility(View.VISIBLE);
                            String f_inicio = Objects.requireNonNull(snapshot.child("fecha_ini_contrato").getValue()).toString();
                            int dia = Integer.parseInt(f_inicio.split("/")[0]);
                            int mes = Integer.parseInt(f_inicio.split("/")[1]) - 1;
                            int anio = Integer.parseInt(f_inicio.split("/")[2]);
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(anio,mes,dia);
                            fecha_inicio.setDate(calendar.getTimeInMillis());
                            fecha_cal_ini = calendar.getTimeInMillis();
                        }else{
                            fecha_inicio.setVisibility(View.GONE);
                            btn_add_fecha_inicio.setVisibility(View.VISIBLE);
                            btn_add_fecha_fin.setVisibility(View.GONE);
                        }

                        if(snapshot.child("fecha_fin_contrato").exists()){
                            btn_add_fecha_fin.setVisibility(View.GONE);
                            btn_del_fecha_fin.setVisibility(View.VISIBLE);
                            String f_fin = Objects.requireNonNull(snapshot.child("fecha_fin_contrato").getValue()).toString();
                            int dia2 = Integer.parseInt(f_fin.split("/")[0]);
                            int mes2 = Integer.parseInt(f_fin.split("/")[1]) - 1;
                            int anio2 = Integer.parseInt(f_fin.split("/")[2]);
                            Calendar calendar2 = Calendar.getInstance();
                            calendar2.set(anio2,mes2,dia2);
                            fecha_fin.setDate(calendar2.getTimeInMillis());
                            fecha_cal_fin = calendar2.getTimeInMillis();
                        }else{
                            fecha_fin.setVisibility(View.GONE);
                            btn_del_fecha_fin.setVisibility(View.GONE);
                        }

                        if(snapshot.child("estado").exists()){
                            String estado = Objects.requireNonNull(snapshot.child("estado").getValue()).toString();
                            int spinnerPosition = adapterspinner_estado.getPosition(estado);
                            spinner_estado.setSelection(spinnerPosition);
                        }

                        if(snapshot.child("rol").exists()){
                            String rol = Objects.requireNonNull(snapshot.child("rol").getValue()).toString();
                            int spinnerPosition = adapterspinner_tipo.getPosition(rol);
                            spinner_tipo.setSelection(spinnerPosition);
                        }

                        if(snapshot.child("solicitudes").exists()){
                            cant_solicitudes.setText(snapshot.child("solicitudes").getChildrenCount()+" Solicitudes");
                        }
                        if(snapshot.child("marcaciones").exists()){
                            cant_marcaciones.setText(snapshot.child("marcaciones").getChildrenCount()+" Marcaciones");
                        }

                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


        fecha_inicio.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year,month,dayOfMonth);
            view.setDate(calendar.getTimeInMillis());
            fecha_cal_ini = view.getDate();
        });

        fecha_fin.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year,month,dayOfMonth);
            view.setDate(calendar.getTimeInMillis());
            fecha_cal_fin = view.getDate();
        });

    }
}