package com.jonathanaguilar.datareflix.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jonathanaguilar.datareflix.R;


public class Holder_usuario extends RecyclerView.ViewHolder{

    public TextView card_cedula,card_nombre, card_telefono, card_rol;
    public CardView cardView;
    public ImageView foto;

    public Holder_usuario(@NonNull View itemView) {
        super(itemView);

        card_cedula = itemView.findViewById(R.id.card_cedula);
        card_nombre = itemView.findViewById(R.id.card_nombre);
        card_telefono = itemView.findViewById(R.id.card_telefono);
        card_rol = itemView.findViewById(R.id.card_rol);
        foto = itemView.findViewById(R.id.card_foto);
        cardView = itemView.findViewById(R.id.cardview_usuario);

    }


}
