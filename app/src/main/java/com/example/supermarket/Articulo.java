package com.example.supermarket;

import java.io.Serializable;

public class Articulo implements Serializable {
    private String descripcion;
    private double precioUnidad;
    private int cantidadComprada;
    private int imagenResourceId;

    public Articulo(String descripcion, double precioUnidad, int imagenResourceId) {
        this.descripcion = descripcion;
        this.precioUnidad = precioUnidad;
        this.imagenResourceId = imagenResourceId;
        this.cantidadComprada = 0;
    }

    public String getDescripcion() { return descripcion; }
    public double getPrecioUnidad() { return precioUnidad; }
    public int getCantidadComprada() { return cantidadComprada; }
    public int getImagenResourceId() { return imagenResourceId; }

    public double getPrecioTotal() {
        return precioUnidad * cantidadComprada;
    }

    public void incrementarCantidad() {
        this.cantidadComprada++;
    }

    public void decrementarCantidad() {
        if (this.cantidadComprada > 0) {
            this.cantidadComprada--;
        }
    }
}