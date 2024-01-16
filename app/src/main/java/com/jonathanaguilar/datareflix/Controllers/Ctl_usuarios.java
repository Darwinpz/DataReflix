package com.jonathanaguilar.datareflix.Controllers;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jonathanaguilar.datareflix.Adaptadores.Adapter_usuario;
import com.jonathanaguilar.datareflix.Objetos.Ob_usuario;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Ctl_usuarios {

    DatabaseReference dbref;

    public Ctl_usuarios(DatabaseReference dbref) {
        this.dbref = dbref;
    }

    public void crear_usuarios(Ob_usuario obUsuario){

        dbref.child("usuarios").push().setValue(obUsuario);

    }

    public void eliminar_usuario(String uid){
        dbref.child("usuarios").child(uid).removeValue();
    }

    public void update_usuario(Ob_usuario usuario) {

        if(usuario.uid != null) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("cedula", usuario.cedula);
            datos.put("nombre", usuario.nombre);
            datos.put("email", usuario.email.toLowerCase());
            datos.put("telefono", usuario.telefono);
            datos.put("rol", usuario.rol);
            dbref.child("usuarios").child(usuario.uid).updateChildren(datos);
        }

    }

    public void VerUsuarios(Adapter_usuario list_usuario, String uid, final TextView textView, final ProgressBar progressBar, TextView txt_contador) {

        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);

        dbref.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    list_usuario.ClearUsuario();
                    int contador = 0;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        if(!Objects.equals(snapshot.getKey(), uid)) {

                            Ob_usuario usuario = new Ob_usuario();
                            usuario.uid = snapshot.getKey();

                            if (snapshot.child("cedula").exists()) {
                                usuario.cedula = Objects.requireNonNull(snapshot.child("cedula").getValue()).toString();
                            }
                            if (snapshot.child("nombre").exists()) {
                                usuario.nombre = Objects.requireNonNull(snapshot.child("nombre").getValue()).toString();
                            }
                            if (snapshot.child("url_foto").exists()) {
                                usuario.url_foto = Objects.requireNonNull(snapshot.child("url_foto").getValue()).toString();
                            }
                            if (snapshot.child("email").exists()) {
                                usuario.email = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                            }
                            if (snapshot.child("telefono").exists()) {
                                usuario.telefono = Objects.requireNonNull(snapshot.child("telefono").getValue()).toString();
                            }
                            if (snapshot.child("rol").exists()) {
                                usuario.rol = Objects.requireNonNull(snapshot.child("rol").getValue()).toString();
                            }

                            list_usuario.AddUsuario(usuario);
                            contador++;

                        }

                    }


                    txt_contador.setText(contador + " Usuarios");
                    progressBar.setVisibility(View.GONE);

                    if (list_usuario.getItemCount() == 0) {
                        textView.setVisibility(View.VISIBLE);
                    } else {
                        textView.setVisibility(View.GONE);
                    }

                    list_usuario.notifyDataSetChanged();

                } else {
                    list_usuario.ClearUsuario();
                    list_usuario.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    
}
