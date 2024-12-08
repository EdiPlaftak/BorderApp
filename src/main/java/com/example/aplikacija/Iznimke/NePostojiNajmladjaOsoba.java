package com.example.aplikacija.Iznimke;

public class NePostojiNajmladjaOsoba extends Exception{

    public NePostojiNajmladjaOsoba() {
    }

    public NePostojiNajmladjaOsoba(String message) {
        super(message);
    }

    public NePostojiNajmladjaOsoba(String message, Throwable cause) {
        super(message, cause);
    }

    public NePostojiNajmladjaOsoba(Throwable cause) {
        super(cause);
    }
}
