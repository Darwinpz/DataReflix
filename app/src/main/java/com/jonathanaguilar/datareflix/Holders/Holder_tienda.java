package com.jonathanaguilar.datareflix.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jonathanaguilar.datareflix.R;


public class Holder_tienda extends RecyclerView.ViewHolder{

    public TextView card_nombre, card_telefono, card_horas, card_tipo;
    public CardView cardView;
    public ImageView foto;

    public Holder_tienda(@NonNull View itemView) {
        super(itemView);

        card_nombre = itemView.findViewById(R.id.card_nombre);
        card_telefono = itemView.findViewById(R.id.card_telefono);
        card_horas = itemView.findViewById(R.id.card_horas);
        card_tipo = itemView.findViewById(R.id.card_tipo);
        foto = itemView.findViewById(R.id.card_foto);
        cardView = itemView.findViewById(R.id.cardview_usuario);

    }


}
