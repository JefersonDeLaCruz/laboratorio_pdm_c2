package com.example.laboratorio_pdm_c2.Dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;



import java.util.List;

public interface PersonaDao {

    @Insert
    long insertPersona(PersonaDao persona);

    @Query("SELECT * FROM personas")
    List<PersonaDao> getAllPersona();

    @Query("SELECT * FROM personas WHERE idpersona = :idpersona")
    PersonaDao getPersona(int idpersona);


    @Update
    long updatePersona(PersonaDao persona);

    @Delete
    long deletePersona(PersonaDao persona);
}
