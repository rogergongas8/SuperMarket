package com.example.supermarket;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "supermercado.db";
    // Subimos a la versión 4 para forzar el onUpgrade
    private static final int DATABASE_VERSION = 5;

    private static final String TABLE_CREATE =
            "CREATE TABLE articulos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "descripcion TEXT, " +
                    "precio REAL, " +
                    "imagen_nombre TEXT)";

    public AdminSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        insertarDatosIniciales(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS articulos");
        onCreate(db);
    }

    private void insertarDatosIniciales(SQLiteDatabase db) {
        Object[][] datos = {
                {"Naranja", 0.50, "naranja"},
                {"Limón", 0.60, "limon"},
                {"Piña", 1.20, "pina"},
                {"Uvas", 2.50, "uvas"},
                {"Ciruela", 0.40, "ciruela"},
                {"Melocotón", 0.80, "melocoton"},
                {"Sandía", 3.00, "sandia"},
                {"Melón", 2.80, "melon"},
                {"Pera", 0.55, "pera"},
                {"Cereza", 4.00, "cereza"},
                {"Fresa", 3.50, "fresa"},
                {"Plátano", 1.10, "platano"},
                {"Manzana", 1.50, "manzana"},
                {"Kiwi", 2.50, "kiwi"},
        };

        for (Object[] dato : datos) {
            db.execSQL("INSERT INTO articulos (descripcion, precio, imagen_nombre) VALUES (?, ?, ?)", dato);
        }
    }

    public void borrarTodosLosArticulos() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM articulos");
        db.close();
    }
}