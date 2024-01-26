package com.jonathanaguilar.datareflix.Usuarios;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;

import com.jonathanaguilar.datareflix.Controllers.Alert_dialog;
import com.jonathanaguilar.datareflix.Controllers.Progress_dialog;
import com.jonathanaguilar.datareflix.Fragments.Fragment_Usuarios;
import com.jonathanaguilar.datareflix.Objetos.Ob_usuario;
import com.jonathanaguilar.datareflix.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Add_usuario extends AppCompatActivity {

    EditText editTextcedula, editTextnombre, editTextTextEmailAddress, editTextTextPhone;
    Spinner spinner_tipo, spinner_estado;
    Button btn_add_usuario;
    ArrayAdapter<CharSequence> adapterspinner_tipo, adapterspinner_estado;
    Alert_dialog alertDialog;
    Progress_dialog dialog;
    CalendarView fecha_inicio;
    long fecha_cal_ini;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_usuario);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        editTextcedula = findViewById(R.id.editTextcedula);
        editTextnombre = findViewById(R.id.editTextnombre);
        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        editTextTextPhone = findViewById(R.id.editTextTextPhone);
        spinner_tipo = findViewById(R.id.spinner_tipo);
        spinner_estado = findViewById(R.id.spinner_estado);
        btn_add_usuario = findViewById(R.id.btn_add_usuario);

        Date dia = new Date();
        fecha_inicio = findViewById(R.id.fecha_inicio);
        fecha_cal_ini = dia.getTime();

        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);

        adapterspinner_tipo = ArrayAdapter.createFromResource(this, R.array.rol, android.R.layout.simple_spinner_item);
        adapterspinner_tipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo.setAdapter(adapterspinner_tipo);

        adapterspinner_estado = ArrayAdapter.createFromResource(this, R.array.estado_user, android.R.layout.simple_spinner_item);
        adapterspinner_estado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_estado.setAdapter(adapterspinner_estado);

        fecha_inicio.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year,month,dayOfMonth);
            view.setDate(calendar.getTimeInMillis());
            fecha_cal_ini = view.getDate();
        });

        editTextcedula.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.toString().trim().length() == 10){
                    if(!Fragment_Usuarios.ctlUsuarios.Validar_Cedula(editable.toString().trim())){
                        editTextcedula.setError("Cédula Incorrecta");
                    }
                }else{
                    editTextcedula.setError("Ingresa 10 dígitos");
                }

            }
        });

        editTextnombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!Fragment_Usuarios.ctlUsuarios.validar_usuario(editable.toString().trim())){
                    editTextnombre.setError("Ingresa un nombre válido");
                }
            }
        });

        editTextTextEmailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!Fragment_Usuarios.ctlUsuarios.validar_correo(editable.toString().trim())){
                    editTextTextEmailAddress.setError("Ingresa un correo válido");
                }
            }
        });


        editTextTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().trim().length() == 10) {
                    if (!Fragment_Usuarios.ctlUsuarios.validar_celular(editable.toString().trim())) {
                        editTextTextPhone.setError("Ingresa un celular válido");
                    }
                }else{
                    editTextTextPhone.setError("Ingresa 10 dígitos");
                }
            }
        });

        btn_add_usuario.setOnClickListener(view -> {

            dialog.mostrar_mensaje("Creando Usuario...");

            if(!editTextcedula.getText().toString().trim().isEmpty() && editTextcedula.getError() == null &&
                    !editTextnombre.getText().toString().trim().isEmpty() && editTextnombre.getError() == null  &&
                    !editTextTextEmailAddress.getText().toString().trim().isEmpty() && editTextTextEmailAddress.getError() == null &&
                    !editTextTextPhone.getText().toString().trim().isEmpty() &&  !spinner_tipo.getSelectedItem().toString().equals("Selecciona")
                    && !spinner_estado.getSelectedItem().toString().equals("Selecciona")) {

                Ob_usuario usuario = new Ob_usuario();
                usuario.cedula = editTextcedula.getText().toString();
                usuario.nombre = editTextnombre.getText().toString();
                usuario.email = editTextTextEmailAddress.getText().toString();
                usuario.telefono = editTextTextPhone.getText().toString();
                usuario.rol = spinner_tipo.getSelectedItem().toString();
                usuario.clave = usuario.cedula;
                usuario.estado = spinner_estado.getSelectedItem().toString();
                usuario.fecha_ini_contrato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha_cal_ini);
                Fragment_Usuarios.ctlUsuarios.crear_usuarios(usuario);

                dialog.ocultar_mensaje();
                alertDialog.crear_mensaje("Correcto", "Usuario Creado Correctamente", builder -> {
                    builder.setCancelable(false);
                    builder.setNeutralButton("Aceptar", (dialogInterface, i) -> finish());
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


    }
}