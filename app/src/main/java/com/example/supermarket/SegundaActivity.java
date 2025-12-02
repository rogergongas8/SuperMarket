package com.example.supermarket;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SegundaActivity extends AppCompatActivity {

    private TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda);

        List<Articulo> listaCompra = (ArrayList<Articulo>) getIntent().getSerializableExtra("lista_compra");
        tvTotal = findViewById(R.id.tv_total_global);

        RecyclerView rv = findViewById(R.id.recycler_view_cesta);
        rv.setLayoutManager(new LinearLayoutManager(this));

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
}