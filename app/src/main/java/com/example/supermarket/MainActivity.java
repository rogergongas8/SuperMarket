package com.example.supermarket;

// Imports de Android y librerías
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

// Imports de Java
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Articulo> articulosDisponibles;
    private ArticulosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializarArticulos();
        RecyclerView recyclerView = findViewById(R.id.recycler_view_articulos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ArticulosAdapter(this, articulosDisponibles);
        recyclerView.setAdapter(adapter);
        Button irACestaBtn = findViewById(R.id.btn_ir_a_cesta);
        irACestaBtn.setOnClickListener(v -> navegarACesta());

    }

    private void inicializarArticulos() {
        articulosDisponibles = new ArrayList<>();

    }

    private void navegarACesta() {
        boolean hayArticulosSeleccionados = false;
        for (Articulo a : articulosDisponibles) {
            if (a.getCantidadComprada() > 0) {
                hayArticulosSeleccionados = true;
                break;
            }
        }

        if (hayArticulosSeleccionados) {
            Intent intent = new Intent(this, SegundaActivity.class);
            startActivity(intent);
        } else {
            // Context (this) usado para mostrar el Toast (Control 17)
            Toast.makeText(this, "Añade algo a la cesta antes de continuar.", Toast.LENGTH_SHORT).show();
        }
    }
}