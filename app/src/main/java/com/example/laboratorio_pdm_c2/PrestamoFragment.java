package com.example.laboratorio_pdm_c2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laboratorio_pdm_c2.Entitys.Articulo;
import com.example.laboratorio_pdm_c2.Entitys.Persona;
import com.example.laboratorio_pdm_c2.Entitys.Prestamo;
import com.example.laboratorio_pdm_c2.database.appDataBase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PrestamoFragment extends Fragment {

    private RecyclerView rvPrestamos;
    private PrestamoAdapter adapter;
    private appDataBase db;
    private List<Articulo> articulosDisponibles = new ArrayList<>();
    private List<Persona> personasList = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public PrestamoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prestamo, container, false);

        db = appDataBase.getINSTANCE(getContext());
        rvPrestamos = view.findViewById(R.id.rvPrestamos);
        FloatingActionButton fabAddPrestamo = view.findViewById(R.id.fabAddPrestamo);

        adapter = new PrestamoAdapter();
        rvPrestamos.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPrestamos.setAdapter(adapter);

        adapter.setOnDevolverClickListener(this::handleDevolucion);

        fabAddPrestamo.setOnClickListener(v -> showAddPrestamoDialog());

        loadPrestamos();
        loadSpinnersData();

        return view;
    }

    private void loadPrestamos() {
        appDataBase.databaseWriteExcecutor.execute(() -> {
            List<Prestamo> prestamos = db.prestamoDao().getAllPrestamo();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> adapter.setPrestamos(prestamos));
            }
        });
    }

    private void loadSpinnersData() {
        appDataBase.databaseWriteExcecutor.execute(() -> {
            // Solo artículos que NO estén prestados
            articulosDisponibles = db.articuloDao().getAllArticulos(); // Idealmente filtrar por esPrestado = false
            personasList = db.personaDao().getAllPersona();
        });
    }

    private void handleDevolucion(Prestamo prestamo) {
        prestamo.devuelto = true;
        appDataBase.databaseWriteExcecutor.execute(() -> {
            db.prestamoDao().updatePrestamo(prestamo);
            
            // Marcar el artículo como disponible de nuevo
            Articulo articulo = db.articuloDao().getArticulo(prestamo.idarticulo);
            if (articulo != null) {
                articulo.esPrestado = false;
                db.articuloDao().updateArticulo(articulo);
            }
            
            loadPrestamos();
            loadSpinnersData();
        });
        Toast.makeText(getContext(), "Artículo devuelto", Toast.LENGTH_SHORT).show();
    }

    private void showAddPrestamoDialog() {
        // Filtrar artículos realmente disponibles para el spinner
        List<Articulo> disponibles = new ArrayList<>();
        for (Articulo a : articulosDisponibles) {
            if (!a.esPrestado) disponibles.add(a);
        }

        if (disponibles.isEmpty() || personasList.isEmpty()) {
            Toast.makeText(getContext(), "Necesitas artículos disponibles y personas registradas", Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_prestamo, null);
        builder.setView(dialogView);

        Spinner spinnerArticulos = dialogView.findViewById(R.id.spinnerArticulos);
        Spinner spinnerPersonas = dialogView.findViewById(R.id.spinnerPersonas);
        TextInputEditText etFecha = dialogView.findViewById(R.id.etFechaDevolucion);

        // Configurar Spinner Artículos
        List<String> nombresArticulos = new ArrayList<>();
        for (Articulo a : disponibles) nombresArticulos.add(a.nombre);
        ArrayAdapter<String> artAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nombresArticulos);
        artAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArticulos.setAdapter(artAdapter);

        // Configurar Spinner Personas
        List<String> nombresPersonas = new ArrayList<>();
        for (Persona p : personasList) nombresPersonas.add(p.nombre);
        ArrayAdapter<String> perAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nombresPersonas);
        perAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPersonas.setAdapter(perAdapter);

        builder.setTitle("Registrar Nuevo Préstamo")
                .setPositiveButton("Registrar", (dialog, which) -> {
                    int artPos = spinnerArticulos.getSelectedItemPosition();
                    int perPos = spinnerPersonas.getSelectedItemPosition();
                    String fechaStr = etFecha.getText().toString();

                    try {
                        Prestamo nuevo = new Prestamo();
                        nuevo.idarticulo = disponibles.get(artPos).idarticulo;
                        nuevo.idpersona = personasList.get(perPos).idpersona;
                        nuevo.fechaPrestamo = new Date(); // Hoy
                        nuevo.fechaDevolucionEstimada = dateFormat.parse(fechaStr);
                        nuevo.devuelto = false;

                        appDataBase.databaseWriteExcecutor.execute(() -> {
                            db.prestamoDao().insertPrestamo(nuevo);
                            
                            // Marcar artículo como prestado
                            Articulo a = disponibles.get(artPos);
                            a.esPrestado = true;
                            db.articuloDao().updateArticulo(a);
                            
                            loadPrestamos();
                            loadSpinnersData();
                        });
                        Toast.makeText(getContext(), "Préstamo registrado", Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        Toast.makeText(getContext(), "Formato de fecha inválido", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null);

        builder.create().show();
    }
}
