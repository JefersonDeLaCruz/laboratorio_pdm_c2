package com.example.laboratorio_pdm_c2.Entitys;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "personas")
public class Persona {
    @PrimaryKey(autoGenerate = true)
    public int idpersona;
    public String nombre;
    public String apellido;
    public String contacto;
    public String direccion;
    public boolean activo = true; // Campo para borrado lógico
}