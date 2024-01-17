package com.jonathanaguilar.datareflix.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jonathanaguilar.datareflix.Holders.Holder_marcacion;
import com.jonathanaguilar.datareflix.Objetos.Ob_marcacion;
import com.jonathanaguilar.datareflix.Principal;
import com.jonathanaguilar.datareflix.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_marcacion extends RecyclerView.Adapter<Holder_marcacion> {

    public List<Ob_marcacion> list_marcacion = new ArrayList<>();
    Context context;

    public Adapter_marcacion(Context context) {
        this.context = context;
    }

    public void AddMarcacion (Ob_marcacion marcacion ){
        list_marcacion.add(marcacion);
        notifyItemInserted(list_marcacion.size());
    }

    public void ClearMarcacion (){
        list_marcacion.clear();
    }

    @NonNull
    @Override
    public Holder_marcacion onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_marcacion,parent,false);
        return new Holder_marcacion(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder_marcacion holder, int position) {

        holder.card_fecha_hora.setText(list_marcacion.get(position).fecha_hora);

        if(Principal.rol.equals("Administrador")) {
            holder.card_empleado.setVisibility(View.VISIBLE);
            holder.card_empleado.setText(list_marcacion.get(position).empleado);
        }else{
            holder.card_empleado.setVisibility(View.GONE);
            holder.card_empleado.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return list_marcacion.size();
    }



}
