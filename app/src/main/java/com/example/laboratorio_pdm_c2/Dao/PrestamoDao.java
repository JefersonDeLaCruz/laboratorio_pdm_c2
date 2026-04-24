package com.example.laboratorio_pdm_c2.Dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.laboratorio_pdm_c2.Entitys.Prestamo;

import java.util.List;

public interface PrestamoDao {
    @Insert
    long insertPrestamo(Prestamo Prestamo);

    @Query("SELECT * FROM Prestamos")
    List<Prestamo> getAllPrestamo();

    @Query("SELECT * FROM Prestamos WHERE idPrestamo = :idPrestamo")
    Prestamo getPrestamo(int idPrestamo);

    @Update
    long updatePrestamo(Prestamo Prestamo);

    @Delete
    long deletePrestamo(Prestamo Prestamo);
}
