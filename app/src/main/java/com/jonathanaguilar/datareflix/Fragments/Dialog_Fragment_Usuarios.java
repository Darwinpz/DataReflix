package com.jonathanaguilar.datareflix.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jonathanaguilar.datareflix.Adaptadores.Adapter_usuario;
import com.jonathanaguilar.datareflix.Controllers.Ctl_usuarios;
import com.jonathanaguilar.datareflix.Principal;
import com.jonathanaguilar.datareflix.R;
import com.jonathanaguilar.datareflix.Usuarios.Add_usuario;

public class Dialog_Fragment_Usuarios extends DialogFragment {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView txt_sinresultados, txt_contador;
    Adapter_usuario adapterUsuario;
    Ctl_usuarios ctlUsuarios;
    public static DialogFragment dialogFragment;
    Button btn_add;
    @Override
    public void onDetach() {
        super.onDetach();
        dialogFragment = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View vista = inflater.inflate(R.layout.fragment_usuarios, null);
        builder.setView(vista);

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

        ctlUsuarios.VerUsuarios(adapterUsuario, Principal.id, txt_sinresultados, progressBar, txt_contador);

        dialogFragment = this;
        return builder.create();


    }
}
