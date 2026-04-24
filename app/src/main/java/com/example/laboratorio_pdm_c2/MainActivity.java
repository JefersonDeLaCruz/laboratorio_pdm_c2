package com.example.laboratorio_pdm_c2;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

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

        setupViews();
        setupEvents();
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
                    Toast.makeText(MainActivity.this, "Artículos seleccionado", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.item_2) {
                    Toast.makeText(MainActivity.this, "Personas seleccionado", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.item_3) {
                    Toast.makeText(MainActivity.this, "Préstamos seleccionado", Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            }
        });
    }
}
