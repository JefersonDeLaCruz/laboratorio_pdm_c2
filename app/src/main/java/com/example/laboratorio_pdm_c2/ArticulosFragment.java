package com.example.laboratorio_pdm_c2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laboratorio_pdm_c2.Entitys.Articulo;
import com.example.laboratorio_pdm_c2.Entitys.Categoria;
import com.example.laboratorio_pdm_c2.database.appDataBase;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class ArticulosFragment extends Fragment {

    private RecyclerView rvArticulos;
    private ArticuloAdapter adapter;
    private appDataBase db;
    private List<Categoria> categoriasList = new ArrayList<>();

    public ArticulosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articulos, container, false);

        db = appDataBase.getINSTANCE(getContext());
        rvArticulos = view.findViewById(R.id.rvArticulos);
        FloatingActionButton fabAddArticulo = view.findViewById(R.id.fabAddArticulo);
        FloatingActionButton fabAddCategoria = view.findViewById(R.id.fabAddCategoria);

        adapter = new ArticuloAdapter();
        rvArticulos.setLayoutManager(new LinearLayoutManager(getContext()));
        rvArticulos.setAdapter(adapter);

        fabAddArticulo.setOnClickListener(v -> showAddArticuloDialog());
        fabAddCategoria.setOnClickListener(v -> showAddCategoriaDialog());

        loadArticulos();
        loadCategorias();

        return view;
    }

    private void loadArticulos() {
        appDataBase.databaseWriteExcecutor.execute(() -> {
            List<Articulo> articulos = db.articuloDao().getAllArticulos();
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> adapter.setArticulos(articulos));
            }
        });
    }

    private void loadCategorias() {
        appDataBase.databaseWriteExcecutor.execute(() -> {
            categoriasList = db.categoriaDao().getAllCategoria();
        });
    }

    private void showAddCategoriaDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("Nueva Categoría");

        final EditText input = new EditText(getContext());
        input.setHint("Nombre de la categoría (ej. Electrónica)");
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombreCat = input.getText().toString().trim();
            if (!nombreCat.isEmpty()) {
                Categoria nuevaCat = new Categoria();
                nuevaCat.nombre = nombreCat;
                appDataBase.databaseWriteExcecutor.execute(() -> {
                    db.categoriaDao().insertCategoria(nuevaCat);
                    loadCategorias();
                });
                Toast.makeText(getContext(), "Categoría guardada", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void showAddArticuloDialog() {
        if (categoriasList.isEmpty()) {
            Toast.makeText(getContext(), "Primero debe registrar categorías", Toast.LENGTH_SHORT).show();
            return;
        }

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_articulo, null);
        builder.setView(dialogView);

        TextInputEditText etNombre = dialogView.findViewById(R.id.etNombreArticulo);
        TextInputEditText etDescripcion = dialogView.findViewById(R.id.etDescripcionArticulo);
        Spinner spinnerCategorias = dialogView.findViewById(R.id.spinnerCategorias);

        List<String> nombresCategorias = new ArrayList<>();
        for (Categoria cat : categoriasList) {
            nombresCategorias.add(cat.nombre);
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, nombresCategorias);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorias.setAdapter(spinnerAdapter);

        builder.setTitle("Registrar Artículo")
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = etNombre.getText().toString().trim();
                    String descripcion = etDescripcion.getText().toString().trim();
                    int categoriaIndex = spinnerCategorias.getSelectedItemPosition();

                    if (!nombre.isEmpty()) {
                        Articulo nuevoArticulo = new Articulo();
                        nuevoArticulo.nombre = nombre;
                        nuevoArticulo.descripcion = descripcion;
                        nuevoArticulo.idcategoria = categoriasList.get(categoriaIndex).idcategoria;
                        nuevoArticulo.esPrestado = false;

                        appDataBase.databaseWriteExcecutor.execute(() -> {
                            db.articuloDao().insertArticulo(nuevoArticulo);
                            loadArticulos();
                        });
                    } else {
                        Toast.makeText(getContext(), "El nombre es obligatorio", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null);

        builder.create().show();
    }
}