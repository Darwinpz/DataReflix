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
import com.jonathanaguilar.datareflix.Adaptadores.Adapter_semanas;
import com.jonathanaguilar.datareflix.Objetos.Ob_horario;

import java.util.Objects;

public class Ctl_semanas {

    DatabaseReference dbref;

    public Ctl_semanas(DatabaseReference dbref) {
        this.dbref = dbref;
    }

    public void VerSemanas(Adapter_semanas list_semanas) {



    }



}
