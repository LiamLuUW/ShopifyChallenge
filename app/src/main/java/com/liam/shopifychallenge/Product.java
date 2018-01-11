package com.liam.shopifychallenge;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liam on 2018-01-03. Product Object
 */

public class Product implements Parcelable{
    private long id;
    private String title;
    private String body_html;
    private String vendor;
    private List<ProductVariant> variants;
    private ProductImage image;

    public Product(long id, String title, String body_html){
        this.id = id;
        this.title = title;
        this.body_html = body_html;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody_html() {
        return body_html;
    }

    public void setBody_html(String body_html) {
        this.body_html = body_html;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }


    public List<ProductVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<ProductVariant> variants) {
        this.variants = variants;
    }


    public ProductImage getImage() {
        return image;
    }

    public void setImage(ProductImage image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags){
        out.writeValue(this.id);
        out.writeString(this.title);
        out.writeString(this.body_html);
        out.writeString(this.vendor);
        out.writeTypedList(this.variants);
        out.writeParcelable(this.image, flags);
        //no need to pass image data since we don't need it in detail page
    }

    public Product(Parcel in){
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.title = in.readString();
        this.body_html = in.readString();
        this.vendor =in.readString();
        this.variants = new ArrayList<ProductVariant>();
        in.readTypedList(variants, ProductVariant.CREATOR);
        this.image = in.readParcelable(ProductImage.class.getClassLoader());
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {

        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public boolean equals(Product p2){
        if (this.id == p2.id && this.title.equals( p2.title) && this.body_html.equals(p2.body_html)
                && this.vendor.equals(p2.vendor) && this.image.equals(p2.image) ){
            int pos = 0;
            for(ProductVariant pv : p2.getVariants()){
                if(!this.variants.get(pos).equals(pv)) return false;
                pos++;
            }
            return true;
        }
            return false;
    }
}


class ProductImage implements Parcelable {
    private long id;
    private long product_id;
    private String src;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags){
        out.writeValue(this.id);
        out.writeValue(this.product_id);
        out.writeString(this.src);
    }

    public ProductImage(Parcel in){
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.product_id = (Long) in.readValue(Long.class.getClassLoader());
        this.src = in.readString();
    }

    public static final Parcelable.Creator<ProductImage> CREATOR = new Parcelable.Creator<ProductImage>() {

        public ProductImage createFromParcel(Parcel in) {
            return new ProductImage(in);
        }

        public ProductImage[] newArray(int size) {
            return new ProductImage[size];
        }
    };

    public boolean equals(ProductImage pi){
        return this.id == pi.id && this.src.equals(pi.src) && this.product_id == pi.product_id;
    }
}

class ProductVariant implements Parcelable{
    private long id;
    private long product_id;
    private String title;
    private float price;
    private float weight;
    private String weight_unit;
    private boolean taxable;
    private int inventory_quantity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getWeight_unit() {
        return weight_unit;
    }

    public void setWeight_unit(String weight_unit) {
        this.weight_unit = weight_unit;
    }

    public boolean isTaxable() {
        return taxable;
    }

    public void setTaxable(boolean taxable) {
        this.taxable = taxable;
    }

    public int getInventory_quantity() {
        return inventory_quantity;
    }

    public void setInventory_quantity(int inventory_quantity) {
        this.inventory_quantity = inventory_quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags){
        out.writeValue(this.id);
        out.writeValue(this.product_id);
        out.writeString(this.title);
        out.writeValue(this.price);
        out.writeValue(this.weight);
        out.writeString(this.weight_unit);
        out.writeValue(this.inventory_quantity);
        out.writeValue(this.taxable);
    }


    public ProductVariant(Parcel in){
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.product_id = (Long) in.readValue(Long.class.getClassLoader());
        this.title = in.readString();
        this.price = (Float) in.readValue(Float.class.getClassLoader());
        this.weight = (Float) in.readValue(Float.class.getClassLoader());
        this.weight_unit = in.readString();
        this.inventory_quantity = (Integer) in.readValue(Integer.class.getClassLoader());
        this.taxable = (Boolean) in.readValue(Boolean.class.getClassLoader());

    }

    public static final Parcelable.Creator<ProductVariant> CREATOR = new Parcelable.Creator<ProductVariant>() {

        public ProductVariant createFromParcel(Parcel in) {
            return new ProductVariant(in);
        }

        public ProductVariant[] newArray(int size) {
            return new ProductVariant[size];
        }
    };

    public boolean equals(ProductVariant pv){
        return this.id == pv.id && this.product_id == pv.product_id && this.title.equals(pv.title)
                && this.price == pv.price && this.weight == pv.weight && this.weight_unit.equals(pv.weight_unit)
                && this.inventory_quantity == pv.inventory_quantity && this.taxable == pv.taxable;
    }
}