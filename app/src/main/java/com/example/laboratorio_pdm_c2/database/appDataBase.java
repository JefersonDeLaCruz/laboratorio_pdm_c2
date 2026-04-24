package com.example.laboratorio_pdm_c2.database;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.laboratorio_pdm_c2.Convertions.Converter;
import com.example.laboratorio_pdm_c2.Dao.ArticuloDao;
import com.example.laboratorio_pdm_c2.Dao.CategoriaDao;
import com.example.laboratorio_pdm_c2.Dao.PersonaDao;
import com.example.laboratorio_pdm_c2.Dao.PrestamoDao;
import com.example.laboratorio_pdm_c2.Entitys.Articulo;
import com.example.laboratorio_pdm_c2.Entitys.Categoria;
import com.example.laboratorio_pdm_c2.Entitys.Persona;
import com.example.laboratorio_pdm_c2.Entitys.Prestamo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Categoria.class, Articulo.class, Persona.class, Prestamo.class}, version = 3, exportSchema = true)
@TypeConverters({Converter.class})
public abstract class appDataBase extends RoomDatabase {

    public abstract CategoriaDao categoriaDao();
    public abstract ArticuloDao articuloDao();
    public abstract PersonaDao personaDao();
    public abstract PrestamoDao prestamoDao();

    private static volatile appDataBase INSTANCE;
    public static final ExecutorService databaseWriteExcecutor = Executors.newFixedThreadPool(4);

    // Migración de la versión 1 a la 2: Agregando campos apellido y direccion a la tabla personas
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE personas ADD COLUMN apellido TEXT");
            database.execSQL("ALTER TABLE personas ADD COLUMN direccion TEXT");
        }
    };

    // Migración de la versión 2 a la 3: Agregando campo activo para borrado lógico
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE personas ADD COLUMN activo INTEGER NOT NULL DEFAULT 1");
        }
    };

    public static appDataBase getINSTANCE(Context context) {
        if (INSTANCE == null) {
            synchronized (appDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            appDataBase.class,
                            "nexus_db"
                    )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .fallbackToDestructiveMigrationOnDowngrade()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
