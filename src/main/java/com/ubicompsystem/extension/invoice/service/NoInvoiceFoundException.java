package com.ubicompsystem.extension.invoice.service;

public class NoInvoiceFoundException extends Exception {

    public NoInvoiceFoundException( String message, Throwable cause) {
        super( message, cause );
    }

    public NoInvoiceFoundException(String message) {
        super(message);
    }
}
