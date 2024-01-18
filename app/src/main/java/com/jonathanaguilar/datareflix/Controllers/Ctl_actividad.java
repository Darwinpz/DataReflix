package com.jonathanaguilar.datareflix.Controllers;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jonathanaguilar.datareflix.Adaptadores.Adapter_actividad;
import com.jonathanaguilar.datareflix.Objetos.Ob_actividad;

import java.util.Objects;

public class Ctl_actividad {

    DatabaseReference dbref;

    public Ctl_actividad(DatabaseReference dbref) {
        this.dbref = dbref;
    }


    public void crear_actividad(String uid_user, Ob_actividad obActividad){

        dbref.child("usuarios").child(uid_user).child("actividades").push().setValue(obActividad);

    }


    public void Ver_my_Actividades(Adapter_actividad list_actividad, String uid, final TextView textView, final ProgressBar progressBar, TextView txt_contador) {

        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);

        dbref.child("usuarios").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    list_actividad.ClearActividad();
                    int contador = 0;

                    if (dataSnapshot.child("actividades").exists()) {

                        for (DataSnapshot snapshot : dataSnapshot.child("actividades").getChildren()) {

                            Ob_actividad actividad = new Ob_actividad();
                            actividad.uid = snapshot.getKey();

                            if (snapshot.child("fecha_inicio").exists()) {
                                actividad.fecha_inicio = Objects.requireNonNull(snapshot.child("fecha_inicio").getValue()).toString();
                            }
                            if (snapshot.child("fecha_fin").exists()) {
                                actividad.fecha_fin = Objects.requireNonNull(snapshot.child("fecha_fin").getValue()).toString();
                            }
                            if (snapshot.child("estado").exists()) {
                                actividad.estado = Objects.requireNonNull(snapshot.child("estado").getValue()).toString();
                            }
                            if (snapshot.child("tipo").exists()) {
                                actividad.tipo = Objects.requireNonNull(snapshot.child("tipo").getValue()).toString();
                            }
                            if (snapshot.child("mensaje").exists()) {
                                actividad.mensaje = Objects.requireNonNull(snapshot.child("mensaje").getValue()).toString();
                            }
                            if (dataSnapshot.child("nombre").exists()) {
                                actividad.empleado = Objects.requireNonNull(dataSnapshot.child("nombre").getValue()).toString();
                            }

                            list_actividad.AddActividad(actividad);
                            contador++;

                        }

                    }

                    txt_contador.setText(contador + " Actividades");
                    progressBar.setVisibility(View.GONE);

                    if (list_actividad.getItemCount() == 0) {
                        textView.setVisibility(View.VISIBLE);
                    } else {
                        textView.setVisibility(View.GONE);
                    }

                    list_actividad.notifyDataSetChanged();

                } else {
                    list_actividad.ClearActividad();
                    list_actividad.notifyDataSetChanged();
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
