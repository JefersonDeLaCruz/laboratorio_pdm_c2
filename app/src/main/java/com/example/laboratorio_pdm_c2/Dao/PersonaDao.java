package com.example.laboratorio_pdm_c2.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.laboratorio_pdm_c2.Entitys.Persona;

import java.util.List;

@Dao
public interface PersonaDao {

    @Insert
    long insertPersona(Persona persona);

    @Query("SELECT * FROM personas")
    List<Persona> getAllPersona();

    @Query("SELECT * FROM personas WHERE idpersona = :idpersona")
    Persona getPersona(int idpersona);

    @Update
    int updatePersona(Persona persona);

    @Delete
    int deletePersona(Persona persona);
}
