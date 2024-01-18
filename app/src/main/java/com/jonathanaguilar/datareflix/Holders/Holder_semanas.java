package com.jonathanaguilar.datareflix.Holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jonathanaguilar.datareflix.R;


public class Holder_semanas extends RecyclerView.ViewHolder{

    public TextView card_horas, card_fecha;
    public CardView cardView;

    public Holder_semanas(@NonNull View itemView) {
        super(itemView);

        card_horas = itemView.findViewById(R.id.card_horas);
        card_fecha = itemView.findViewById(R.id.card_fecha);
        cardView =  itemView.findViewById(R.id.cardview_horario);

    }

}
