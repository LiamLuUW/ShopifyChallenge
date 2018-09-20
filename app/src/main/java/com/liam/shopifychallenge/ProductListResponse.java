package com.liam.shopifychallenge;

import android.util.Log;

import com.liam.shopifychallenge.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * ProductList API call's response
 */

public class ProductListResponse {
    public List<Product> products;
    private Set<String> tagList;
    private HashMap<String, ArrayList<Product>> hashedProductList;

    public void setTagList() {
        hashedProductList = new HashMap<>();
        for (Product product : products) {
           String[] tags = StringUtil.parseStringsToList(product.getTags());
            for (String tag : tags) {
                //newInstance new unique tag key and add prduct
                if (!hashedProductList.containsKey(tag)) {
                    ArrayList<Product> products = new ArrayList<>();
                    products.add(product);
                    hashedProductList.put(tag, products);
                } else {
                    hashedProductList.get(tag).add(product);
                }
            }
        }
        tagList = hashedProductList.keySet();
    }

    public Set<String> getTagList() {
        return tagList;
    }

    public HashMap<String, ArrayList<Product>> getHashedProductList() {
        return hashedProductList;
    }

    public void setHashedProductList(HashMap<String, ArrayList<Product>> hashedProductList) {
        this.hashedProductList = hashedProductList;
    }
}
