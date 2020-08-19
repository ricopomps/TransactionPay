package com.example.transactionpay.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties({"createdAt", "updatedAt", "__v"})
public class User implements Serializable {

    private String _id;
    private String cpf;
    private String name;
    private String avatar;
    private int telefone;
    private String pws;


    public User() {

    }

    public User(String _id, String cpf, String name, String avatar, int telefone, String pws) {
        this._id = _id;
        this.cpf = cpf;
        this.name = name;
        this.avatar = avatar;
        this.telefone = telefone;
        this.pws = pws;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getTelefone() {
        return telefone;
    }

    public void setTelefone(int telefone) {
        this.telefone = telefone;
    }

    public String getPws() {
        return pws;
    }

    public void setPws(String pws) {
        this.pws = pws;
    }
}
