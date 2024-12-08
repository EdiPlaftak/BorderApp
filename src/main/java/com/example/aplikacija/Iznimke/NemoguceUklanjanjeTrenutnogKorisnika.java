package com.example.aplikacija.Iznimke;

public class NemoguceUklanjanjeTrenutnogKorisnika extends RuntimeException{

    public NemoguceUklanjanjeTrenutnogKorisnika() {
    }

    public NemoguceUklanjanjeTrenutnogKorisnika(String message) {
        super(message);
    }

    public NemoguceUklanjanjeTrenutnogKorisnika(String message, Throwable cause) {
        super(message, cause);
    }

    public NemoguceUklanjanjeTrenutnogKorisnika(Throwable cause) {
        super(cause);
    }
}
