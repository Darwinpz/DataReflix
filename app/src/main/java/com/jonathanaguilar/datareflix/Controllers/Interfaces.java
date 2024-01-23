package com.jonathanaguilar.datareflix.Controllers;

import android.app.AlertDialog;

public class Interfaces {

    public Interfaces(){}

    public interface build{
        void verbuilder(AlertDialog.Builder builder);
    }

    /**
     * @implNote Obtener un contador de registros
     */
    public interface Firebase_count{
        void count(long cantidad);
    }

}

