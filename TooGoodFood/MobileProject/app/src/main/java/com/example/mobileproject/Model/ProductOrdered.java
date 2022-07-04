//This class allow the users to see the Preview of the current Order
package com.example.mobileproject.Model;

import java.io.Serializable;

public class ProductOrdered implements Serializable {
    String  Name, Quantity, Prezzo, Totale;


    public ProductOrdered(String name,String quantity ,String prezzo, String totale) {
        Name = name;
        Totale = totale;
        Quantity = quantity;
        Prezzo = prezzo;
    }

    public ProductOrdered() {}

    public String getTotale() {
        return Totale;
    }

    public void setTotale(String totale) {
        Totale = totale;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrezzo() {
        return Prezzo;
    }

    public void setPrezzo(String prezzo) {
        Prezzo = prezzo;
    }
}
