package com.jonathanaguilar.datareflix.Controllers;

import com.google.firebase.database.DatabaseReference;
import com.jonathanaguilar.datareflix.Objetos.Ob_actividad;

public class Ctl_actividad {

    DatabaseReference dbref;

    public Ctl_actividad(DatabaseReference dbref) {
        this.dbref = dbref;
    }


    public void crear_actividad(Ob_actividad obActividad){

        dbref.child("actividades").setValue(obActividad);

    }

}
