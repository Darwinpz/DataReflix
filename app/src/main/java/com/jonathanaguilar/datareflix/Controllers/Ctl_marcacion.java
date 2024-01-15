package com.jonathanaguilar.datareflix.Controllers;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jonathanaguilar.datareflix.Adaptadores.Adapter_marcacion;
import com.jonathanaguilar.datareflix.Objetos.Ob_marcacion;

import java.util.Objects;

public class Ctl_marcacion {

    DatabaseReference dbref;

    public Ctl_marcacion(DatabaseReference dbref) {
        this.dbref = dbref;
    }

    public void crear_marcacion(String uid, Ob_marcacion obMarcacion){

        if(uid != null && !uid.isEmpty()) {
            dbref.child("usuarios").child(uid).child("marcaciones").push().setValue(obMarcacion);
        }

    }

    public void VerMarcaciones(Adapter_marcacion list_marcacion, final TextView textView, final ProgressBar progressBar, TextView txt_contador) {

        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);

        dbref.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    list_marcacion.ClearMarcacion();
                    int contador = 0;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        if (snapshot.child("marcaciones").exists()) {

                            for (DataSnapshot datos : snapshot.child("marcaciones").getChildren()) {

                                if (datos.child("fecha").exists() && datos.child("hora").exists()) {

                                    Ob_marcacion marcacion = new Ob_marcacion();
                                    marcacion.uid = snapshot.getKey();
                                    marcacion.fecha = Objects.requireNonNull(datos.child("fecha").getValue()).toString();
                                    marcacion.hora = Objects.requireNonNull(datos.child("hora").getValue()).toString();
                                    marcacion.empleado = Objects.requireNonNull(snapshot.child("nombre").getValue()).toString();

                                    list_marcacion.AddMarcacion(marcacion);
                                    contador++;

                                }
                            }
                        }

                    }

                    txt_contador.setText(contador + " Marcaciones");
                    progressBar.setVisibility(View.GONE);

                    if (list_marcacion.getItemCount() == 0) {
                        textView.setVisibility(View.VISIBLE);
                    } else {
                        textView.setVisibility(View.GONE);
                    }

                    list_marcacion.notifyDataSetChanged();

                } else {
                    list_marcacion.ClearMarcacion();
                    list_marcacion.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    public void Ver_my_Marcaciones(Adapter_marcacion list_marcacion, String uid, final TextView textView, final ProgressBar progressBar, TextView txt_contador) {

        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);

        dbref.child("usuarios").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    list_marcacion.ClearMarcacion();
                    int contador = 0;

                    if (dataSnapshot.child("marcaciones").exists()) {

                        for (DataSnapshot snapshot : dataSnapshot.child("marcaciones").getChildren()) {

                            if (snapshot.child("fecha").exists() && snapshot.child("hora").exists()) {

                                Ob_marcacion marcacion = new Ob_marcacion();
                                marcacion.uid = snapshot.getKey();
                                marcacion.fecha = Objects.requireNonNull(snapshot.child("fecha").getValue()).toString();
                                marcacion.hora = Objects.requireNonNull(snapshot.child("hora").getValue()).toString();
                                marcacion.empleado = Objects.requireNonNull(dataSnapshot.child("nombre").getValue()).toString();

                                list_marcacion.AddMarcacion(marcacion);
                                contador++;

                            }
                        }

                    }

                    txt_contador.setText(contador + " Marcaciones");
                    progressBar.setVisibility(View.GONE);

                    if (list_marcacion.getItemCount() == 0) {
                        textView.setVisibility(View.VISIBLE);
                    } else {
                        textView.setVisibility(View.GONE);
                    }

                    list_marcacion.notifyDataSetChanged();

                } else {
                    list_marcacion.ClearMarcacion();
                    list_marcacion.notifyDataSetChanged();
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
