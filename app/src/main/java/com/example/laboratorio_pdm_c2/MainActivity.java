package com.example.laboratorio_pdm_c2;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navbar;
    private TextView tvTitle, tvSubtitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Eliminamos el padding bottom del listener para que la BottomNavigationView maneje su propio espacio o quede pegada
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        setupViews();
        setupEvents();

        // Cargar fragment inicial (Artículos)
        if (savedInstanceState == null) {
            updateHeader("Gestión de Artículos", "Administra el inventario de recursos disponibles");
            replaceFragment(new ArticulosFragment());
        }
    }



    private void setupViews(){
        navbar = findViewById(R.id.nav_bar);
        tvTitle = findViewById(R.id.tvMainTitle);
        tvSubtitle = findViewById(R.id.tvMainSubtitle);
    }


    private void setupEvents(){
        navbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                if (id == R.id.item_1) {
                    updateHeader("Gestión de Artículos", "Administra el inventario de recursos disponibles");
                    replaceFragment(new ArticulosFragment());
                    return true;
                } else if (id == R.id.item_2) {
                    updateHeader("Gestión de Personas", "Registro y control de usuarios del sistema");
                    replaceFragment(new fragment_persona());
                    return true;
                } else if (id == R.id.item_3) {
                    updateHeader("Gestión de Préstamos", "Control de préstamos activos y devoluciones");
                    replaceFragment(new PrestamoFragment());
                    return true;
                }

                return false;
            }
        });
    }

    private void updateHeader(String title, String subtitle) {
        if (tvTitle != null && tvSubtitle != null) {
            tvTitle.setText(title);
            tvSubtitle.setText(subtitle);
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
