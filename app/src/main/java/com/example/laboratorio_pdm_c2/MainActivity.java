package com.example.laboratorio_pdm_c2;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import com.example.laboratorio_pdm_c2.Entitys.Articulo;
import com.example.laboratorio_pdm_c2.Entitys.Categoria;
import com.example.laboratorio_pdm_c2.database.appDataBase;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        // 1. Obtener la instancia de la DB
        appDataBase db = appDataBase.getINSTANCE(this);

        // 2. Usar el executor que definiste en tu clase appDataBase
        appDataBase.databaseWriteExcecutor.execute(() -> {
            try {
                // --- PRUEBA DE CATEGORÍA ---
                Categoria cat = new Categoria();
                cat.nombre = "Electrónica";
                db.categoriaDao().insertCategoria(cat);

                // --- PRUEBA DE ARTÍCULO ---
                // Nota: Asegúrate de que el id de la categoría coincida
                // o que tu objeto Articulo esté bien configurado.
                Articulo art = new Articulo();
                art.nombre = "Cargador Tipo C";
                art.descripcion = "Cargador rápido 25W";
                art.idcategoria = 1; // Asumiendo que el ID generado fue 1

                db.articuloDao().insertArticulo(art);

                // --- CONSULTAR PARA VERIFICAR ---
                int totalArticulos = db.articuloDao().getAllArticulos().size();

                // Ver el resultado en el Logcat (Filtra por "NexusDB")
                Log.d("NexusDB", "Prueba exitosa. Artículos en DB: " + totalArticulos);

            } catch (Exception e) {
                Log.e("NexusDB", "Error en la base de datos: " + e.getMessage());
            }
        });


        setupViews();
        setupEvents();

        // Cargar fragment inicial (Artículos)
        if (savedInstanceState == null) {
            replaceFragment(new ArticulosFragment());
        }
    }



    private void setupViews(){

        navbar = findViewById(R.id.nav_bar);

    }


    private void setupEvents(){
        navbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                if (id == R.id.item_1) {
                    replaceFragment(new ArticulosFragment());
                    return true;
                } else if (id == R.id.item_2) {
                    replaceFragment(new PersonasFragment());
                    return true;
                } else if (id == R.id.item_3) {
                    replaceFragment(new PrestamoFragment());
                    return true;
                }

                return false;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
