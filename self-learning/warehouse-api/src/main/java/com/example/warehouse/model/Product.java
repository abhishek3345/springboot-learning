package com.example.warehouse.model;

import jakarta.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private String vendor;
    private int price;
    private int stock;
    private String currency;
    private String image_url;
    @Column(unique = true)
    private String sku;

    //constructors

    public Product() {
    }

    public Product(String name, String description, String vendor, int price, int stock, String currency, String image_url, String sku) {
        this.name = name;
        this.description = description;
        this.vendor = vendor;
        this.price = price;
        this.stock = stock;
        this.currency = currency;
        this.image_url = image_url;
        this.sku = sku;
    }

    public Product(int id, String name, String description, String vendor, int price, int stock, String currency, String image_url, String sku) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.vendor = vendor;
        this.price = price;
        this.stock = stock;
        this.currency = currency;
        this.image_url = image_url;
        this.sku = sku;
    }



    //getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }


    //to-string


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", vendor='" + vendor + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", currency='" + currency + '\'' +
                ", image_url='" + image_url + '\'' +
                ", sku='" + sku + '\'' +
                '}';
    }
}
