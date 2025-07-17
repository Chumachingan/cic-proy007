package es.cic.curso2025.proy007.controller;

public class CocheException extends Exception {

    public CocheException() {
        super();
    }

    public CocheException(String message) {
        super(message);        
    }

    public CocheException(String message, Throwable throwable) {
        super(message, throwable);        
    }
}

