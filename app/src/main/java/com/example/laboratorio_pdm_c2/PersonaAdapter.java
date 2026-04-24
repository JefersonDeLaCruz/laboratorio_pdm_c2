package com.example.laboratorio_pdm_c2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laboratorio_pdm_c2.Entitys.Persona;

import java.util.ArrayList;
import java.util.List;

public class PersonaAdapter extends RecyclerView.Adapter<PersonaAdapter.PersonaViewHolder> {

    private List<Persona> personas = new ArrayList<>();
    private OnPersonaActionListener listener;

    public interface OnPersonaActionListener {
        void onEdit(Persona persona);
        void onDelete(Persona persona);
    }

    public PersonaAdapter(OnPersonaActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PersonaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_persona, parent, false);
        return new PersonaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonaViewHolder holder, int position) {
        Persona current = personas.get(position);
        holder.tvNombre.setText(current.nombre + " " + (current.apellido != null ? current.apellido : ""));
        holder.tvContacto.setText(current.contacto);
        holder.tvDireccion.setText(current.direccion);

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(current));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(current));
    }

    @Override
    public int getItemCount() {
        return personas.size();
    }

    public void setPersonas(List<Persona> personas) {
        this.personas = personas;
        notifyDataSetChanged();
    }

    class PersonaViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNombre, tvContacto, tvDireccion;
        private ImageButton btnEdit, btnDelete;

        public PersonaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreCompleto);
            tvContacto = itemView.findViewById(R.id.tvContacto);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            btnEdit = itemView.findViewById(R.id.btnEditPersona);
            btnDelete = itemView.findViewById(R.id.btnDeletePersona);
        }
    }
}