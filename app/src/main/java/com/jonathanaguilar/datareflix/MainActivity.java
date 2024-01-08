package com.jonathanaguilar.datareflix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jonathanaguilar.datareflix.Controllers.Alert_dialog;
import com.jonathanaguilar.datareflix.Controllers.Progress_dialog;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static FirebaseDatabase DB = FirebaseDatabase.getInstance();
    public static SharedPreferences preferences;
    DatabaseReference databaseReference;
    EditText txt_usuario, txt_clave;
    Progress_dialog dialog;
    public Activity activity;
    Alert_dialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_ingreso = findViewById(R.id.btn_ingresar);
        preferences = getPreferences(MODE_PRIVATE);
        databaseReference = DB.getReference();
        txt_usuario = findViewById(R.id.editTextTextEmailAddress);
        txt_clave = findViewById(R.id.editTextTextPassword);
        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);

        btn_ingreso.setOnClickListener(view -> {

            dialog.mostrar_mensaje("Iniciando sesión...");

            if (!txt_usuario.getText().toString().isEmpty()&&!txt_clave.getText().toString().isEmpty()) {
                databaseReference.child("usuarios").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                        if (datasnapshot.exists()) {

                            boolean existe = false;
                            for (DataSnapshot snapshot : datasnapshot.getChildren()) {

                                if(Objects.requireNonNull(snapshot.child("usuario").getValue()).toString().equals(txt_usuario.getText().toString()) &&
                                        Objects.requireNonNull(snapshot.child("clave").getValue()).toString().equals(txt_clave.getText().toString())){
                                    existe = true;
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("uid", snapshot.getKey());
                                    editor.putString("nombre", Objects.requireNonNull(snapshot.child("nombre").getValue()).toString());
                                    editor.apply();
                                    dialog.ocultar_mensaje();
                                    finish();
                                    startActivity(new Intent(getBaseContext(), Principal.class));
                                }
                            }

                            if(!existe) {
                                dialog.ocultar_mensaje();
                                alertDialog.crear_mensaje("Advertencia", "Usuario y/o Clave Incorrecto", builder -> {
                                    builder.setCancelable(true);
                                    builder.create().show();
                                });
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        dialog.ocultar_mensaje();
                        alertDialog.crear_mensaje("Advertencia", "Error al Iniciar Sesión",builder -> {
                            builder.setCancelable(true);
                            builder.create().show();
                        });
                    }
                });

            }else{

                dialog.ocultar_mensaje();
                alertDialog.crear_mensaje("Advertencia", "Ingresa el usuario y la clave",builder -> {
                    builder.setCancelable(true);
                    builder.create().show();
                });

            }

        });


        activity = this;

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!preferences.getString("uid","").isEmpty()) {
            finish();
            startActivity(new Intent(getBaseContext(), Principal.class));
        }

    }
}