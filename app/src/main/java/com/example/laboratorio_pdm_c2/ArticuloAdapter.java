package com.example.laboratorio_pdm_c2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laboratorio_pdm_c2.Entitys.Articulo;

import java.util.ArrayList;
import java.util.List;

public class ArticuloAdapter extends RecyclerView.Adapter<ArticuloAdapter.ArticuloViewHolder> {

    private List<Articulo> articulos = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ArticuloViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_articulo, parent, false);
        return new ArticuloViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticuloViewHolder holder, int position) {
        Articulo currentArticulo = articulos.get(position);
        holder.tvNombre.setText(currentArticulo.nombre);
        holder.tvDescripcion.setText(currentArticulo.descripcion);
        holder.tvCategoria.setText("Categoría: " + currentArticulo.nombre);
        holder.tvEstado.setText(currentArticulo.esPrestado ? "Prestado" : "Disponible");
        holder.tvEstado.setTextColor(currentArticulo.esPrestado ? 
            holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark) : 
            holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
    }

    @Override
    public int getItemCount() {
        return articulos.size();
    }

    public void setArticulos(List<Articulo> articulos) {
        this.articulos = articulos;
        notifyDataSetChanged();
    }

    class ArticuloViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNombre;
        private TextView tvDescripcion;
        private TextView tvCategoria;
        private TextView tvEstado;

        public ArticuloViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreArticulo);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionArticulo);
            tvCategoria = itemView.findViewById(R.id.tvCategoriaArticulo);
            tvEstado = itemView.findViewById(R.id.tvEstadoArticulo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(articulos.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Articulo articulo);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}