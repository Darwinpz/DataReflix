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

import com.jonathanaguilar.datareflix.Adaptadores.Adapter_usuario;
import com.jonathanaguilar.datareflix.Controllers.Ctl_usuarios;
import com.jonathanaguilar.datareflix.Principal;
import com.jonathanaguilar.datareflix.R;
import com.jonathanaguilar.datareflix.Usuarios.Add_usuario;

public class Fragment_Usuarios extends Fragment {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView txt_sinresultados, txt_contador;
    Adapter_usuario adapterUsuario;
    public static Ctl_usuarios ctlUsuarios;
    Button btn_add;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_usuarios,container,false);

        recyclerView = vista.findViewById(R.id.recyclerview_usuarios);
        txt_sinresultados = vista.findViewById(R.id.txt_sinresultados);
        progressBar = vista.findViewById(R.id.progressBar);
        txt_contador = vista.findViewById(R.id.txt_contador);

        btn_add = vista.findViewById(R.id.add_usuarios);

        adapterUsuario = new Adapter_usuario(vista.getContext());
        ctlUsuarios = new Ctl_usuarios(Principal.databaseReference);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(vista.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterUsuario);

        btn_add.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Add_usuario.class));
        });

        ctlUsuarios.VerUsuarios(adapterUsuario,Principal.id, txt_sinresultados, progressBar, txt_contador);

        return vista;

    }

}
