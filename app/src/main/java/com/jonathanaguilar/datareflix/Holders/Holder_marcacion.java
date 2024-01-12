package com.jonathanaguilar.datareflix.Holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jonathanaguilar.datareflix.R;


public class Holder_marcacion extends RecyclerView.ViewHolder{

    public TextView card_fecha, card_hora;
    public CardView cardView;

    public Holder_marcacion(@NonNull View itemView) {
        super(itemView);

        card_fecha = (TextView) itemView.findViewById(R.id.card_fecha);
        card_hora = (TextView) itemView.findViewById(R.id.card_hora);
        cardView = (CardView) itemView.findViewById(R.id.cardview_marcacion);

    }

}
