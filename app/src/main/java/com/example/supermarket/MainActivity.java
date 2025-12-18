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

    // Asegúrate de que esta IP es correcta (10.0.2.2 para emulador)
    private static final String URL_API = "http://10.0.2.2/supermercado/obtener_articulos.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarArticulos();

        RecyclerView recyclerView = findViewById(R.id.recycler_view_articulos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ArticulosAdapter(this, articulosDisponibles, null);
        recyclerView.setAdapter(adapter);

        Button irACestaBtn = findViewById(R.id.btn_ir_a_cesta);
        irACestaBtn.setOnClickListener(v -> navegarACesta());
    }

    private void inicializarArticulos() {
        // Si la lista ya existe, la limpiamos. Si no, la creamos.
        if (articulosDisponibles == null) {
            articulosDisponibles = new ArrayList<>();
        } else {
            articulosDisponibles.clear();
        }

        if (hayInternet()) {
            descargarDatosDelServidor();
        } else {
            Toast.makeText(this, "Modo Offline (SQLite)", Toast.LENGTH_SHORT).show();
            cargarDesdeSQLite();
        }
    }

    private boolean hayInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private void descargarDatosDelServidor() {
        // 1. LIMPIEZA AGRESIVA DE CACHÉ
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        queue.getCache().clear();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_API, null,
                response -> {
                    AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(this);

                    // Borramos datos viejos
                    dbHelper.borrarTodosLosArticulos();

                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    // Usamos una transacción para que sea más rápido y seguro
                    db.beginTransaction();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            String desc = jsonObject.getString("descripcion");
                            double precio = jsonObject.getDouble("precio");

                            String nombreImagenClean = limpiarNombre(desc);

                            db.execSQL("INSERT INTO articulos (descripcion, precio, imagen_nombre) VALUES (?, ?, ?)",
                                    new Object[]{desc, precio, nombreImagenClean});
                        }
                        db.setTransactionSuccessful();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        db.endTransaction();
                        db.close();
                    }

                    cargarDesdeSQLite();
                },
                error -> {
                    Toast.makeText(this, "Error conexión. Usando datos locales.", Toast.LENGTH_LONG).show();
                    cargarDesdeSQLite();
                }
        );

        request.setShouldCache(false);

        queue.add(request);
    }

    private void cargarDesdeSQLite() {
        articulosDisponibles.clear();
        AdminSQLiteOpenHelper dbHelper = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM articulos", null);
        Toast.makeText(this, "Productos en BD: " + cursor.getCount(), Toast.LENGTH_SHORT).show();
        if (cursor.moveToFirst()) {
            do {
                String desc = cursor.getString(1);
                double precio = cursor.getDouble(2);
                String nombreImgTexto = cursor.getString(3);

                int imgId = getResources().getIdentifier(nombreImgTexto, "drawable", getPackageName());
                if (imgId == 0) {
                    imgId = R.drawable.ic_launcher_foreground;
                }

                articulosDisponibles.add(new Articulo(desc, precio, imgId));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    private String limpiarNombre(String nombre) {
        if (nombre == null) return "ic_launcher_foreground";
        return nombre.toLowerCase()
                .replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u")
                .replace("ñ", "n").replace(" ", "_");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            inicializarArticulos();
            return true;
        } else if (id == R.id.action_ver_cesta) {
            navegarACesta();
            return true;
        } else if (id == R.id.action_perfil) {
            startActivity(new Intent(this, PerfilActivity.class));
            return true;
        } else if (id == R.id.action_salir) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}