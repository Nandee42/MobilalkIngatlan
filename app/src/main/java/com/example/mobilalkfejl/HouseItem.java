package com.example.mobilalkfejl;

public class HouseItem {
    private String address;
    private String price;
    private String description;
    private String contact;
    private final int imageResource;

    public HouseItem(String address, String price, String description, String contact, int imageResource) {
        this.address = address;
        this.price = price;
        this.description = description;
        this.contact = contact;
        this.imageResource = imageResource;
    }

    public String getAddress() {
        return address;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getContact() {
        return contact;
    }

    public int getImageResource() {
        return imageResource;
    }
}
