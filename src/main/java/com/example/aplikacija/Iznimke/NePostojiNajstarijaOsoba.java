package com.example.aplikacija.Iznimke;

public class NePostojiNajstarijaOsoba extends Exception{

    public NePostojiNajstarijaOsoba() {
    }

    public NePostojiNajstarijaOsoba(String message) {
        super(message);
    }

    public NePostojiNajstarijaOsoba(String message, Throwable cause) {
        super(message, cause);
    }

    public NePostojiNajstarijaOsoba(Throwable cause) {
        super(cause);
    }
}
