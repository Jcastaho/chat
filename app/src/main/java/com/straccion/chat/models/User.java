package com.straccion.chat.models;

public class User {

    private String id;
    private String Correo;
    private String NombreUsuario;
    private String Telefono;
    private String ImageProfile;
    private String ImageCover;
    private long Timestamp;


    public User(){

    }

    public User(String id, String correo, String nombreUsuario, String telefono, long timestamp, String imageProfile, String imageCover) {
        this.id = id;
        Correo = correo;
        NombreUsuario = nombreUsuario;
        Telefono = telefono;
        Timestamp = timestamp;
        ImageProfile = imageProfile;
        ImageCover = imageCover;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getNombreUsuario() {
        return NombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        NombreUsuario = nombreUsuario;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public long getTimestamp() {return Timestamp; }

    public void setTimestamp(long timestamp) {
        Timestamp = timestamp;
    }

    public String getImageProfile() {
        return ImageProfile;
    }

    public void setImageProfile(String imageProfile) {
        ImageProfile = imageProfile;
    }

    public String getImageCover() {
        return ImageCover;
    }

    public void setImageCover(String imageCover) {
        ImageCover = imageCover;
    }
}
