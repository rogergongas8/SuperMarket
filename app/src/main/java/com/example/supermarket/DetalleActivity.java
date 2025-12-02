package com.example.supermarket;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetalleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        Articulo articulo = (Articulo) getIntent().getSerializableExtra("detalle_articulo");

        ImageView img = findViewById(R.id.img_detalle);
        TextView titulo = findViewById(R.id.tv_titulo_detalle);
        TextView precio = findViewById(R.id.tv_precio_detalle);
        TextView descLarga = findViewById(R.id.tv_desc_larga);
        Button volver = findViewById(R.id.btn_volver);

        if (articulo != null) {
            titulo.setText(articulo.getDescripcion());
            precio.setText(articulo.getPrecioUnidad() + "€");
            img.setImageResource(articulo.getImagenResourceId());
            descLarga.setText("Detalles increíbles del producto " + articulo.getDescripcion());
        }

        volver.setOnClickListener(v -> finish());
    }
}