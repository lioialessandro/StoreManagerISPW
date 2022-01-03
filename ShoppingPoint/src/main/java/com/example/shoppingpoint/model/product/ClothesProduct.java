package com.example.shoppingpoint.model.product;

import com.example.shoppingpoint.utils.StatusType;

public class ClothesProduct extends Product {
    private String size, material;

    public ClothesProduct(Integer id, String name, Float price, Float discountedPrice, Integer quantity, StatusType status, String size, String material) {
        setId(id);
        setName(name);
        setPrice(price);
        setDiscountedPrice(discountedPrice);
        setQuantity(quantity);
        setStatus(status);
        setSize(size);
        setMaterial(material);
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}