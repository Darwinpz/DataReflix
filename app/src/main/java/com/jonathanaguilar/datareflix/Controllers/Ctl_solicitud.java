package com.jonathanaguilar.datareflix.Controllers;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jonathanaguilar.datareflix.Adaptadores.Adapter_solicitud;
import com.jonathanaguilar.datareflix.Objetos.Ob_solicitud;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Ctl_solicitud {

    DatabaseReference dbref;

    public Ctl_solicitud(DatabaseReference dbref) {
        this.dbref = dbref;
    }

    public void crear_solicitud(String uid_user,Ob_solicitud obSolicitud){

        dbref.child("usuarios").child(uid_user).child("solicitudes").push().setValue(obSolicitud);

    }

    public void eliminar_solicitud(String uid_user, String uid_solicitud){
        dbref.child("usuarios").child(uid_user).child("solicitudes").child(uid_solicitud).removeValue();
    }

    public void update_solicitud(String uid_user, Ob_solicitud solicitud) {

        if(solicitud.uid != null) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("motivo", solicitud.motivo);
            datos.put("tipo", solicitud.tipo);
            datos.put("estado", solicitud.estado);
            datos.put("fecha_respuesta", solicitud.fecha_respuesta);
            dbref.child("usuarios").child(uid_user).child("solicitudes").child(solicitud.uid).updateChildren(datos);
        }

    }

    public void VerSolicitudes(Adapter_solicitud list_solicitud, final TextView textView, final ProgressBar progressBar, TextView txt_contador) {

        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);

        dbref.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    list_solicitud.ClearSolicitud();
                    int contador = 0;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        if (snapshot.child("solicitudes").exists()) {

                            for (DataSnapshot datos : snapshot.child("solicitudes").getChildren()) {

                                Ob_solicitud solicitud = new Ob_solicitud();
                                solicitud.uid = datos.getKey();

                                if (datos.child("fecha_solicitud").exists()) {
                                    solicitud.fecha_solicitud = Objects.requireNonNull(datos.child("fecha_solicitud").getValue()).toString();
                                }
                                if (datos.child("fecha_respuesta").exists()) {
                                    solicitud.fecha_respuesta = Objects.requireNonNull(datos.child("fecha_respuesta").getValue()).toString();
                                }
                                if (datos.child("estado").exists()) {
                                    solicitud.estado = Objects.requireNonNull(datos.child("estado").getValue()).toString();
                                }
                                if (datos.child("tipo").exists()) {
                                    solicitud.tipo = Objects.requireNonNull(datos.child("tipo").getValue()).toString();
                                }
                                if (datos.child("motivo").exists()) {
                                    solicitud.motivo = Objects.requireNonNull(datos.child("motivo").getValue()).toString();
                                }
                                if (snapshot.child("nombre").exists()) {
                                    solicitud.nombre_empleado = Objects.requireNonNull(snapshot.child("nombre").getValue()).toString() + " - " + Objects.requireNonNull(snapshot.child("cedula").getValue()).toString();
                                }

                                solicitud.uid_empleado = snapshot.getKey();

                                list_solicitud.AddSolicitud(solicitud);
                                contador++;

                            }

                        }

                    }

                    txt_contador.setText(contador + " Solicitudes");
                    progressBar.setVisibility(View.GONE);

                    textView.setVisibility(list_solicitud.getItemCount() == 0 ? View.VISIBLE : View.GONE);

                    list_solicitud.notifyDataSetChanged();

                } else {
                    list_solicitud.ClearSolicitud();
                    list_solicitud.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    public void Ver_my_Solicitudes(Adapter_solicitud list_solicitud, String uid, final TextView textView, final ProgressBar progressBar, TextView txt_contador) {

        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);

        dbref.child("usuarios").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    list_solicitud.ClearSolicitud();
                    int contador = 0;

                    if (dataSnapshot.child("solicitudes").exists()) {

                        for (DataSnapshot snapshot : dataSnapshot.child("solicitudes").getChildren()) {

                            Ob_solicitud solicitud = new Ob_solicitud();
                            solicitud.uid = snapshot.getKey();

                            if (snapshot.child("fecha_solicitud").exists()) {
                                solicitud.fecha_solicitud = Objects.requireNonNull(snapshot.child("fecha_solicitud").getValue()).toString();
                            }
                            if (snapshot.child("fecha_respuesta").exists()) {
                                solicitud.fecha_respuesta = Objects.requireNonNull(snapshot.child("fecha_respuesta").getValue()).toString();
                            }
                            if (snapshot.child("estado").exists()) {
                                solicitud.estado = Objects.requireNonNull(snapshot.child("estado").getValue()).toString();
                            }
                            if (snapshot.child("tipo").exists()) {
                                solicitud.tipo = Objects.requireNonNull(snapshot.child("tipo").getValue()).toString();
                            }
                            if (snapshot.child("motivo").exists()) {
                                solicitud.motivo = Objects.requireNonNull(snapshot.child("motivo").getValue()).toString();
                            }
                            if (dataSnapshot.child("nombre").exists()) {
                                solicitud.nombre_empleado = Objects.requireNonNull(dataSnapshot.child("nombre").getValue()).toString();
                            }

                            solicitud.uid_empleado = dataSnapshot.getKey();

                            list_solicitud.AddSolicitud(solicitud);
                            contador++;

                        }

                    }

                    txt_contador.setText(contador + " Solicitudes");
                    progressBar.setVisibility(View.GONE);

                    if (list_solicitud.getItemCount() == 0) {
                        textView.setVisibility(View.VISIBLE);
                    } else {
                        textView.setVisibility(View.GONE);
                    }

                    list_solicitud.notifyDataSetChanged();

                } else {
                    list_solicitud.ClearSolicitud();
                    list_solicitud.notifyDataSetChanged();
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
