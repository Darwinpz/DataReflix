package com.jonathanaguilar.datareflix.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jonathanaguilar.datareflix.Fragments.Fragment_Horario;
import com.jonathanaguilar.datareflix.Holders.Holder_horario;
import com.jonathanaguilar.datareflix.Objetos.Ob_horario;
import com.jonathanaguilar.datareflix.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_horario extends RecyclerView.Adapter<Holder_horario> {

    public List<Ob_horario> list_horario = new ArrayList<>();
    Context context;

    public Adapter_horario(Context context) {
        this.context = context;
    }

    public void AddHorario (Ob_horario horario ){
        list_horario.add(horario);
        notifyItemInserted(list_horario.size());
    }

    public void ClearHorario (){
        list_horario.clear();
    }

    @NonNull
    @Override
    public Holder_horario onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_horario,parent,false);
        return new Holder_horario(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder_horario holder, int position) {

        holder.card_fecha_hora_inicio.setText(list_horario.get(position).fecha + " - "+ list_horario.get(position).hora_inicio);
        holder.card_fecha_hora_fin.setText(list_horario.get(position).fecha+ " - "+ list_horario.get(position).hora_fin );

        holder.cardView.setOnLongClickListener(view -> {

            Fragment_Horario.alertDialog.crear_mensaje("¿Estás Seguro de Eliminar el horario?", "¡Esta acción no es reversible!", builder -> {
                builder.setPositiveButton("Aceptar", (dialogInterface, i) -> {
                    Fragment_Horario.dialog.mostrar_mensaje("Eliminando Horario...");
                    Fragment_Horario.ctlHorario.eliminar_horario(list_horario.get(position).uid);
                    Fragment_Horario.dialog.ocultar_mensaje();
                });
                builder.setNeutralButton("Cancelar", (dialogInterface, i) -> {});
                builder.setCancelable(false);
                builder.create().show();
            });

            return true;

        });

    }

    @Override
    public int getItemCount() {
        return list_horario.size();
    }



}
