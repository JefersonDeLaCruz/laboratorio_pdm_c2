package com.example.laboratorio_pdm_c2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laboratorio_pdm_c2.Entitys.Persona;
import com.example.laboratorio_pdm_c2.database.appDataBase;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class fragment_persona extends Fragment {

    private RecyclerView rvPersonas;
    private PersonaAdapter adapter;
    private appDataBase db;

    public fragment_persona() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_persona, container, false);

        db = appDataBase.getINSTANCE(getContext());
        rvPersonas = view.findViewById(R.id.rvPersonas);
        FloatingActionButton fabAdd = view.findViewById(R.id.fabAddPersona);

        adapter = new PersonaAdapter(new PersonaAdapter.OnPersonaActionListener() {
            @Override
            public void onEdit(Persona persona) {
                showPersonaDialog(persona);
            }

            @Override
            public void onDelete(Persona persona) {
                confirmDelete(persona);
            }
        });

        rvPersonas.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPersonas.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> showPersonaDialog(null));

        loadPersonas();

        return view;
    }

    private void loadPersonas() {
        appDataBase.databaseWriteExcecutor.execute(() -> {
            List<Persona> personas = db.personaDao().getAllPersona();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> adapter.setPersonas(personas));
            }
        });
    }

    private void showPersonaDialog(Persona personaToEdit) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_persona, null);
        builder.setView(dialogView);

        TextInputEditText etNombre = dialogView.findViewById(R.id.etNombrePersona);
        TextInputEditText etApellido = dialogView.findViewById(R.id.etApellidoPersona);
        TextInputEditText etContacto = dialogView.findViewById(R.id.etContactoPersona);
        TextInputEditText etDireccion = dialogView.findViewById(R.id.etDireccionPersona);

        boolean isEdit = personaToEdit != null;
        if (isEdit) {
            builder.setTitle("Editar Persona");
            etNombre.setText(personaToEdit.nombre);
            etApellido.setText(personaToEdit.apellido);
            etContacto.setText(personaToEdit.contacto);
            etDireccion.setText(personaToEdit.direccion);
        } else {
            builder.setTitle("Registrar Persona");
        }

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = etNombre.getText().toString().trim();
            String apellido = etApellido.getText().toString().trim();
            String contacto = etContacto.getText().toString().trim();
            String direccion = etDireccion.getText().toString().trim();

            if (nombre.isEmpty() || contacto.isEmpty()) {
                Toast.makeText(getContext(), "Nombre y contacto son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            Persona p = isEdit ? personaToEdit : new Persona();
            p.nombre = nombre;
            p.apellido = apellido;
            p.contacto = contacto;
            p.direccion = direccion;

            appDataBase.databaseWriteExcecutor.execute(() -> {
                if (isEdit) {
                    db.personaDao().updatePersona(p);
                } else {
                    db.personaDao().insertPersona(p);
                }
                loadPersonas();
            });
        });

        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }

    private void confirmDelete(Persona persona) {
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Eliminar Persona")
                .setMessage("¿Estás seguro de que deseas eliminar a " + persona.nombre + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    appDataBase.databaseWriteExcecutor.execute(() -> {
                        db.personaDao().deletePersona(persona);
                        loadPersonas();
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}