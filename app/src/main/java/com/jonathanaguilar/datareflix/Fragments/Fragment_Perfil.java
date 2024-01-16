package com.jonathanaguilar.datareflix.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class Fragment_Perfil extends Fragment {

    Button btn_salir, btn_update_profile;
    TextView txt_nombre, txt_cedula, txt_rol;
    EditText editTextEmail, editTextTextPhone;
    Progress_dialog dialog;
    ImageView img_perfil;
    Alert_dialog alertDialog;
    String URL_FOTO = "", NOMBRE = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View vista = inflater.inflate(R.layout.fragment_perfil,container,false);

        txt_nombre = vista.findViewById(R.id.txt_nombre);
        txt_cedula = vista.findViewById(R.id.txt_cedula);
        txt_rol = vista.findViewById(R.id.txt_rol);
        editTextEmail = vista.findViewById(R.id.editTextTextEmailAddress);
        editTextTextPhone = vista.findViewById(R.id.editTextTextPhone);
        btn_salir = vista.findViewById(R.id.btn_salir);
        img_perfil = vista.findViewById(R.id.img_perfil);
        dialog = new Progress_dialog(vista.getContext());
        alertDialog = new Alert_dialog(vista.getContext());

        btn_update_profile = vista.findViewById(R.id.btn_update_profile);

        if(!Principal.id.isEmpty()){

            btn_update_profile.setOnClickListener(view -> {

                dialog.mostrar_mensaje("Actualizando Perfil...");

                if(!editTextEmail.getText().toString().isEmpty() && !editTextTextPhone.getText().toString().isEmpty()) {

                    Ob_usuario usuario = new Ob_usuario();
                    usuario.uid = Principal.id;
                    usuario.email = editTextEmail.getText().toString();
                    usuario.telefono = editTextTextPhone.getText().toString();
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

            txt_rol.setText(Principal.rol);
            Principal.databaseReference.child("usuarios").child(Principal.id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){

                        if(snapshot.child("cedula").exists()) {
                            txt_cedula.setText(Objects.requireNonNull(snapshot.child("cedula").getValue()).toString());
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
                            editTextEmail.setText(Objects.requireNonNull(snapshot.child("email").getValue()).toString());
                        }
                        if(snapshot.child("telefono").exists()){
                            editTextTextPhone.setText(Objects.requireNonNull(snapshot.child("telefono").getValue()).toString());
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

                if(URL_FOTO != null) {

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
            MainActivity.preferences.edit().clear().apply();
            dialog.ocultar_mensaje();

            requireActivity().finish();
            startActivity(new Intent(vista.getContext(), MainActivity.class));

        });

        return vista;
    }

    public void update_perfil(Ob_usuario usuario) {

        if(usuario.uid != null) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("email", usuario.email.toLowerCase());
            datos.put("telefono", usuario.telefono);
            Principal.databaseReference.child("usuarios").child(usuario.uid).updateChildren(datos);
        }

    }


}
