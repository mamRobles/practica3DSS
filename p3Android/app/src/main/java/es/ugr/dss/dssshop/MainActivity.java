package es.ugr.dss.dssshop;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.ugr.dss.dssshop.adapter.ProductAdapter;
import es.ugr.dss.dssshop.api.ApiClient;
import es.ugr.dss.dssshop.model.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerProducts = findViewById(R.id.recyclerProducts);
        recyclerProducts.setLayoutManager(new LinearLayoutManager(this));
        loadProducts();
    }

    private void loadProducts() {
        ApiClient.getProductApi().getProducts()
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call,
                                           Response<List<Product>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Product> products = response.body();
                            recyclerProducts.setAdapter(new ProductAdapter(products));
                        } else {
                            Toast.makeText(MainActivity.this,
                                    "HTTP " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable error) {
                        Log.e("DSSShop", "Fallo de red", error);
                        Toast.makeText(MainActivity.this,
                                "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}