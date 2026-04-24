package com.example.laboratorio_pdm_c2.Entitys;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "articulos",
        foreignKeys = @ForeignKey(
                entity = Categoria.class,
                parentColumns = "idcategoria",
                childColumns = "idcategoria",
                onDelete = ForeignKey.RESTRICT,
                onUpdate = ForeignKey.NO_ACTION
        ))
public class Articulo {
    @PrimaryKey(autoGenerate = true)
    public int idarticulo;
    public String nombre;
    public String descripcion;
    public int idcategoria;
    public boolean esPrestado = false;
}