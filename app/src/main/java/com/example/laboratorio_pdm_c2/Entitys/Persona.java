package com.example.laboratorio_pdm_c2.Entitys;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "personas")
public class Persona {
    @PrimaryKey(autoGenerate = true)
    public int idpersona;
    public String nombre;
    public String contacto;
}