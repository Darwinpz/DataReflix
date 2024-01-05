package com.jonathanaguilar.datareflix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jonathanaguilar.datareflix.Controllers.Alert_dialog;
import com.jonathanaguilar.datareflix.Controllers.Progress_dialog;

public class MainActivity extends AppCompatActivity {

    public static FirebaseDatabase DB = FirebaseDatabase.getInstance();
    public static SharedPreferences preferences;
    DatabaseReference databaseReference;
    EditText txt_usuario, txt_clave;
    private Progress_dialog dialog;
    public static Activity activity;
    public Alert_dialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_ingreso = findViewById(R.id.btn_ingresar);

        preferences = getPreferences(MODE_PRIVATE);

        databaseReference = DB.getReference();

        txt_usuario = (EditText) findViewById(R.id.editTextTextEmailAddress);
        txt_clave = (EditText) findViewById(R.id.editTextTextPassword);

        dialog = new Progress_dialog(new ProgressDialog(this),this);
        alertDialog = new Alert_dialog(this);


        btn_ingreso.setOnClickListener(view -> {

            dialog.mostrar_mensaje("Iniciando sesión...");

            if (!txt_usuario.getText().toString().isEmpty()&&!txt_clave.getText().toString().isEmpty()) {

                databaseReference.child("usuarios").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                        if (datasnapshot.exists()) {

                            for (DataSnapshot snapshot : datasnapshot.getChildren()) {

                                if(snapshot.child("usuario").getValue().toString().equals(txt_usuario.getText().toString()) &&
                                        snapshot.child("clave").getValue().toString().equals(txt_clave.getText().toString())){

                                    dialog.ocultar_mensaje();

                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("uid", snapshot.getKey().toString());
                                    editor.apply();

                                    startActivity(new Intent(getBaseContext(), Principal.class));

                                    finish();

                                }

                            }

                            alertDialog.crear_mensaje("Advertencia", "Usuario y/o Clave Incorrecto",builder -> {
                                builder.setCancelable(true);
                                alertDialog.mostrar_mensaje(builder);
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        alertDialog.crear_mensaje("Advertencia", "Error al Iniciar Sesión",builder -> {
                            builder.setCancelable(true);
                            alertDialog.mostrar_mensaje(builder);
                        });

                    }
                });


            }else{

                alertDialog.crear_mensaje("Advertencia", "Ingresa el usuario y la clave",builder -> {

                    builder.setCancelable(true);
                    alertDialog.mostrar_mensaje(builder);

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
        }

    }
}