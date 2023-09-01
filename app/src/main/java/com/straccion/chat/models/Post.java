package com.straccion.chat.models;

import java.util.Date;

public class Post {
    private String Titulo;
    private String Description;
    private String Imagen1;
    private String Imagen2;
    private String Categoria;
    private String idUser;
    private long Timestamp;

    public Post(){

    }
    public Post(String titulo, String description, String imagen1, String imagen2, String categoria, String idUser, long timestamp) {
        Titulo = titulo;
        Description = description;
        Imagen1 = imagen1;
        Imagen2 = imagen2;
        Categoria = categoria;
        this.idUser = idUser;
        Timestamp = timestamp;
    }
    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImagen1() {
        return Imagen1;
    }

    public void setImagen1(String imagen1) {
        Imagen1 = imagen1;
    }

    public String getImagen2() {
        return Imagen2;
    }

    public void setImagen2(String imagen2) {
        Imagen2 = imagen2;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public long getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(long timestamp) {
        Timestamp = timestamp;
    }
}
