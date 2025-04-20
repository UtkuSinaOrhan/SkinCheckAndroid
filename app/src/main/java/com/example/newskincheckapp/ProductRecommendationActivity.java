package com.example.newskincheckapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ProductRecommendationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_recommendation);

        // Enable back key in ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Recommended Products");
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Category information from intent
        String productCategory = getIntent().getStringExtra("PRODUCT_CATEGORY");

        // Buy recommended products
        List<Product> recommendedProducts = setupRecommendedProducts(productCategory);

        // Connect Adapter
        ProductAdapter adapter = new ProductAdapter(recommendedProducts);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Close current activity when back key is pressed
        finish();
        return true;
    }

    // Returns recommended products by category
    private List<Product> setupRecommendedProducts(String category) {
        List<Product> recommendedProducts = new ArrayList<>();

        switch (category) {
            case "Acne and Pimples":
                recommendedProducts.add(new Product("Cleanser Product", R.drawable.cleanser, "https://www.korendy.com.tr/products/cosrx-salicylic-acid-daily-gentle-cleanser?srsltid=AfmBOor30zqPEzZRC_sZ1xfjoBxvaSYl7vMG0PHAV_ylzZAisPr8suGq" ));
                recommendedProducts.add(new Product("Moisturizers", R.drawable.moisturizers, "https://www.dermoeczanem.com/cerave-yaglanma-karsiti-nemlendirici-yuz-kremi-52-ml"));
                recommendedProducts.add(new Product("Spot Treatment", R.drawable.spot_treatment, "https://www.trendyol.com/la-roche-posay/effaclar-duo-m-cilt-bakim-kremi-40ml-yagli-ve-akneye-egilimli-ciltler-p-2368487?boutiqueId=61&merchantId=365501"));
                recommendedProducts.add(new Product("Tea Tree Oil Serum", R.drawable.tea_tree_oil_serum,"https://www.korendy.com.tr/collections/all/products/dr-ceuracle-tea-tree-purifine-95-essence"));
                break;

            case "Dryness and Dehydration":
                recommendedProducts.add(new Product("Gentle Cleansers", R.drawable.gentle_cleansers, "https://www.trendyol.com/la-roche-posay/toleriane-caring-wash-400ml-p-2543665?boutiqueId=61&merchantId=149326"));
                recommendedProducts.add(new Product("Hyaluronic Acid Serums", R.drawable.hyaluronic_acid_serums, "https://www.trendyol.com/la-roche-posay/hyalu-b5-dolgunlastirici-serum-hassas-ciltiler-icin-30-ml-p-35814733"));
                recommendedProducts.add(new Product("Intensive Hydration Masks", R.drawable.intensive_hydration_masks, "https://www.sephora.com.tr/p/drink-up-intensive-overnight---avokadolu-nemlendirici-maske-P10050390.html"));
                recommendedProducts.add(new Product("Moisturizers with Ceramides", R.drawable.moisturizers_with_ceramides , "https://www.dermoeczanem.com/cerave-nemlendirici"));
                break;

            case "Dark Spots and Hyperpigmentation":
                recommendedProducts.add(new Product("Vitamin C Serum", R.drawable.vitamin_c_serum, "https://www.trendyol.com/la-roche-posay/pure-vitamin-c10-c-vitamin-icerikli-isilti-veren-serum-30ml-p-82732248"));
                recommendedProducts.add(new Product("Products with Niacinamide", R.drawable.product_with_niacinamide, "https://www.dermoeczanem.com/cosrx-galactomyces-maya-mantari-ozlu-cilt-tonu-esitlemeye-yardimci-serum-100-ml"));
                recommendedProducts.add(new Product("Spot Correctors with Alpha Arbutin", R.drawable.spot_correctors_with_alpha_arbutin, "https://www.trendyol.com/the-ordinary/alpha-arbutin-2-ha-p-28309809"));
                recommendedProducts.add(new Product("Sunscreens SPF 30 or Higher", R.drawable.sunscreens_spf_or_higher, "https://www.trendyol.com/la-roche-posay/anthelios-xl-spf-50-dry-touch-yagli-ciltler-icin-matlastirici-parfumsuz-yuz-gunes-kremi-50-ml-p-737514859"));
                break;

            case "Sensitivity and Redness":
                recommendedProducts.add(new Product("Azelaic Acid Product", R.drawable.azelaic_acid_product, "https://www.trendyol.com/the-ordinary/azelaic-acid-suspension-10-30ml-p-6707048?utm_source=chatgpt.com"));
                recommendedProducts.add(new Product("Moisturizers with Probiotics", R.drawable.moisturize_with_probiotics, "https://miseca.com/products/prebiyotik-nemlendirici-krem?utm_source=chatgpt.com"));
                recommendedProducts.add(new Product("Soothing Toner", R.drawable.soothing_toner,"https://www.sephora.com.tr/p/aloe-vera-toner-P3607080.html"));
                recommendedProducts.add(new Product("Mineral-Based Sunscreen", R.drawable.mineral_based_sunscreen,"https://www.dermoeczanem.com/avene-mineral-sivi-gunes-kremi-spf-50-40-ml?gad_source=4&gclid=CjwKCAiAgoq7BhBxEiwAVcW0LCyKX-E_BsVycJNlq6DNe-zyjcrWc8YVxNC5foj6srmepIEZGbNvUBoCyUYQAvD_BwE"));
                break;

            case "Oily Skin and Shine":
                recommendedProducts.add(new Product("Clay Mask Kaolin Based", R.drawable.claymask_kaolin_based, "https://www.origins.com.tr/product/15346/62429/cilt-bakimi/bakim/maskeler/original-skintm/arndrc-puruzsuzlestirici-hassas-kil-maskesi#/sku/190723"));
                recommendedProducts.add(new Product("Oil Controlling Toners", R.drawable.oil_controlling_toners_witch_hazel, "https://www.trendyol.com/sirenol/natural-cadi-findigi-cilt-sikilastirici-gozenek-temizleyici-tonik-300-ml-p-33993054?utm_source=chatgpt.com"));
                recommendedProducts.add(new Product("Lightweight Gel Moisturizer", R.drawable.lightweight_gel_moisturizer ,"https://www.trendyol.com/clinique/dramt-diff-hydrating-jelly-125-ml-p-4827049"));
                recommendedProducts.add(new Product("Cleansers with Salicylic Acid", R.drawable.cleansers_with_saliucylic_acid, "https://www.korendy.com.tr/products/cosrx-salicylic-acid-daily-gentle-cleanser"));
                break;

            case "Fine Lines and Wrinkles":
                recommendedProducts.add(new Product("Retinol Serum", R.drawable.retinol_serum, "https://www.dermoeczanem.com/la-roche-posay-retinol-b3-yaslanma-ve-kirisiklik-karsiti-serum-30-ml?gad_source=1&gclid=CjwKCAiAgoq7BhBxEiwAVcW0LLLwWCoE3FTPy1v60d1mBP_n3A0N4pyLSiIHcH6ZjkY2WJCZ4tugthoCERgQAvD_BwE"));
                recommendedProducts.add(new Product("Moisturizers with Peptides", R.drawable.moisturizer_with_peptides, "https://www.origins.com.tr/product/15917/128043/tum-urunler/youthtopatm-elma-ozu-ve-peptit-iceren-dolgunlastrc-krem/antioksidan-acsndan-zengin-peptit-iceren-nemlendirici?utm_source=chatgpt.com#/sku/186594"));
                recommendedProducts.add(new Product("Collagen Boosting Mask", R.drawable.collagen_boosting_mask, "https://www.kikomilano.com.tr/new-bright-lift-mask/"));
                recommendedProducts.add(new Product("Antioxidant Rich Product", R.drawable.antioxidant_rich_product, "https://www.kiehls.com.tr/midnight-recovery-concentrate-p-11148?gad_source=1&gclid=CjwKCAiAgoq7BhBxEiwAVcW0LJnIkIbxAa-9qboHoBYZoDs8WugbpgR1A9B7mGe6yE8dYs3OAWsquxoCKkcQAvD_BwE&gclsrc=aw.ds"));
                break;

            case "Pore Issues":
                recommendedProducts.add(new Product("Clay Mask", R.drawable.clay_mask, "https://www.dermoeczanem.com/caudalie-instant-detox-mask-75-ml?gad_source=4&gclid=CjwKCAiAgoq7BhBxEiwAVcW0LBs0ym2lx00dtsPTHHqkjlTToUKvk9bCVoc3-XVQFgxILuB5YdFs3hoCOO8QAvD_BwE"));
                recommendedProducts.add(new Product("Pore Refining Serum", R.drawable.pore_refining_serum,"https://www.dermoeczanem.com/bioderma-sebium-pore-refiner-krem-30ml?gad_source=1&gclid=CjwKCAiAgoq7BhBxEiwAVcW0LPGI10dJjFPYGTsYNI9d5oBq28i1swtWhg-hTQtSyM4G9z1emJkAABoCRVMQAvD_BwE"));
                recommendedProducts.add(new Product("Lightweight Moisturizer", R.drawable.lightweightmoisturizer, "https://www.clinique.com.tr/product/1687/5047/cilt-bakimi/yuz-nemlendirici/dramatically-different-nemlendirici-jel-krem"));
                recommendedProducts.add(new Product("Toners with BHA", R.drawable.toner_with_bha, "https://www.trendyol.com/cosrx/aha-bha-clarifying-treatment-toner-aha-bha-iceren-arindirici-tonik-p-2870701"));
                break;

            default:
                break;
        }

        return recommendedProducts;
    }
}
