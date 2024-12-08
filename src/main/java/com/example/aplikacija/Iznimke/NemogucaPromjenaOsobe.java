package com.example.aplikacija.Iznimke;

public class NemogucaPromjenaOsobe extends Exception{

    public NemogucaPromjenaOsobe() {
    }

    public NemogucaPromjenaOsobe(String message) {
        super(message);
    }

    public NemogucaPromjenaOsobe(String message, Throwable cause) {
        super(message, cause);
    }

    public NemogucaPromjenaOsobe(Throwable cause) {
        super(cause);
    }
}
