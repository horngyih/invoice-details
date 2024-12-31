package com.ubicompsystem.extension.invoice.dao;

import com.ubicompsystem.extension.invoice.model.InvoiceDetailsDTO;
import com.ubicompsystem.extension.invoice.service.DuplicateInvoiceException;
import com.ubicompsystem.extension.invoice.service.NoInvoiceFoundException;

public interface InvoiceDetailDAO {
    InvoiceDetailsDTO getByInvoiceID(String invoiceID ) throws NoInvoiceFoundException, DuplicateInvoiceException;
    InvoiceDetailsDTO createInvoiceDetail( String invoiceID ) throws DuplicateInvoiceException;
    InvoiceDetailsDTO updateInvoiceDetail( InvoiceDetailsDTO invoiceDetailsDTO ) throws NoInvoiceFoundException, DuplicateInvoiceException;
}
