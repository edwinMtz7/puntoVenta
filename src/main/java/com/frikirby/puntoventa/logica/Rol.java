package com.frikirby.puntoventa.logica;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Rol implements Serializable {
    
    @Id
    private int id_rol;
    private String nombre;
    @OneToMany (mappedBy = "miRol")
    private List<Usuario> usuarios;

    public Rol() {
    }

    public Rol(int id_rol, String nombre, List<Usuario> usuarios) {
        this.id_rol = id_rol;
        this.nombre = nombre;
        this.usuarios = usuarios;
    }

    public int getId_rol() {
        return id_rol;
    }

    public void setId_rol(int id_rol) {
        this.id_rol = id_rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    
}
