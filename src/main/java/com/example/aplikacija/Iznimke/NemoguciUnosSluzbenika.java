package com.example.aplikacija.Iznimke;

public class NemoguciUnosSluzbenika extends RuntimeException{

    public NemoguciUnosSluzbenika() {
    }

    public NemoguciUnosSluzbenika(String message) {
        super(message);
    }

    public NemoguciUnosSluzbenika(String message, Throwable cause) {
        super(message, cause);
    }

    public NemoguciUnosSluzbenika(Throwable cause) {
        super(cause);
    }
}
