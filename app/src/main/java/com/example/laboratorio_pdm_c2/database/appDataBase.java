package com.example.laboratorio_pdm_c2.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.laboratorio_pdm_c2.Convertions.Converter;
import com.example.laboratorio_pdm_c2.Dao.ArticuloDao;
import com.example.laboratorio_pdm_c2.Dao.CategoriaDao;
import com.example.laboratorio_pdm_c2.Dao.PersonaDao;
import com.example.laboratorio_pdm_c2.Dao.PrestamoDao;
import com.example.laboratorio_pdm_c2.Entitys.Articulo;
import com.example.laboratorio_pdm_c2.Entitys.Categoria;
import com.example.laboratorio_pdm_c2.Entitys.Persona;
import com.example.laboratorio_pdm_c2.Entitys.Prestamo;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kotlin.jvm.Synchronized;

@Database(entities = {Categoria.class, Articulo.class, Persona.class, Prestamo.class}, version = 1,exportSchema = true)
@TypeConverters({Converter.class})
public abstract class appDataBase  extends RoomDatabase {

    public abstract CategoriaDao categoriaDao();
    public abstract ArticuloDao articuloDao();
    public abstract PersonaDao personaDao();
    public abstract PrestamoDao prestamoDao();


    private static volatile appDataBase INSTANCE;

    public static final ExecutorService databaseWriteExcecutor = Executors.newFixedThreadPool(4);

    public  static appDataBase getINSTANCE(Context context) {

        if (INSTANCE == null) {
            synchronized (appDataBase.class) {
                INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        appDataBase.class,
                        "nexus_db"
                ).build();
            }
        }

        return INSTANCE;
    }
}
