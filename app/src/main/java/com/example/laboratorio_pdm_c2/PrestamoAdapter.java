package com.example.laboratorio_pdm_c2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laboratorio_pdm_c2.Entitys.Articulo;
import com.example.laboratorio_pdm_c2.Entitys.Persona;
import com.example.laboratorio_pdm_c2.Entitys.Prestamo;
import com.example.laboratorio_pdm_c2.database.appDataBase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PrestamoAdapter extends RecyclerView.Adapter<PrestamoAdapter.PrestamoViewHolder> {

    private List<Prestamo> prestamos = new ArrayList<>();
    private OnCompletarClickListener completarListener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @NonNull
    @Override
    public PrestamoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_prestamo, parent, false);
        return new PrestamoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PrestamoViewHolder holder, int position) {
        Prestamo current = prestamos.get(position);
        appDataBase db = appDataBase.getINSTANCE(holder.itemView.getContext());

        // Limpiar para evitar que se muestren datos de items reciclados
        holder.tvArticulo.setText("Cargando...");
        holder.tvPersona.setText("...");

        appDataBase.databaseWriteExcecutor.execute(() -> {
            Articulo articulo = db.articuloDao().getArticulo(current.idarticulo);
            Persona persona = db.personaDao().getPersona(current.idpersona);

            holder.itemView.post(() -> {
                if (articulo != null) holder.tvArticulo.setText(articulo.nombre);
                if (persona != null) holder.tvPersona.setText("Prestado a: " + persona.nombre);
            });
        });

        String fechaP = current.fechaPrestamo != null ? dateFormat.format(current.fechaPrestamo) : "N/A";
        String fechaD = current.fechaDevolucionEstimada != null ? dateFormat.format(current.fechaDevolucionEstimada) : "N/A";
        holder.tvFechas.setText("Desde: " + fechaP + " - Hasta: " + fechaD);

        holder.tvEstado.setText(current.devuelto ? "Estado: Completado" : "Estado: Pendiente");
        holder.btnCompletar.setVisibility(current.devuelto ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return prestamos.size();
    }

    public void setPrestamos(List<Prestamo> prestamos) {
        this.prestamos = prestamos;
        notifyDataSetChanged();
    }

    class PrestamoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvArticulo, tvPersona, tvFechas, tvEstado;
        private Button btnCompletar;

        public PrestamoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvArticulo = itemView.findViewById(R.id.tvArticuloPrestado);
            tvPersona = itemView.findViewById(R.id.tvPersonaPrestamo);
            tvFechas = itemView.findViewById(R.id.tvFechasPrestamo);
            tvEstado = itemView.findViewById(R.id.tvEstadoPrestamo);
            btnCompletar = itemView.findViewById(R.id.btnDevolver);

            btnCompletar.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (completarListener != null && position != RecyclerView.NO_POSITION) {
                    completarListener.onCompletarClick(prestamos.get(position));
                }
            });
        }
    }

    public interface OnCompletarClickListener {
        void onCompletarClick(Prestamo prestamo);
    }

    public void setOnCompletarClickListener(OnCompletarClickListener listener) {
        this.completarListener = listener;
    }
}
