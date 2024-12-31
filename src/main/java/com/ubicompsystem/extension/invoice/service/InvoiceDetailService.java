package com.ubicompsystem.extension.invoice.service;

import com.ubicompsystem.extension.invoice.model.InvoiceDetailsDTO;

public interface InvoiceDetailService {
    void registerInvoice( String invoiceID ) throws DuplicateInvoiceException;
    InvoiceDetailsDTO retrieveInvoice( String invoiceID ) throws NoInvoiceFoundException, DuplicateInvoiceException;
    InvoiceDetailsDTO updateInvoice(InvoiceDetailsDTO invoiceDetailsDTO ) throws NoInvoiceFoundException, DuplicateInvoiceException;

}
