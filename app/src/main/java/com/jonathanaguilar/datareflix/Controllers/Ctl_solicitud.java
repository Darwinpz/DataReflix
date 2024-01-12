package com.jonathanaguilar.datareflix.Controllers;

import com.google.firebase.database.DatabaseReference;
import com.jonathanaguilar.datareflix.Objetos.Ob_solicitud;

public class Ctl_solicitud {

    DatabaseReference dbref;

    public Ctl_solicitud(DatabaseReference dbref) {
        this.dbref = dbref;
    }

    public void crear_solicitud(Ob_solicitud obSolicitud){

        dbref.child("solicitudes").setValue(obSolicitud);

    }



}
