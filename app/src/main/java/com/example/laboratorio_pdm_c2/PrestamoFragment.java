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
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class PrestamoFragment extends Fragment {

    private RecyclerView rvPrestamos;
    private PrestamoAdapter adapter;
    private appDataBase db;
    private MaterialButtonToggleGroup toggleGroup;
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
        toggleGroup = view.findViewById(R.id.toggleGroupPrestamos);
        FloatingActionButton fabAddPrestamo = view.findViewById(R.id.fabAddPrestamo);

        adapter = new PrestamoAdapter();
        rvPrestamos.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPrestamos.setAdapter(adapter);


        adapter.setOnCompletarClickListener(this::handleCompletar);

        //mnejar el cambio de botones (simulando tabs)
        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                loadPrestamos();
            }
        });

        fabAddPrestamo.setOnClickListener(v -> showAddPrestamoDialog());

        loadPrestamos();
        loadSpinnersData();

        return view;
    }

    private void loadPrestamos() {
        appDataBase.databaseWriteExcecutor.execute(() -> {
            List<Prestamo> todosLosPrestamos = db.prestamoDao().getAllPrestamo();
            
            // btn presionado
            boolean filtrarCompletados = toggleGroup.getCheckedButtonId() == R.id.btnFiltrarCompletados;
            
            List<Prestamo> filtrados = new ArrayList<>();
            for (Prestamo p : todosLosPrestamos) {
                if (p.devuelto == filtrarCompletados) {
                    filtrados.add(p);
                }
            }

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> adapter.setPrestamos(filtrados));
            }
        });
    }

    private void loadSpinnersData() {
        appDataBase.databaseWriteExcecutor.execute(() -> {
            articulosDisponibles = db.articuloDao().getAllArticulos();
            personasList = db.personaDao().getAllPersona();
        });
    }

    private void handleCompletar(Prestamo prestamo) {
        prestamo.devuelto = true;
        appDataBase.databaseWriteExcecutor.execute(() -> {
            db.prestamoDao().updatePrestamo(prestamo);
            Articulo articulo = db.articuloDao().getArticulo(prestamo.idarticulo);
            if (articulo != null) {
                articulo.esPrestado = false;
                db.articuloDao().updateArticulo(articulo);
            }
            loadPrestamos();
            loadSpinnersData();
        });
        Toast.makeText(getContext(), "Préstamo completado", Toast.LENGTH_SHORT).show();
    }

    private void showAddPrestamoDialog() {
        List<Articulo> disponibles = new ArrayList<>();
        for (Articulo a : articulosDisponibles) {
            if (!a.esPrestado) disponibles.add(a);
        }

        if (disponibles.isEmpty() || personasList.isEmpty()) {
            Toast.makeText(getContext(), "Necesitas artículos disponibles y personas registradas", Toast.LENGTH_LONG).show();
            return;
        }

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_prestamo, null);
        builder.setView(dialogView);

        Spinner spinnerArticulos = dialogView.findViewById(R.id.spinnerArticulos);
        Spinner spinnerPersonas = dialogView.findViewById(R.id.spinnerPersonas);
        TextInputEditText etFecha = dialogView.findViewById(R.id.etFechaDevolucion);

        etFecha.setFocusable(false);
        etFecha.setClickable(true);

        etFecha.setOnClickListener(v -> {
            CalendarConstraints constraints = new CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())
                    .build();

            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Fecha de devolución estimada")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setCalendarConstraints(constraints)
                    .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                TimeZone timeZoneUTC = TimeZone.getTimeZone("UTC");
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                format.setTimeZone(timeZoneUTC);
                etFecha.setText(format.format(new Date(selection)));
            });

            datePicker.show(getParentFragmentManager(), "DATE_PICKER");
        });

        List<String> nombresArticulos = new ArrayList<>();
        for (Articulo a : disponibles) nombresArticulos.add(a.nombre);
        ArrayAdapter<String> artAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nombresArticulos);
        artAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArticulos.setAdapter(artAdapter);

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

                    if (fechaStr.isEmpty()) {
                        Toast.makeText(getContext(), "Debe seleccionar una fecha", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        Prestamo nuevo = new Prestamo();
                        nuevo.idarticulo = disponibles.get(artPos).idarticulo;
                        nuevo.idpersona = personasList.get(perPos).idpersona;
                        nuevo.fechaPrestamo = new Date();
                        nuevo.fechaDevolucionEstimada = dateFormat.parse(fechaStr);
                        nuevo.devuelto = false;

                        appDataBase.databaseWriteExcecutor.execute(() -> {
                            db.prestamoDao().insertPrestamo(nuevo);
                            Articulo a = disponibles.get(artPos);
                            a.esPrestado = true;
                            db.articuloDao().updateArticulo(a);
                            loadPrestamos();
                            loadSpinnersData();
                        });
                        Toast.makeText(getContext(), "Préstamo registrado", Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        Toast.makeText(getContext(), "Error en el formato de fecha", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null);

        builder.create().show();
    }
}
