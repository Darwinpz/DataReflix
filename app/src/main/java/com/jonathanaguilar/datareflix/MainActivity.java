package com.jonathanaguilar.datareflix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
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

import java.util.Objects;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    public static FirebaseDatabase DB = FirebaseDatabase.getInstance();
    public static SharedPreferences preferences;
    DatabaseReference databaseReference;
    EditText txt_usuario, txt_clave;
    Progress_dialog dialog;
    public Activity activity;
    Alert_dialog alertDialog;
    Button btn_ingresar_huella;
    BiometricPrompt.PromptInfo promptInfo;
    BiometricPrompt biometricPrompt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_ingreso = findViewById(R.id.btn_ingresar);
        btn_ingresar_huella = findViewById(R.id.btn_ingresar_huella);

        preferences = getPreferences(MODE_PRIVATE);
        databaseReference = DB.getReference();
        txt_usuario = findViewById(R.id.editTextTextEmailAddress);
        txt_clave = findViewById(R.id.editTextTextPassword);
        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);

        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                btn_ingresar_huella.setVisibility(View.VISIBLE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                btn_ingresar_huella.setVisibility(View.GONE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                btn_ingresar_huella.setVisibility(View.GONE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                if(preferences.getString("biometrico_user","").isEmpty()){
                    btn_ingresar_huella.setVisibility(View.GONE);
                }else{
                    btn_ingresar_huella.setVisibility(View.VISIBLE);
                }
                break;
        }

        btn_ingresar_huella.setOnClickListener(view -> {

            Executor executor = ContextCompat.getMainExecutor(this);
            biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Toast.makeText(getApplicationContext(), "ERROR "+errString,Toast.LENGTH_SHORT).show();

                    alertDialog.crear_mensaje("No está Configurado el Biométrico", "Configura y vuelve a Intentar", builder -> {
                        builder.setCancelable(false);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {

                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                                Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL);
                                startActivity(enrollIntent);
                            }

                        });
                        builder.create().show();
                    });

                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);

                    String id = preferences.getString("uid_biometric","");

                    if(!id.isEmpty()) {
                        databaseReference.child("usuarios").child(id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()) {

                                    if (preferences.getString("uid", "").isEmpty()) {

                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("uid", snapshot.getKey());
                                        editor.putString("rol", Objects.requireNonNull(snapshot.child("rol").getValue()).toString());
                                        editor.apply();
                                        finish();
                                        startActivity(new Intent(getBaseContext(), Principal.class));

                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }else{
                        Toast.makeText(getApplicationContext(),"No hay Registro Biometrico guardado",Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(getApplicationContext(), "Error al Autenticar",Toast.LENGTH_SHORT).show();
                }
            });

            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Verificación de Huella DataReflix")
                    .setSubtitle("Ingresa tu huella para iniciar sesión")
                    .setNegativeButtonText("Cancelar")
                    .setConfirmationRequired(false)
                    .build();

            biometricPrompt.authenticate(promptInfo);

        });

        btn_ingreso.setOnClickListener(view -> {

            Login(txt_usuario.getText().toString(),txt_clave.getText().toString());

        });


        activity = this;


    }

    private void Login(String usuario, String clave){

        dialog.mostrar_mensaje("Iniciando sesión...");

        if (!usuario.isEmpty()&&!clave.isEmpty()) {
            databaseReference.child("usuarios").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                    if (datasnapshot.exists()) {

                        boolean existe = false;
                        for (DataSnapshot snapshot : datasnapshot.getChildren()) {

                            if(snapshot.child("cedula").exists() && snapshot.child("clave").exists() && snapshot.child("rol").exists()) {
                                if (Objects.requireNonNull(snapshot.child("cedula").getValue()).toString().equals(usuario) &&
                                        Objects.requireNonNull(snapshot.child("clave").getValue()).toString().equals(clave)) {

                                    existe = true;

                                    if (preferences.getString("uid", "").isEmpty()) {
                                        SharedPreferences.Editor editor = preferences.edit();

                                        if(preferences.getString("uid_biometric","").isEmpty()){
                                            Log.e("PRUEBA","ENTRAAA");
                                            dialog.ocultar_mensaje();
                                            alertDialog.crear_mensaje("¿Desea Agregar este usuario al Biométrico?", "Accede con un solo usuario, directamente con Biometría", builder -> {
                                                builder.setPositiveButton("Aceptar", (dialogInterface, i) -> {
                                                    editor.putString("uid_biometric", snapshot.getKey());
                                                    Toast.makeText(getApplicationContext(),"Biometrico Agregado Correctamente", Toast.LENGTH_SHORT).show();

                                                });
                                                builder.setNeutralButton("Cancelar", (dialogInterface, i) -> {});
                                                builder.setCancelable(false);
                                                builder.create().show();
                                            });

                                        }
                                        dialog.mostrar_mensaje("Iniciando sesión...");

                                        editor.putString("uid", snapshot.getKey());
                                        editor.putString("rol", Objects.requireNonNull(snapshot.child("rol").getValue()).toString());
                                        editor.apply();
                                        dialog.ocultar_mensaje();
                                        finish();
                                        startActivity(new Intent(getBaseContext(), Principal.class));

                                    }
                                }
                            }
                        }

                        if(!existe) {
                            dialog.ocultar_mensaje();
                            alertDialog.crear_mensaje("Advertencia", "Usuario y/o Clave Incorrecto", builder -> {
                                builder.setCancelable(true);
                                builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
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
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                        builder.create().show();
                    });
                }
            });

        }else{

            dialog.ocultar_mensaje();
            alertDialog.crear_mensaje("Advertencia", "Ingresa el usuario y la clave",builder -> {
                builder.setCancelable(true);
                builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                builder.create().show();
            });

        }
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