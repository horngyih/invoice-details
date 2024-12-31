package com.ubicompsystem.extension.invoice.service;

import com.ubicompsystem.extension.invoice.dao.InvoiceDetailDAO;
import com.ubicompsystem.extension.invoice.model.InvoiceDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultInvoiceDetailServiceImpl
    implements InvoiceDetailService
{
    InvoiceDetailDAO invoiceDetailDAO;

    public DefaultInvoiceDetailServiceImpl( @Autowired InvoiceDetailDAO invoiceDetailDAO) {
        this.invoiceDetailDAO = invoiceDetailDAO;
    }

    @Override
    public void registerInvoice(String invoiceID) throws DuplicateInvoiceException {
        this.invoiceDetailDAO.createInvoiceDetail( invoiceID );
    }

    @Override
    public InvoiceDetailsDTO retrieveInvoice(String invoiceID) throws NoInvoiceFoundException, DuplicateInvoiceException {
        return this.invoiceDetailDAO.getByInvoiceID(invoiceID);
    }

    @Override
    public InvoiceDetailsDTO updateInvoice(InvoiceDetailsDTO invoiceDetailsDTO) throws NoInvoiceFoundException, DuplicateInvoiceException {
        return this.invoiceDetailDAO.updateInvoiceDetail( invoiceDetailsDTO );
    }
}
