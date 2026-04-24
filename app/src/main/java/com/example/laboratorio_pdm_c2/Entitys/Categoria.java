package com.example.laboratorio_pdm_c2.Entitys;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categorias")
public class Categoria {

    @PrimaryKey(autoGenerate = true)
   public int idcategoria;
    public String  nombre;

}
