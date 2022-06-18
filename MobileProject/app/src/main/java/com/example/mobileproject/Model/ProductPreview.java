package com.example.mobileproject.Model;

import java.io.Serializable;

public class ProductPreview implements Serializable {
    String Image, Name, Descrizione, Prezzo, Rating;

    public ProductPreview() {
    }

    public ProductPreview(String image, String name) {
        Image = image;
        Name = name;
    }

    public ProductPreview(String image, String name, String descrizione, String prezzo, String rating) {
        Image = image;
        Name = name;
        Descrizione = descrizione;
        Prezzo = prezzo;
        Rating = rating;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getDescrizione() {
        return Descrizione;
    }

    public void setDescrizione(String descrizione) {
        Descrizione = descrizione;
    }

    public String getPrezzo() {
        return Prezzo;
    }

    public void setPrezzo(String prezzo) {
        Prezzo = prezzo;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
