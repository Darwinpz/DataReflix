package com.jonathanaguilar.datareflix.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jonathanaguilar.datareflix.Adaptadores.Adapter_horario;
import com.jonathanaguilar.datareflix.Controllers.Alert_dialog;
import com.jonathanaguilar.datareflix.Controllers.Ctl_horario;
import com.jonathanaguilar.datareflix.Controllers.Progress_dialog;
import com.jonathanaguilar.datareflix.Horarios.Add_horario;
import com.jonathanaguilar.datareflix.Principal;
import com.jonathanaguilar.datareflix.R;

public class Fragment_Horario extends Fragment {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView txt_sinresultados, txt_contador;
    Adapter_horario adapterHorario;
    public static Ctl_horario ctlHorario;
    public static Alert_dialog alertDialog;
    public static Progress_dialog dialog;
    Button btn_add;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View vista =  inflater.inflate(R.layout.fragment_horario,container,false);

        recyclerView = vista.findViewById(R.id.recyclerview_horarios);
        txt_sinresultados = vista.findViewById(R.id.txt_sinresultados);
        progressBar = vista.findViewById(R.id.progressBar);
        txt_contador = vista.findViewById(R.id.txt_contador);

        dialog = new Progress_dialog(vista.getContext());
        alertDialog = new Alert_dialog(vista.getContext());

        btn_add = vista.findViewById(R.id.add_horario);

        adapterHorario = new Adapter_horario(vista.getContext());
        ctlHorario = new Ctl_horario(Principal.databaseReference);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(vista.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterHorario);

        btn_add.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Add_horario.class));
        });

        ctlHorario.VerHorarios(adapterHorario, txt_sinresultados, progressBar, txt_contador);

        return vista;

    }

}
