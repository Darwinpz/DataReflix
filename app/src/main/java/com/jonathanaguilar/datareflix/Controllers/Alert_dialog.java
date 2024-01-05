package com.jonathanaguilar.datareflix.Controllers;

import android.app.AlertDialog;
import android.content.Context;

public class Alert_dialog {

    private AlertDialog dialog;
    private Context context;


    public Alert_dialog(Context context) {
        this.context = context;
    }

    public void crear_mensaje(String titulo, String mensaje, Interfaces.build build){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titulo)
                .setMessage(mensaje)
                .setCancelable(false);

        build.verbuilder(builder);

    }

    public void mostrar_mensaje(AlertDialog.Builder builder){
        dialog =builder.create();
        dialog.show();
    }


}
