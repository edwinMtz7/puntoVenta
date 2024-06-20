package com.frikirby.puntoventa.logica;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Usuario implements Serializable {
    
    @Id
    private int id_usuario;
    private String nombre;
    private String apellidoP;
    private String apellidoM;
    private String nombreUsuario;
    private String contrasenia;
    @ManyToOne
    @JoinColumn(name = "fk_rol")
    private Rol miRol;

    public Usuario() {
    }

    public Usuario(int id_usuario, String nombre, String apellidoP, String apellidoM, String nombreUsuario, String contrasenia, Rol miRol) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.miRol = miRol;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    public String getApellidoM() {
        return apellidoM;
    }

    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Rol getMiRol() {
        return miRol;
    }

    public void setMiRol(Rol miRol) {
        this.miRol = miRol;
    }
    
}
