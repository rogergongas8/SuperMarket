package com.example.supermarket;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SegundaActivity extends AppCompatActivity {

    private TextView tvTotal;
    private List<Articulo> listaCompra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        // Habilitar la flecha de volver atrás en la barra superior
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        listaCompra = (ArrayList<Articulo>) getIntent().getSerializableExtra("lista_compra");
        tvTotal = findViewById(R.id.tv_total_global);

        RecyclerView rv = findViewById(R.id.recycler_view_cesta);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // El adaptador maneja las imágenes automáticamente gracias al ID que pasamos desde Main
        ArticulosAdapter adapter = new ArticulosAdapter(this, listaCompra, () -> {
            calcularPrecioTotal(listaCompra);
        });

        rv.setAdapter(adapter);

        calcularPrecioTotal(listaCompra);
    }

    private void calcularPrecioTotal(List<Articulo> lista) {
        double total = 0;
        for (Articulo a : lista) {
            total += a.getPrecioTotal();
        }
        tvTotal.setText("TOTAL: " + String.format("%.2f", total) + "€");
    }

    // --- AÑADIDO: Lógica del Menú ---
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem itemCesta = menu.findItem(R.id.action_ver_cesta);
        if (itemCesta != null) {
            itemCesta.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // Flecha de atrás
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_refresh) {
            Toast.makeText(this, "La cesta ya está actualizada", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.action_salir) {
            // Cierra toda la app
            finishAffinity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}