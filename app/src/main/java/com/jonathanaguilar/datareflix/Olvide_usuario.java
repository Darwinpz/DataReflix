package com.jonathanaguilar.datareflix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jonathanaguilar.datareflix.Controllers.Alert_dialog;
import com.jonathanaguilar.datareflix.Controllers.Progress_dialog;
import com.jonathanaguilar.datareflix.Fragments.Fragment_Usuarios;
import com.jonathanaguilar.datareflix.Objetos.Ob_solicitud;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Olvide_usuario extends AppCompatActivity {

    Button btn_resetear;
    EditText txt_usuario;
    Alert_dialog alertDialog;
    Progress_dialog dialog;
    DatabaseReference dbref;
    String UID_EMPLEADO = "";
    boolean pendiente = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvide_usuario);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        txt_usuario = findViewById(R.id.editTextTextusuario);
        btn_resetear = findViewById(R.id.btn_resetear);

        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);

        dbref = MainActivity.DB.getReference();

        txt_usuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.toString().trim().length() == 10){

                    if (!Validar_Cedula(editable.toString().trim())) {
                        txt_usuario.setError("Ingresa una cédula válida");
                    }else{

                        dbref.child("usuarios").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                                if(datasnapshot.exists()){

                                    for (DataSnapshot snapshot : datasnapshot.getChildren()) {

                                        if(snapshot.child("cedula").exists()){

                                            if (Objects.requireNonNull(snapshot.child("cedula").getValue()).toString().equalsIgnoreCase(txt_usuario.getText().toString())){

                                                UID_EMPLEADO = snapshot.getKey();

                                                if(snapshot.child("solicitudes").exists()) {

                                                    if (snapshot.child("estado").exists()) {

                                                        for(DataSnapshot soli : snapshot.child("solicitudes").getChildren()){

                                                            if(soli.child("tipo").exists()){

                                                                if(soli.child("tipo").getValue().toString().equalsIgnoreCase("reseteo de clave")){

                                                                    if (Objects.requireNonNull(soli.child("estado").getValue()).toString().equalsIgnoreCase("pendiente")) {
                                                                        pendiente = true;
                                                                    }else{
                                                                        pendiente = false;
                                                                    }

                                                                }

                                                            }

                                                        }

                                                    }

                                                }

                                            }
                                        }

                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                }else{
                    txt_usuario.setError("Ingresa 10 dígitos");
                }


            }
        });



        btn_resetear.setOnClickListener(view -> {

            dialog.mostrar_mensaje("Creando Solicitud...");

            if(!txt_usuario.getText().toString().isEmpty() && txt_usuario.getError() == null ){

                if(!UID_EMPLEADO.isEmpty()) {

                    if(!pendiente) {

                        Date dia = new Date();
                        String hora = String.format("%02d:%02d", dia.getHours(), dia.getMinutes()) + " " + ((dia.getHours() < 12) ? "am" : "pm");

                        Ob_solicitud solicitud = new Ob_solicitud();
                        solicitud.motivo = "Olvidé mi Clave";
                        solicitud.estado = "Pendiente";
                        solicitud.fecha_solicitud = (new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(dia.getTime())) + " - " + hora;
                        solicitud.tipo = "Reseteo de clave";

                        dbref.child("usuarios").child(UID_EMPLEADO).child("solicitudes").push().setValue(solicitud);

                        dialog.ocultar_mensaje();
                        alertDialog.crear_mensaje("Correcto", "Solicitud Creada Correctamente", builder -> {
                            builder.setCancelable(false);
                            builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {
                            });
                            builder.create().show();
                        });

                    }else{
                        dialog.ocultar_mensaje();
                        alertDialog.crear_mensaje("¡Advertencia!", "Ya tienes una solicitud Pendiente", builder -> {
                            builder.setCancelable(true);
                            builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                            builder.create().show();
                        });
                    }

                }else{
                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("¡Advertencia!", "No existe el usuario", builder -> {
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

    public boolean Validar_Cedula(String cedula){

        int suma = 0;

        for (int i = 0; i < 9; i++)
        {
            int coeficiente = ((i % 2) == 0) ? 2 : 1;
            int calculo = Integer.parseInt(String.valueOf(cedula.charAt(i))) * coeficiente;
            suma += (calculo >= 10) ? calculo - 9 : calculo;
        }

        int residuo = suma % 10;
        int valor = (residuo == 0) ? 0 : (10 - residuo);

        return Integer.parseInt(String.valueOf(cedula.charAt(9))) == valor;

    }

}