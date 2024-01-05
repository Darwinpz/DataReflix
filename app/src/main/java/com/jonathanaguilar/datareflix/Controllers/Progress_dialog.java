package com.jonathanaguilar.datareflix.Controllers;

import android.app.ProgressDialog;
import android.content.Context;

public class Progress_dialog {

    private ProgressDialog dialog;
    private Context context;

    public Progress_dialog(ProgressDialog dialog, Context context) {
        this.dialog = dialog;
        this.context = context;
    }

    public void mostrar_mensaje(String mensaje){
        dialog = new ProgressDialog(context);
        dialog.setMessage(mensaje);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    public void ocultar_mensaje()
    {
        dialog.dismiss();
    }

}
