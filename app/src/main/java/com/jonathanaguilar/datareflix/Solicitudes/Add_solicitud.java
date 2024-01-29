package com.jonathanaguilar.datareflix.Solicitudes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.ServerValue;
import com.jonathanaguilar.datareflix.Controllers.Alert_dialog;
import com.jonathanaguilar.datareflix.Controllers.Progress_dialog;
import com.jonathanaguilar.datareflix.Objetos.Ob_solicitud;
import com.jonathanaguilar.datareflix.Principal;
import com.jonathanaguilar.datareflix.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Add_solicitud extends AppCompatActivity {

    Spinner spinner_tipo;
    EditText editTextMotivo;
    ArrayAdapter<CharSequence> adapterspinner_tipo;
    Button btn_add_solicitud;
    Alert_dialog alertDialog;
    Progress_dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_solicitud);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        btn_add_solicitud = findViewById(R.id.btn_add_solicitud);
        spinner_tipo = findViewById(R.id.spinner_tipo);
        editTextMotivo = findViewById(R.id.editTextMotivo);

        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);

        adapterspinner_tipo = ArrayAdapter.createFromResource(this, R.array.tipo_solicitud, android.R.layout.simple_spinner_item);
        adapterspinner_tipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo.setAdapter(adapterspinner_tipo);

        btn_add_solicitud.setOnClickListener(view -> {

            dialog.mostrar_mensaje("Creando Solicitud...");

            if(!editTextMotivo.getText().toString().isEmpty()) {

                if (!spinner_tipo.getSelectedItem().toString().equals("Selecciona")) {

                    Date dia = new Date();
                    String hora = String.format("%02d:%02d", dia.getHours(), dia.getMinutes())+ " "+ ((dia.getHours()<12) ? "am":"pm");

                    Ob_solicitud solicitud = new Ob_solicitud();
                    solicitud.motivo = editTextMotivo.getText().toString();
                    solicitud.estado = "Pendiente";
                    solicitud.fecha_solicitud = (new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(dia.getTime())) + " - "+ hora;
                    solicitud.tipo = spinner_tipo.getSelectedItem().toString();

                    Ver_solicitudes.ctlSolicitud.crear_solicitud(Principal.id,solicitud);

                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("Correcto", "Solicitud Creada Correctamente", builder -> {
                        builder.setCancelable(false);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> finish());
                        builder.create().show();
                    });

                }else {
                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("¡Advertencia!", "Selecciona un Tipo de Solicitud", builder -> {
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