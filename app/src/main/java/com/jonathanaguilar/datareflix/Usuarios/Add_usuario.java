package com.jonathanaguilar.datareflix.Usuarios;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.jonathanaguilar.datareflix.Controllers.Alert_dialog;
import com.jonathanaguilar.datareflix.Controllers.Progress_dialog;
import com.jonathanaguilar.datareflix.Fragments.Fragment_Usuarios;
import com.jonathanaguilar.datareflix.Objetos.Ob_usuario;
import com.jonathanaguilar.datareflix.R;

public class Add_usuario extends AppCompatActivity {

    EditText editTextcedula, editTextnombre, editTextTextEmailAddress, editTextTextPhone;
    Spinner spinner_tipo;
    Button btn_add_usuario;
    ArrayAdapter<CharSequence> adapterspinner_tipo;
    Alert_dialog alertDialog;
    Progress_dialog dialog;
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
        btn_add_usuario = findViewById(R.id.btn_add_usuario);

        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);

        adapterspinner_tipo = ArrayAdapter.createFromResource(this, R.array.rol, android.R.layout.simple_spinner_item);
        adapterspinner_tipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo.setAdapter(adapterspinner_tipo);

        btn_add_usuario.setOnClickListener(view -> {

            dialog.mostrar_mensaje("Creando Usuario...");

            if(!editTextcedula.getText().toString().isEmpty() && !editTextnombre.getText().toString().isEmpty() &&
            !editTextTextEmailAddress.getText().toString().isEmpty() && !editTextTextPhone.getText().toString().isEmpty()) {

                if (!spinner_tipo.getSelectedItem().toString().equals("Selecciona")) {

                    Ob_usuario usuario = new Ob_usuario();
                    usuario.cedula = editTextcedula.getText().toString();
                    usuario.nombre = editTextnombre.getText().toString();
                    usuario.email = editTextTextEmailAddress.getText().toString();
                    usuario.telefono = editTextTextPhone.getText().toString();
                    usuario.rol = spinner_tipo.getSelectedItem().toString();
                    usuario.clave = usuario.cedula;

                    Fragment_Usuarios.ctlUsuarios.crear_usuarios(usuario);

                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("Correcto", "Usuario Creado Correctamente", builder -> {
                        builder.setCancelable(false);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> finish());
                        builder.create().show();
                    });

                } else {
                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("¡Advertencia!", "Selecciona un Rol", builder -> {
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