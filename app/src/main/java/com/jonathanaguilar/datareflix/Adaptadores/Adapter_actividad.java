package com.jonathanaguilar.datareflix.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jonathanaguilar.datareflix.Actividades.Det_actividad;
import com.jonathanaguilar.datareflix.Holders.Holder_actividad;
import com.jonathanaguilar.datareflix.Objetos.Ob_actividad;
import com.jonathanaguilar.datareflix.Principal;
import com.jonathanaguilar.datareflix.R;
import com.jonathanaguilar.datareflix.Solicitudes.Det_solicitud;

import java.util.ArrayList;
import java.util.List;

public class Adapter_actividad extends RecyclerView.Adapter<Holder_actividad> {

    public List<Ob_actividad> list_actividad = new ArrayList<>();
    Context context;

    public Adapter_actividad(Context context) {
        this.context = context;
    }

    public void AddActividad (Ob_actividad obActividad ){
        list_actividad.add(obActividad);
        notifyItemInserted(list_actividad.size());
    }

    public void ClearActividad (){
        list_actividad.clear();
    }

    @NonNull
    @Override
    public Holder_actividad onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_actividades,parent,false);
        return new Holder_actividad(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder_actividad holder, int position) {

        holder.card_fecha.setText(list_actividad.get(position).fecha_inicio);
        holder.card_tipo.setText(list_actividad.get(position).tipo);
        holder.card_estado.setText(list_actividad.get(position).estado);

        if(list_actividad.get(position).estado!=null){
            switch (list_actividad.get(position).estado.toLowerCase()){
                case "pendiente":
                    holder.card_estado.setTextColor(ContextCompat.getColor(context,R.color.warning));
                    break;
                case "finalizado":
                    holder.card_estado.setTextColor(ContextCompat.getColor(context,R.color.success));
                    break;
                default:
                    holder.card_estado.setTextColor(ContextCompat.getColor(context,R.color.proyecto_night));
                    break;
            }
        }

        if(Principal.rol.equals("Administrador")) {
            holder.card_empleado.setVisibility(View.VISIBLE);
            holder.card_empleado.setText(list_actividad.get(position).empleado);
        }else{
            holder.card_empleado.setVisibility(View.GONE);
            holder.card_empleado.setText("");
        }

        holder.cardView.setOnClickListener(view -> {

            Intent i = new Intent();
            i.setClass(context, Det_actividad.class);
            i.putExtra("uid",list_actividad.get(position).uid);
            i.putExtra("uid_empleado",list_actividad.get(position).uid_empleado);
            i.putExtra("ced_empleado",list_actividad.get(position).ced_empleado);
            i.putExtra("nom_empleado",list_actividad.get(position).empleado);
            context.startActivity(i);

        });

    }

    @Override
    public int getItemCount() {
        return list_actividad.size();
    }



}
