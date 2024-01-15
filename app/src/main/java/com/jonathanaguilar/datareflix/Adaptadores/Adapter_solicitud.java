package com.jonathanaguilar.datareflix.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jonathanaguilar.datareflix.Holders.Holder_solicitud;
import com.jonathanaguilar.datareflix.Objetos.Ob_solicitud;
import com.jonathanaguilar.datareflix.Principal;
import com.jonathanaguilar.datareflix.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_solicitud extends RecyclerView.Adapter<Holder_solicitud> {

    public List<Ob_solicitud> list_solicitud = new ArrayList<>();
    Context context;

    public Adapter_solicitud(Context context) {
        this.context = context;
    }

    public void AddSolicitud (Ob_solicitud obSolicitud ){
        list_solicitud.add(obSolicitud);
        notifyItemInserted(list_solicitud.size());
    }

    public void ClearSolicitud (){
        list_solicitud.clear();
    }

    @NonNull
    @Override
    public Holder_solicitud onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_solicitud,parent,false);
        return new Holder_solicitud(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder_solicitud holder, int position) {

        holder.card_fecha.setText(list_solicitud.get(position).fecha_solicitud);
        holder.card_tipo.setText(list_solicitud.get(position).tipo);
        holder.card_estado.setText(list_solicitud.get(position).estado);

        if(Principal.rol.equals("Administrador")) {
            holder.card_empleado.setVisibility(View.VISIBLE);
            holder.card_empleado.setText(list_solicitud.get(position).empleado);
        }else{
            holder.card_empleado.setVisibility(View.GONE);
            holder.card_empleado.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return list_solicitud.size();
    }



}
