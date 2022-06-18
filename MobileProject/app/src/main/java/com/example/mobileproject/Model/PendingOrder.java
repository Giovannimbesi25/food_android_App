package com.example.mobileproject.Model;

import java.io.Serializable;

public class PendingOrder implements Serializable {
    String Totale,  DateTime,  Indirizzo;

    public PendingOrder() {
    }

    public PendingOrder(String totale, String dateTime, String indirizzo) {
        Totale = totale;
        DateTime = dateTime;
        Indirizzo = indirizzo;
    }

    public String getTotale() {
        return Totale;
    }

    public void setTotale(String totale) {
        Totale = totale;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getIndirizzo() {
        return Indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        Indirizzo = indirizzo;
    }
}
