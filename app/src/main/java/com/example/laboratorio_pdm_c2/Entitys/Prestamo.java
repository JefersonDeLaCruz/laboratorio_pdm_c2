package com.example.laboratorio_pdm_c2.Entitys;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "prestamos",
        foreignKeys = {
                @ForeignKey(entity = Articulo.class, parentColumns = "idarticulo", childColumns = "idarticulo", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Persona.class, parentColumns = "idpersona", childColumns = "idpersona", onDelete = ForeignKey.CASCADE)
        })
public class Prestamo {
    @PrimaryKey(autoGenerate = true)
    public int idprestamo;
    public int idarticulo;
    public int idpersona;
    public Date fechaPrestamo;
    public Date fechaDevolucionEstimada;
    public boolean devuelto = false;
}