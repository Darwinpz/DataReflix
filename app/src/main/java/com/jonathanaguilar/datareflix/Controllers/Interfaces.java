package com.jonathanaguilar.datareflix.Controllers;

import android.app.AlertDialog;

public class Interfaces {

    public Interfaces(){

    }

    /**
     * @implNote Obtener el estado de un objeto
     */
    public interface estado{

        void verestado(boolean myestado, String uid, String rol);

    }

    public interface build{

        void verbuilder(AlertDialog.Builder builder);

    }
}

