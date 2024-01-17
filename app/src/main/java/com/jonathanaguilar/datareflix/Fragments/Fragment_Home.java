package com.jonathanaguilar.datareflix.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.jonathanaguilar.datareflix.Actividades.Ver_actividades;
import com.jonathanaguilar.datareflix.Horarios.Ver_horarios;
import com.jonathanaguilar.datareflix.Marcacion.Ver_marcaciones;
import com.jonathanaguilar.datareflix.R;
import com.jonathanaguilar.datareflix.Solicitudes.Ver_solicitudes;

public class Fragment_Home extends Fragment {

    CardView card_marcacion, card_horario, card_actividades, card_solicitudes, card_tienda;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View vista =  inflater.inflate(R.layout.fragment_home,container,false);

        card_marcacion = vista.findViewById(R.id.card_marcacion);
        card_horario = vista.findViewById(R.id.card_horario);
        card_actividades = vista.findViewById(R.id.card_actividades);
        card_solicitudes = vista.findViewById(R.id.card_solicitudes);
        card_tienda = vista.findViewById(R.id.card_tienda);

        card_marcacion.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Ver_marcaciones.class));
        });

        card_horario.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Ver_horarios.class));
        });

        card_actividades.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Ver_actividades.class));
        });

        card_solicitudes.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Ver_solicitudes.class));
        });

        return vista;

    }

}