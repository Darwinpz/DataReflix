package com.jonathanaguilar.datareflix.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jonathanaguilar.datareflix.Controllers.Alert_dialog;
import com.jonathanaguilar.datareflix.Controllers.Progress_dialog;
import com.jonathanaguilar.datareflix.MainActivity;
import com.jonathanaguilar.datareflix.Objetos.Ob_usuario;
import com.jonathanaguilar.datareflix.Principal;
import com.jonathanaguilar.datareflix.R;
import com.jonathanaguilar.datareflix.Vi_fotos;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class Fragment_Perfil extends Fragment {

    Button btn_salir, btn_update_profile;
    TextView txt_nombre, txt_cedula,txt_estado, txt_rol, cant_marcaciones, cant_solicitudes, txtfecha_ini_contrato,txtfecha_fin_contrato;
    EditText editTextEmail, editTextTextPhone, editTextTextClave;
    Progress_dialog dialog;
    ImageView img_perfil;
    Alert_dialog alertDialog;
    String URL_FOTO = "", NOMBRE = "", clave ="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View vista = inflater.inflate(R.layout.fragment_perfil,container,false);

        txt_nombre = vista.findViewById(R.id.txt_nombre);
        txt_cedula = vista.findViewById(R.id.txt_cedula);
        txt_estado = vista.findViewById(R.id.txt_estado);
        txt_rol = vista.findViewById(R.id.txt_rol);
        editTextEmail = vista.findViewById(R.id.editTextTextEmailAddress);
        editTextTextPhone = vista.findViewById(R.id.editTextTextPhone);
        btn_salir = vista.findViewById(R.id.btn_salir);
        img_perfil = vista.findViewById(R.id.img_perfil);
        dialog = new Progress_dialog(vista.getContext());
        alertDialog = new Alert_dialog(vista.getContext());
        editTextTextClave = vista.findViewById(R.id.editTextTextClave);

        cant_marcaciones = vista.findViewById(R.id.cant_marcaciones);
        cant_solicitudes = vista.findViewById(R.id.cant_solicitudes);
        txtfecha_ini_contrato = vista.findViewById(R.id.txtfecha_ini_contrato);
        txtfecha_fin_contrato = vista.findViewById(R.id.txtfecha_fin_contrato);

        btn_update_profile = vista.findViewById(R.id.btn_update_profile);

        if(!Principal.id.isEmpty()){

            btn_update_profile.setOnClickListener(view -> {

                dialog.mostrar_mensaje("Actualizando Perfil...");

                if(!editTextEmail.getText().toString().isEmpty() && editTextEmail.getError() == null
                        && !editTextTextPhone.getText().toString().isEmpty()  && editTextTextPhone.getError() == null
                        && !editTextTextClave.getText().toString().isEmpty()) {

                    Ob_usuario usuario = new Ob_usuario();
                    usuario.uid = Principal.id;
                    usuario.email = editTextEmail.getText().toString();
                    usuario.telefono = editTextTextPhone.getText().toString();
                    usuario.clave = editTextTextClave.getText().toString();
                    update_perfil(usuario);

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
                        if (!validar_celular(editable.toString().trim())) {
                            editTextTextPhone.setError("Ingresa un celular válido");
                        }
                    }else{
                        editTextTextPhone.setError("Ingresa 10 dígitos");
                    }
                }
            });

            editTextEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(!validar_correo(editable.toString().trim())){
                        editTextEmail.setError("Ingresa un correo válido");
                    }
                }
            });

            txt_rol.setText(Principal.rol);
            Principal.databaseReference.child("usuarios").child(Principal.id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){

                        if(snapshot.child("cedula").exists()) {
                            txt_cedula.setText(Objects.requireNonNull(snapshot.child("cedula").getValue()).toString());
                        }
                        if(snapshot.child("estado").exists()) {
                            txt_estado.setText(Objects.requireNonNull(snapshot.child("estado").getValue()).toString());
                            switch (txt_estado.getText().toString().toLowerCase()){
                                case "activo":
                                    txt_estado.setTextColor(ContextCompat.getColor(vista.getContext(),R.color.success));
                                    break;
                                case "inactivo":
                                    txt_estado.setTextColor(ContextCompat.getColor(vista.getContext(),R.color.danger));
                                    break;
                                default:
                                    txt_estado.setTextColor(ContextCompat.getColor(vista.getContext(),R.color.proyecto_night));
                                    break;
                            }
                        }
                        if(snapshot.child("nombre").exists()) {
                            NOMBRE = Objects.requireNonNull(snapshot.child("nombre").getValue()).toString();
                            txt_nombre.setText(NOMBRE);
                        }
                        if(snapshot.child("url_foto").exists()){
                            URL_FOTO = Objects.requireNonNull(snapshot.child("url_foto").getValue()).toString();
                            Glide.with(vista.getContext()).load(URL_FOTO).centerCrop().into(img_perfil);
                        }else{
                            img_perfil.setImageResource(R.drawable.perfil);
                        }
                        if(snapshot.child("email").exists()){
                            editTextEmail.setText(Objects.requireNonNull(snapshot.child("email").getValue()).toString().trim());
                        }
                        if(snapshot.child("clave").exists()){
                            editTextTextClave.setText(Objects.requireNonNull(snapshot.child("clave").getValue()).toString());
                            clave = Objects.requireNonNull(snapshot.child("clave").getValue()).toString();
                        }
                        if(snapshot.child("telefono").exists()){
                            editTextTextPhone.setText(Objects.requireNonNull(snapshot.child("telefono").getValue()).toString().trim());
                        }

                        if(snapshot.child("fecha_ini_contrato").exists()){
                            txtfecha_ini_contrato.setText(Objects.requireNonNull(snapshot.child("fecha_ini_contrato").getValue()).toString());
                        }
                        if(snapshot.child("fecha_fin_contrato").exists()){
                            txtfecha_fin_contrato.setText(Objects.requireNonNull(snapshot.child("fecha_fin_contrato").getValue()).toString());
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

        img_perfil.setOnClickListener(view -> {

            alertDialog.crear_mensaje("Información", "Selecciona una opción", builder -> {

                if(!URL_FOTO.isEmpty()) {

                    builder.setPositiveButton("Ver Foto", (dialogInterface, i) -> {

                        startActivity(new Intent(getContext(), Vi_fotos.class)
                                .putExtra("url", URL_FOTO)
                                .putExtra("titulo", NOMBRE));

                    });
                    builder.setNeutralButton("Subir Foto", (dialogInterface, i) -> {


                    });

                    builder.setCancelable(true);
                    builder.create().show();

                }else{




                }

            });

        });


        btn_salir.setOnClickListener(view -> {

            dialog.mostrar_mensaje("Cerrando Sesión...");

            SharedPreferences.Editor editor = Principal.preferences.edit();
            editor.putString("uid", "");
            editor.putString("rol", "");
            editor.putString("estado", "");
            editor.apply();
            //MainActivity.preferences.edit().clear().apply();
            dialog.ocultar_mensaje();
            startActivity(new Intent(vista.getContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            requireActivity().finish();

        });

        return vista;
    }

    public void update_perfil(Ob_usuario usuario) {

        if(usuario.uid != null) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("email", usuario.email.toLowerCase());
            datos.put("telefono", usuario.telefono);

            if(!clave.equals(usuario.clave)){
                if(!Principal.preferences.getString("uid_biometric","").isEmpty()){
                    SharedPreferences.Editor editor = Principal.preferences.edit();
                    editor.putString("uid_biometric","");
                    editor.apply();
                }
                datos.put("clave", usuario.clave);
            }

            Principal.databaseReference.child("usuarios").child(usuario.uid).updateChildren(datos);


        }

    }

    public boolean validar_celular(String celular){

        Pattern patron = Pattern.compile("^(0|593)?9[0-9]\\d{7}$");

        return patron.matcher(celular).matches();

    }

    public boolean validar_correo(String correo){

        Pattern patron = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.([a-zA-Z]{2,4})+$");

        return patron.matcher(correo).matches();

    }


}
