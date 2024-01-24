package com.jonathanaguilar.datareflix.Adaptadores;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jonathanaguilar.datareflix.Holders.Holder_tienda;
import com.jonathanaguilar.datareflix.Objetos.Ob_tienda;
import com.jonathanaguilar.datareflix.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_tienda extends RecyclerView.Adapter<Holder_tienda> {

    public List<Ob_tienda> list_tienda = new ArrayList<>();
    Context context;

    public Adapter_tienda(Context context) {
        this.context = context;
    }

    public void AddTienda (Ob_tienda tienda ){
        list_tienda.add(tienda);
        notifyItemInserted(list_tienda.size());
    }

    public void ClearTienda (){
        list_tienda.clear();
    }

    @NonNull
    @Override
    public Holder_tienda onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_tienda,parent,false);
        return new Holder_tienda(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder_tienda holder, int position) {

        holder.card_nombre.setText(list_tienda.get(position).nombre);
        holder.card_telefono.setText(list_tienda.get(position).telefono);
        holder.card_horas.setText(list_tienda.get(position).horas);
        holder.card_tipo.setText(list_tienda.get(position).tipo);

        if(list_tienda.get(position).url_foto != null){
            Glide.with(context).load(list_tienda.get(position).url_foto).centerCrop().into(holder.foto);
        }else{
            holder.foto.setImageResource(R.drawable.perfil);
        }


        holder.cardView.setOnLongClickListener(view -> {

            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            String text = list_tienda.get(position).telefono;
            ClipData clip = ClipData.newPlainText("telefono",  text);
            clipboard.setPrimaryClip(clip);

            Toast.makeText(context,"Telefono copiado al Portapapeles",Toast.LENGTH_SHORT).show();

            return true;
        });

    }

    @Override
    public int getItemCount() {
        return list_tienda.size();
    }



}
