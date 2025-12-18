package com.example.supermarket;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

public class ArticulosAdapter extends RecyclerView.Adapter<ArticulosAdapter.ArticuloViewHolder> {

    private final List<Articulo> articulosList;
    private final Context context;
    private final Runnable alCambiarCantidad;

    public ArticulosAdapter(Context context, List<Articulo> articulosList, Runnable alCambiarCantidad) {
        this.context = context;
        this.articulosList = articulosList;
        this.alCambiarCantidad = alCambiarCantidad;
    }

    public static class ArticuloViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenIV;
        TextView descripcionTV, cantidadTV, precioTotalTV, precioUnidadTV;
        Button sumarBtn, restarBtn;

        public ArticuloViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenIV = itemView.findViewById(R.id.item_imagen);
            descripcionTV = itemView.findViewById(R.id.item_descripcion);
            precioUnidadTV = itemView.findViewById(R.id.item_precio_unidad);
            cantidadTV = itemView.findViewById(R.id.item_cantidad);
            precioTotalTV = itemView.findViewById(R.id.item_precio_total);
            sumarBtn = itemView.findViewById(R.id.item_btn_sumar);
            restarBtn = itemView.findViewById(R.id.item_btn_restar);
        }
    }

    @NonNull
    @Override
    public ArticuloViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_articulo, parent, false);
        return new ArticuloViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticuloViewHolder holder, int position) {
        Articulo articulo = articulosList.get(position);

        holder.descripcionTV.setText(articulo.getDescripcion());
        holder.precioUnidadTV.setText(String.format(Locale.getDefault(), "%.2f€/ud", articulo.getPrecioUnidad()));

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), articulo.getImagenResourceId());
        holder.imagenIV.setImageBitmap(bitmap);


        actualizarVistas(holder, articulo);

        holder.sumarBtn.setOnClickListener(v -> {
            articulo.incrementarCantidad();
            actualizarVistas(holder, articulo);
            if (alCambiarCantidad != null) alCambiarCantidad.run();
        });

        holder.restarBtn.setOnClickListener(v -> {
            articulo.decrementarCantidad();
            actualizarVistas(holder, articulo);
            if (alCambiarCantidad != null) alCambiarCantidad.run();
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalleActivity.class);
            intent.putExtra("detalle_articulo", articulo);
            context.startActivity(intent);
        });
    }

    private void actualizarVistas(ArticuloViewHolder holder, Articulo articulo) {
        holder.cantidadTV.setText(String.valueOf(articulo.getCantidadComprada()));
        holder.precioTotalTV.setText(String.format(Locale.getDefault(), "%.2f€", articulo.getPrecioTotal()));
    }

    @Override
    public int getItemCount() {
        return articulosList.size();
    }
}