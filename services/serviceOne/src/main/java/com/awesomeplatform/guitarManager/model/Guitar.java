package com.awesomeplatform.guitarManager.model;

import java.io.Serializable;

public class Guitar implements Serializable {
    private final long id;
    private final String brand;
    private final String model;

    public Guitar(long id, String brand, String model) {
        this.id = id;
        this.brand = brand;
        this.model = model;
    }

    public long getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() { return model; }
}
