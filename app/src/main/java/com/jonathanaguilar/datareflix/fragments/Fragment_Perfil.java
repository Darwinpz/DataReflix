package com.jonathanaguilar.datareflix.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jonathanaguilar.datareflix.MainActivity;
import com.jonathanaguilar.datareflix.R;

public class Fragment_Perfil extends Fragment {

    DatabaseReference databaseReference;
    final String id = MainActivity.preferences.getString("user","");
    TextView txt_usuario, txt_email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View vista = inflater.inflate(R.layout.fragment_perfil,container,false);

        txt_usuario = (TextView) vista.findViewById(R.id.txt_usuario);
        txt_email = (TextView) vista.findViewById(R.id.txt_email);

        assert id != null;
        if(!id.isEmpty()){

            databaseReference = MainActivity.DB.getReference();
            databaseReference.child("usuarios").child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){

                        txt_usuario.setText(snapshot.child("nombre").getValue().toString());
                        txt_email.setText(snapshot.child("email").getValue().toString());

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        return vista;
    }

}
