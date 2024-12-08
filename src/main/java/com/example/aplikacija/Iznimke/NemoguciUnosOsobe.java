package com.example.aplikacija.Iznimke;

public class NemoguciUnosOsobe extends RuntimeException{

    public NemoguciUnosOsobe() {
    }

    public NemoguciUnosOsobe(String message) {
        super(message);
    }

    public NemoguciUnosOsobe(String message, Throwable cause) {
        super(message, cause);
    }

    public NemoguciUnosOsobe(Throwable cause) {
        super(cause);
    }
}
