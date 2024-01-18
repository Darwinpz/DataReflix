package com.jonathanaguilar.datareflix.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jonathanaguilar.datareflix.Holders.Holder_semanas;
import com.jonathanaguilar.datareflix.Objetos.Ob_semana;
import com.jonathanaguilar.datareflix.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_semanas extends RecyclerView.Adapter<Holder_semanas> {

    public List<Ob_semana> list_semana = new ArrayList<>();
    Context context;

    public Adapter_semanas(Context context) {
        this.context = context;
    }

    public void AddSemana (Ob_semana semana){
        list_semana.add(semana);
        notifyItemInserted(list_semana.size());
    }

    public void ClearSemana (){
        list_semana.clear();
    }

    @NonNull
    @Override
    public Holder_semanas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_semanas,parent,false);
        return new Holder_semanas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder_semanas holder, int position) {

        holder.card_fecha.setText(list_semana.get(position).fecha);

        if(list_semana.get(position).hora_inicio !=null && list_semana.get(position).hora_fin !=null ){
            holder.card_horas.setText(list_semana.get(position).hora_inicio+ " - "+ list_semana.get(position).hora_fin);
        }else{
            holder.card_horas.setText("No Programado");
        }


    }

    @Override
    public int getItemCount() {
        return list_semana.size();
    }


    public Ob_semana getSemana(int position) {
        return list_semana.get(position);
    }
}
