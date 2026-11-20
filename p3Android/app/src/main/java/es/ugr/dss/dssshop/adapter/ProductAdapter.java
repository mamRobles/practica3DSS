package es.ugr.dss.dssshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import es.ugr.dss.dssshop.R;
import es.ugr.dss.dssshop.model.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final List<Product> products;

    public ProductAdapter(List<Product> products) {
        this.products = products;
    }
    // onCreateViewHolder: inflar item_product.xml
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(row);
    }
    // onBindViewHolder: asignar nombre y precio
    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product p = products.get(position);
        holder.txtName.setText(p.getProductoNombre());
        holder.txtPrice.setText(String.format(Locale.getDefault(), "%.2f EUR", p.getProductoPrice()));
        // El boton Add no tiene accion en esta sesion.
        //  Pero para el aviso enseñado en clase hace falta esto:
        holder.btnAdd.setOnClickListener(v ->
                Toast.makeText(holder.itemView.getContext(),
                        p.getProductoNombre() + " added to cart", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
    // ProductViewHolder: referencias a las vistas
    static class ProductViewHolder extends RecyclerView.ViewHolder {
        final TextView txtName;
        final TextView txtPrice;
        final Button btnAdd;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName  = itemView.findViewById(R.id.txtProductoNombre);
            txtPrice = itemView.findViewById(R.id.txtProductoPrice);
            btnAdd   = itemView.findViewById(R.id.btnAniadirCarrito);
        }
    }
}