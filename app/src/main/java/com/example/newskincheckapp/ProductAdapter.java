package com.example.newskincheckapp;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    // List of products to be displayed in the RecyclerView
    private List<Product> productList;

    // Constructor to initialize the product list
    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for individual product items
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // Get the product at the given position
        Product product = productList.get(position);

        // Set the product name and image in the corresponding views
        holder.productName.setText(product.getName());
        holder.productImage.setImageResource(product.getImageResId());

        // Handle product click event
        holder.itemView.setOnClickListener(v -> {
            // Retrieve the URL of the product
            String productUrl = product.getUrl();
            if (productUrl != null && !productUrl.isEmpty()) {
                // Open the product URL in a browser
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(productUrl));
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Return the total number of products in the list
        return productList.size();
    }

    // ViewHolder class to hold the views for each product item
    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName; // TextView for displaying product name
        ImageView productImage; // ImageView for displaying product image

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the views
            productName = itemView.findViewById(R.id.productName);
            productImage = itemView.findViewById(R.id.productImage);
        }
    }
}
