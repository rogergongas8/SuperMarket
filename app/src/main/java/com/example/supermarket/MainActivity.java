package com.example.supermarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Articulo> articulosDisponibles;
    private ArticulosAdapter adapter;

    // ¡OJO! CAMBIA ESTA IP POR LA TUYA DE VERDAD (ej: 192.168.1.35)
    // Si usas emulador, deja 10.0.2.2
    private static final String URL_API = "http://10.0.2.2/supermercado/obtener_articulos.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarArticulos();

        RecyclerView recyclerView = findViewById(R.id.recycler_view_articulos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Pasamos 'null' porque aquí no necesitamos recalcular totales globales
        adapter = new ArticulosAdapter(this, articulosDisponibles, null);
        recyclerView.setAdapter(adapter);

        Button irACestaBtn = findViewById(R.id.btn_ir_a_cesta);
        irACestaBtn.setOnClickListener(v -> navegarACesta());
    }

    private void inicializarArticulos() {
        articulosDisponibles = new ArrayList<>();
        if (hayInternet()) {
            descargarDatosDelServidor();
        } else {
            Toast.makeText(this, "Modo Offline", Toast.LENGTH_SHORT).show();
            cargarDesdeSQLite();
        }
    }

    private boolean hayInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private void descargarDatosDelServidor() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_API, null,
                response -> {
                    AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(this);
                    dbHelper.borrarTodosLosArticulos();
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            String desc = jsonObject.getString("descripcion");
                            double precio = jsonObject.getDouble("precio");
                            int imagenId = R.drawable.ic_launcher_foreground; // Imagen por defecto

                            db.execSQL("INSERT INTO articulos (descripcion, precio, imagen_res_id) VALUES (?, ?, ?)",
                                    new Object[]{desc, precio, imagenId});
                        }
                        db.close();
                        cargarDesdeSQLite();
                        Toast.makeText(this, "Datos actualizados", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error XAMPP: " + error.toString(), Toast.LENGTH_LONG).show();
                    cargarDesdeSQLite();
                }
        );
        Volley.newRequestQueue(this).add(request);
    }

    private void cargarDesdeSQLite() {
        articulosDisponibles.clear();
        AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM articulos", null);

        if (cursor.moveToFirst()) {
            do {
                String desc = cursor.getString(1);
                double precio = cursor.getDouble(2);
                int imgId = cursor.getInt(3);
                articulosDisponibles.add(new Articulo(desc, precio, imgId));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    private void navegarACesta() {
        ArrayList<Articulo> cesta = new ArrayList<>();
        for (Articulo a : articulosDisponibles) {
            if (a.getCantidadComprada() > 0) cesta.add(a);
        }

        if (!cesta.isEmpty()) {
            Intent intent = new Intent(this, SegundaActivity.class);
            intent.putExtra("lista_compra", cesta);
            startActivity(intent);
        } else {
            Toast.makeText(this, "La cesta está vacía", Toast.LENGTH_SHORT).show();
        }
    }

    // Menú de opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            inicializarArticulos();
            return true;
        } else if (item.getItemId() == R.id.action_ver_cesta) {
            navegarACesta();
            return true;
        } else if (item.getItemId() == R.id.action_salir) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}