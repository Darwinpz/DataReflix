package com.jonathanaguilar.datareflix.Controllers;

import com.google.firebase.database.DatabaseReference;
import com.jonathanaguilar.datareflix.Objetos.Ob_horario;

public class Ctl_horario {

    DatabaseReference dbref;

    public Ctl_horario(DatabaseReference dbref) {
        this.dbref = dbref;
    }


    public void crear_horario(Ob_horario obHorario){

        dbref.child("horarios").setValue(obHorario);

    }

}
