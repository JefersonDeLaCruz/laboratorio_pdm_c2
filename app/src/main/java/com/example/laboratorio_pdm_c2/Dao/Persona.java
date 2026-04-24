package com.example.laboratorio_pdm_c2.Dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;



import java.util.List;

public interface Persona {

    @Insert
    long insertPersona(Persona persona);

    @Query("SELECT * FROM personas")
    List<Persona> getAllPersona();

    @Query("SELECT * FROM personas WHERE idpersona = :idpersona")
    Persona getPersona(int idpersona);

    @Update
    long updatePersona(Persona persona);

    @Delete
    long deletePersona(Persona persona);
}
