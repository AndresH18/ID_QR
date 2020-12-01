package com.example.id_qr.data_models;

public class Recarga {

    private String valor;
    private String metodo;

    public Recarga() {
        //Required public Constructor
    }

    public Recarga(String valor, String metodo) {
        this.valor = valor;
        this.metodo = metodo;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
