package com.jonathanaguilar.datareflix.Holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jonathanaguilar.datareflix.R;


public class Holder_marcacion extends RecyclerView.ViewHolder{

    public TextView card_fecha_hora, card_empleado, card_tipo;
    public CardView cardView;

    public Holder_marcacion(@NonNull View itemView) {
        super(itemView);

        card_fecha_hora = itemView.findViewById(R.id.card_fecha_hora);
        card_empleado = itemView.findViewById(R.id.card_empleado);
        card_tipo = itemView.findViewById(R.id.card_tipo);
        cardView =  itemView.findViewById(R.id.cardview_marcacion);

    }

}
