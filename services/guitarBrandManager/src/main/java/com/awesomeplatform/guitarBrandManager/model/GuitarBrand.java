package com.awesomeplatform.guitarBrandManager.model;

import java.io.Serializable;

public class GuitarBrand implements Serializable {
    private final long id;
    private final String brand;

    public GuitarBrand(long id, String brand) {
        this.id = id;
        this.brand = brand;
    }

    public long getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }
}
