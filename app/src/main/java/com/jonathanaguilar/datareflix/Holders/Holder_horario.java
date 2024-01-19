package com.jonathanaguilar.datareflix.Holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jonathanaguilar.datareflix.R;


public class Holder_horario extends RecyclerView.ViewHolder{

    public TextView card_fecha_hora_inicio, card_fecha_hora_fin;
    public CardView cardView;

    public Holder_horario(@NonNull View itemView) {
        super(itemView);

        card_fecha_hora_inicio = itemView.findViewById(R.id.card_fecha_hora_inicio);
        card_fecha_hora_fin = itemView.findViewById(R.id.card_fecha_hora_fin);
        cardView =  itemView.findViewById(R.id.cardview_horario);

    }

}
