package com.ubicompsystem.extension.invoice.service;

public class DuplicateInvoiceException extends Exception{

    public DuplicateInvoiceException( String message, Throwable cause) {
        super( message, cause );
    }

    public DuplicateInvoiceException(String message) {
        super(message);
    }
}
