package com.example.id_qr.data_models;

public class Recarga {

    private String valor;
    private String metodo;
    private String tipo;

    public Recarga() {
        //Required public Constructor
    }

    public Recarga(String valor, String metodo, String tipo) {
        this.valor = valor;
        this.metodo = metodo;
        this.tipo = tipo;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
