package com.example.laboratorio_pdm_c2.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.laboratorio_pdm_c2.Entitys.Categoria;

import java.util.List;

@Dao
public interface CategoriaDao {

    @Insert
    long insertCategoria(Categoria categoria);

    @Query("SELECT * FROM categorias")
    List<Categoria> getAllCategoria();

    @Query("SELECT * FROM categorias WHERE idcategoria = :idcategoria")
    Categoria getCategoria(int idcategoria);

    @Update
    long updateCategoria(Categoria categoria);

    @Delete
    long deleteCategoria(Categoria categoria);


}