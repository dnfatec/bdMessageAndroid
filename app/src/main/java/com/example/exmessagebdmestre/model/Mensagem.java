package com.example.exmessagebdmestre.model;

import java.util.Date;

public class Mensagem {
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }



    @Override
    public String toString() {
        return "Mensagem{" +
                "user='" + user + '\'' +
                ", mensagem='" + mensagem + '\'' +
                ", data=" + data +
                '}';
    }

    public Mensagem() {

    }

    private String user;
    private String mensagem;
    private Date data;
}
