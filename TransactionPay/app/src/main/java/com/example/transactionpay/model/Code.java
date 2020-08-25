package com.example.transactionpay.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties("mensagem")
public class Code implements Serializable {
    private String codigo_de_barras;


    public Code() {
    }

    public Code(String codigo_de_barras) {
        this.codigo_de_barras = codigo_de_barras;
    }

    public String getCodigo_de_barras() {
        return codigo_de_barras;
    }

    public void setCodigo_de_barras(String codigo_de_barras) {
        this.codigo_de_barras = codigo_de_barras;
    }
}
