package com.example.id_qr.data_models;

import com.google.firebase.database.ServerValue;

public class Pago {

    private String tipo;

    public Pago(){
        //Required Empty Constructor
    }

    public Pago(String tipo){
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
