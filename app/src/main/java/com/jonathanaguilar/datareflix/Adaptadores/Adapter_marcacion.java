package com.jonathanaguilar.datareflix.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jonathanaguilar.datareflix.Holders.Holder_marcacion;
import com.jonathanaguilar.datareflix.Marcacion.Det_marcacion;
import com.jonathanaguilar.datareflix.Objetos.Ob_marcacion;
import com.jonathanaguilar.datareflix.Principal;
import com.jonathanaguilar.datareflix.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        String fecha_hora = list_marcacion.get(position).fecha_hora;
        if(!fecha_hora.contains("/")) {
            fecha_hora =  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date(Long.parseLong(fecha_hora)));
        }
        holder.card_fecha_hora.setText(fecha_hora);
        holder.card_tipo.setText(list_marcacion.get(position).tipo);
        holder.card_estado.setText(list_marcacion.get(position).estado);

        if(list_marcacion.get(position).estado!=null){
            switch (list_marcacion.get(position).estado.toLowerCase()){
                case "asistencia":
                case "salida":
                    holder.card_estado.setTextColor(ContextCompat.getColor(context,R.color.success));
                    break;
                case "atraso":
                case "salida temprano":
                    holder.card_estado.setTextColor(ContextCompat.getColor(context,R.color.danger));
                    break;
                case "horas extras":
                    holder.card_estado.setTextColor(ContextCompat.getColor(context,R.color.warning));
                    break;
                case "permiso laboral":
                    holder.card_estado.setTextColor(ContextCompat.getColor(context,R.color.gray));
                    break;
                default:
                    holder.card_estado.setTextColor(ContextCompat.getColor(context,R.color.black));
                    break;
            }
        }

        if(Principal.rol.equals("Administrador")) {
            holder.card_empleado.setVisibility(View.VISIBLE);
            holder.card_empleado.setText(list_marcacion.get(position).empleado);
        }else{
            holder.card_empleado.setVisibility(View.GONE);
            holder.card_empleado.setText("");
        }

        holder.cardView.setOnClickListener(view -> {
            Intent i = new Intent();
            i.setClass(context, Det_marcacion.class);
            i.putExtra("uid",list_marcacion.get(position).uid);
            i.putExtra("uid_empleado",list_marcacion.get(position).uid_empleado);
            i.putExtra("empleado",list_marcacion.get(position).empleado);
            i.putExtra("fecha_hora",list_marcacion.get(position).fecha_hora);
            context.startActivity(i);


        });

    }

    @Override
    public int getItemCount() {
        return list_marcacion.size();
    }



}
