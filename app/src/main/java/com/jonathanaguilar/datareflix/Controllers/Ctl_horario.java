package com.jonathanaguilar.datareflix.Controllers;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jonathanaguilar.datareflix.Adaptadores.Adapter_horario;
import com.jonathanaguilar.datareflix.Objetos.Ob_horario;

import java.util.Objects;

public class Ctl_horario {

    DatabaseReference dbref;

    public Ctl_horario(DatabaseReference dbref) {
        this.dbref = dbref;
    }


    public void crear_horario(Ob_horario obHorario){

        dbref.child("horarios").push().setValue(obHorario);

    }

    public void eliminar_horario(String uid){
        dbref.child("horarios").child(uid).removeValue();
    }

    public void VerHorarios(Adapter_horario list_horario, final TextView textView, final ProgressBar progressBar, TextView txt_contador) {

        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);

        dbref.child("horarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    list_horario.ClearHorario();
                    int contador = 0;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Ob_horario horario = new Ob_horario();
                        horario.uid = snapshot.getKey();
                        if (snapshot.child("fecha").exists()) {
                            horario.fecha = Objects.requireNonNull(snapshot.child("fecha").getValue()).toString();
                        }
                        if (snapshot.child("hora_inicio").exists()) {
                            horario.hora_inicio = Objects.requireNonNull(snapshot.child("hora_inicio").getValue()).toString();
                        }
                        if (snapshot.child("hora_fin").exists()) {
                            horario.hora_fin = Objects.requireNonNull(snapshot.child("hora_fin").getValue()).toString();
                        }
                        list_horario.AddHorario(horario);
                        contador++;

                    }

                    txt_contador.setText(contador + " Registros");
                    progressBar.setVisibility(View.GONE);

                    textView.setVisibility(list_horario.getItemCount() == 0 ? View.VISIBLE : View.GONE);

                    list_horario.notifyDataSetChanged();

                } else {
                    list_horario.ClearHorario();
                    list_horario.notifyDataSetChanged();
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
