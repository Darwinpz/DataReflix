package com.jonathanaguilar.datareflix.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jonathanaguilar.datareflix.Controllers.Progress_dialog;
import com.jonathanaguilar.datareflix.MainActivity;
import com.jonathanaguilar.datareflix.Principal;
import com.jonathanaguilar.datareflix.R;

import java.util.Objects;

public class Fragment_Perfil extends Fragment {

    DatabaseReference databaseReference;
    Button btn_salir;
    TextView txt_nombre, txt_cedula;
    EditText editTextEmail;
    Progress_dialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View vista = inflater.inflate(R.layout.fragment_perfil,container,false);

        txt_nombre = vista.findViewById(R.id.txt_nombre);
        txt_cedula = vista.findViewById(R.id.txt_cedula);
        editTextEmail = vista.findViewById(R.id.editTextTextEmailAddress);
        btn_salir = vista.findViewById(R.id.btn_salir);
        progressDialog = new Progress_dialog(vista.getContext());

        if(!Principal.id.isEmpty()){

            databaseReference = MainActivity.DB.getReference();
            databaseReference.child("usuarios").child(Principal.id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){

                        txt_nombre.setText(Objects.requireNonNull(snapshot.child("nombre").getValue()).toString());
                        txt_cedula.setText(Objects.requireNonNull(snapshot.child("cedula").getValue()).toString());
                        editTextEmail.setText(Objects.requireNonNull(snapshot.child("email").getValue()).toString());

                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


        btn_salir.setOnClickListener(view -> {

            progressDialog.mostrar_mensaje("Cerrando Sesión...");
            MainActivity.preferences.edit().clear().apply();
            progressDialog.ocultar_mensaje();

            requireActivity().finish();
            startActivity(new Intent(vista.getContext(), MainActivity.class));

        });

        return vista;
    }

}
