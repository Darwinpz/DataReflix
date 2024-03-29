package com.jonathanaguilar.datareflix.Holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jonathanaguilar.datareflix.R;


public class Holder_actividad extends RecyclerView.ViewHolder{

    public TextView card_fecha, card_fecha_fin, card_tipo, card_estado, card_empleado;
    public CardView cardView;

    public Holder_actividad(@NonNull View itemView) {
        super(itemView);

        card_fecha = itemView.findViewById(R.id.card_fecha);
        card_fecha_fin = itemView.findViewById(R.id.card_fecha_fin);
        card_tipo = itemView.findViewById(R.id.card_tipo);
        card_estado = itemView.findViewById(R.id.card_estado);
        card_empleado = itemView.findViewById(R.id.card_empleado);
        cardView =  itemView.findViewById(R.id.cardview_actividades);

    }

}
