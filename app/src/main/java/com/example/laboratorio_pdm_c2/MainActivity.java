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
    private static final String KEY_TITLE = "header_title";
    private static final String KEY_SUBTITLE = "header_subtitle";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        setupViews();
        setupEvents();

        if (savedInstanceState != null) {
            //restaurar textos tras cambio de tema/configuracion
            tvTitle.setText(savedInstanceState.getString(KEY_TITLE));
            tvSubtitle.setText(savedInstanceState.getString(KEY_SUBTITLE));
        } else {
            //cargar fragment inicial (Artículos) por primera vez
            updateHeader("Gestión de Artículos", "Administra el inventario de recursos disponibles");
            replaceFragment(new ArticulosFragment());
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //guardar el estado actual de los textos
        outState.putString(KEY_TITLE, tvTitle.getText().toString());
        outState.putString(KEY_SUBTITLE, tvSubtitle.getText().toString());
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
