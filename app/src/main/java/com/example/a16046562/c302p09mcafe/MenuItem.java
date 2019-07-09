package com.example.a16046562.c302p09mcafe;
import java.io.Serializable;
public class MenuItem implements Serializable {
    private String itemId;
    private String categoryId;
    private String itemDescription;
    private double itemUnitPrice;

    public MenuItem(String itemId, String categoryId, String itemDescription, double itemUnitPrice) {
        this.itemId = itemId;
        this.categoryId = categoryId;
        this.itemDescription = itemDescription;
        this.itemUnitPrice = itemUnitPrice;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public double getItemUnitPrice() {
        return itemUnitPrice;
    }

    public void setItemUnitPrice(double itemUnitPrice) {
        this.itemUnitPrice = itemUnitPrice;
    }
    @Override
    public String toString() {
        return itemDescription ;
    }
}
