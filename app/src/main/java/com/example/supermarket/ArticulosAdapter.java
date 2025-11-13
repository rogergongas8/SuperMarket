package com.example.supermarket;

// Imports de Android y librerías
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale; // Para formatear el precio

public class ArticulosAdapter extends RecyclerView.Adapter<ArticulosAdapter.ArticuloViewHolder> {

    private final List<Articulo> articulosList;

    public ArticulosAdapter(Context context, List<Articulo> articulosList) {
        this.articulosList = articulosList;
    }

    // 1. ViewHolder: Mantiene las referencias a las Views de cada fila
    public static class ArticuloViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenIV;
        TextView descripcionTV, cantidadTV, precioTotalTV, precioUnidadTV; // Añadido precioUnidadTV
        Button sumarBtn, restarBtn;

        public ArticuloViewHolder(@NonNull View itemView) {
            super(itemView);
            // Referencias a los IDs del layout item_articulo.xml
            imagenIV = itemView.findViewById(R.id.item_imagen);
            descripcionTV = itemView.findViewById(R.id.item_descripcion);
            precioUnidadTV = itemView.findViewById(R.id.item_precio_unidad); // Nueva referencia
            cantidadTV = itemView.findViewById(R.id.item_cantidad);
            precioTotalTV = itemView.findViewById(R.id.item_precio_total);
            sumarBtn = itemView.findViewById(R.id.item_btn_sumar);
            restarBtn = itemView.findViewById(R.id.item_btn_restar);
        }
    }

    // 2. onCreateViewHolder: Infla el Layout de la fila
    @NonNull
    @Override
    public ArticuloViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_articulo, parent, false);
        return new ArticuloViewHolder(view);
    }

    // 3. onBindViewHolder: Asigna los datos y la lógica a las Views
    @Override
    public void onBindViewHolder(@NonNull ArticuloViewHolder holder, int position) {
        Articulo articulo = articulosList.get(position);

        // Asignación de datos fijos
        holder.descripcionTV.setText(articulo.getDescripcion());
        holder.imagenIV.setImageResource(articulo.getImagenResourceId());
        holder.precioUnidadTV.setText(String.format(Locale.getDefault(), "%.2f€/ud", articulo.getPrecioUnidad()));

        // Asignación y actualización de datos dinámicos
        actualizarVistas(holder, articulo);

        // Listener para el botón Sumar (+)
        holder.sumarBtn.setOnClickListener(v -> {
            articulo.incrementarCantidad();
            actualizarVistas(holder, articulo);
        });

        // Listener para el botón Restar (-)
        holder.restarBtn.setOnClickListener(v -> {
            articulo.decrementarCantidad();
            actualizarVistas(holder, articulo);
        });
    }

    // Método auxiliar para actualizar los TextViews (Cantidad y Total)
    private void actualizarVistas(ArticuloViewHolder holder, Articulo articulo) {
        holder.cantidadTV.setText(String.valueOf(articulo.getCantidadComprada()));
        holder.precioTotalTV.setText(String.format(Locale.getDefault(), "%.2f€", articulo.getPrecioTotal()));
    }

    @Override
    public int getItemCount() {
        return articulosList.size();
    }
}