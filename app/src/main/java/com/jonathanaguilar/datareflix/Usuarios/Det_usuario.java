package com.jonathanaguilar.datareflix.Usuarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jonathanaguilar.datareflix.Controllers.Alert_dialog;
import com.jonathanaguilar.datareflix.Controllers.Progress_dialog;
import com.jonathanaguilar.datareflix.Fragments.Fragment_Usuarios;
import com.jonathanaguilar.datareflix.MainActivity;
import com.jonathanaguilar.datareflix.Objetos.Ob_usuario;
import com.jonathanaguilar.datareflix.Principal;
import com.jonathanaguilar.datareflix.R;
import com.jonathanaguilar.datareflix.Vi_fotos;

import java.util.Objects;

public class Det_usuario extends AppCompatActivity {

    EditText editTextcedula, editTextnombre, editTextTextEmailAddress, editTextTextPhone;
    ImageView img_perfil;
    Spinner spinner_tipo;
    String uid = "";
    Button btn_edit_usuario, btn_del_usuario;
    ArrayAdapter<CharSequence> adapterspinner_tipo;
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

        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);

        uid = Objects.requireNonNull(getIntent().getExtras()).getString("uid");

        spinner_tipo = findViewById(R.id.spinner_tipo);

        adapterspinner_tipo = ArrayAdapter.createFromResource(this, R.array.rol, android.R.layout.simple_spinner_item);
        adapterspinner_tipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo.setAdapter(adapterspinner_tipo);

        if(!uid.isEmpty()) {

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
                        !editTextTextEmailAddress.getText().toString().isEmpty() && !editTextTextPhone.getText().toString().isEmpty()) {

                    if (!spinner_tipo.getSelectedItem().toString().equals("Selecciona")) {

                        Ob_usuario usuario = new Ob_usuario();
                        usuario.uid = uid;
                        usuario.cedula = editTextcedula.getText().toString();
                        usuario.nombre = editTextnombre.getText().toString();
                        usuario.email = editTextTextEmailAddress.getText().toString();
                        usuario.telefono = editTextTextPhone.getText().toString();
                        usuario.rol = spinner_tipo.getSelectedItem().toString();
                        Fragment_Usuarios.ctlUsuarios.update_usuario(usuario);

                        dialog.ocultar_mensaje();
                        alertDialog.crear_mensaje("Correcto", "Usuario Actualizado Correctamente", builder -> {
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

                        if(snapshot.child("rol").exists()){
                            String rol = Objects.requireNonNull(snapshot.child("rol").getValue()).toString();
                            int spinnerPosition = adapterspinner_tipo.getPosition(rol);
                            spinner_tipo.setSelection(spinnerPosition);
                        }

                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

    }
}