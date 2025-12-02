package com.example.supermarket;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "supermercado.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CREATE =
            "CREATE TABLE articulos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "descripcion TEXT, " +
                    "precio REAL, " +
                    "imagen_res_id INTEGER)";

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
        // Array de datos para rellenar rápido
        Object[][] datos = {
                {"Naranja", 0.50, R.drawable.ic_launcher_foreground},
                {"Limón", 0.60, R.drawable.ic_launcher_foreground},
                {"Piña", 1.20, R.drawable.ic_launcher_foreground},
                {"Uvas", 2.50, R.drawable.ic_launcher_foreground},
                {"Ciruela", 0.40, R.drawable.ic_launcher_foreground},
                {"Melocotón", 0.80, R.drawable.ic_launcher_foreground},
                {"Sandía", 3.00, R.drawable.ic_launcher_foreground},
                {"Melón", 2.80, R.drawable.ic_launcher_foreground},
                {"Pera", 0.55, R.drawable.ic_launcher_foreground},
                {"Cereza", 4.00, R.drawable.ic_launcher_foreground}
        };

        for (Object[] dato : datos) {
            db.execSQL("INSERT INTO articulos (descripcion, precio, imagen_res_id) VALUES (?, ?, ?)", dato);
        }
    }
    public void borrarTodosLosArticulos() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM articulos");
        db.close();
    }
}