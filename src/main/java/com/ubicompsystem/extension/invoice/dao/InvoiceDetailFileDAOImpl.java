package com.ubicompsystem.extension.invoice.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubicompsystem.extension.invoice.model.InvoiceDetailsDTO;
import com.ubicompsystem.extension.invoice.service.DuplicateInvoiceException;
import com.ubicompsystem.extension.invoice.service.NoInvoiceFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class InvoiceDetailFileDAOImpl
    implements InvoiceDetailDAO
{
    @Value("${invoice.working.directory:}")
    String invoiceFilePath;

    File workingDirectory;

    @Override
    public InvoiceDetailsDTO getByInvoiceID(String invoiceID) throws NoInvoiceFoundException, DuplicateInvoiceException{
        if( invoiceID == null || invoiceID.isEmpty() ) throw new NoInvoiceFoundException("No Invoice ID provided" );
        File invoiceFile = getInvoiceFile( invoiceID );
        if( invoiceFile.exists() ){
            try {
                return (InvoiceDetailsDTO) getObjectMapper().readValue( invoiceFile, InvoiceDetailsDTO.class );
            } catch (IOException e) {
                throw new NoInvoiceFoundException( "Unable to read invoice data", e );
            }
        }
        throw new NoInvoiceFoundException( String.format( "No Invoice %s registered", invoiceID ) );
    }

    @Override
    public InvoiceDetailsDTO createInvoiceDetail(String invoiceID) throws DuplicateInvoiceException {
        if( invoiceID == null || invoiceID.isEmpty() ) throw new IllegalArgumentException("No invoice ID provided" );
        InvoiceDetailsDTO invoiceDetailsDTO = new InvoiceDetailsDTO();
        invoiceDetailsDTO.setInvoiceID( invoiceID );

        File newInvoiceFile = getInvoiceFile( invoiceID );
        if( newInvoiceFile.exists() ) throw new DuplicateInvoiceException( "Invoice " + invoiceID + " already exists");

        try {
            getObjectMapper().writeValue( newInvoiceFile, invoiceDetailsDTO );
        } catch (Exception e) {
            throw new DuplicateInvoiceException( "Unable to write Invoice", e );
        }

        return invoiceDetailsDTO;
    }

    @Override
    public InvoiceDetailsDTO updateInvoiceDetail(InvoiceDetailsDTO invoiceDetailsDTO) throws NoInvoiceFoundException, DuplicateInvoiceException {
        if( invoiceDetailsDTO == null ) return invoiceDetailsDTO;
        if( invoiceDetailsDTO.getInvoiceID() == null || invoiceDetailsDTO.getInvoiceID().isEmpty() ) throw new NoInvoiceFoundException("No Invoice ID provided" );

        File invoiceFile = getInvoiceFile( invoiceDetailsDTO.getInvoiceID() );
        if( invoiceFile.exists() ){
            try {
                getObjectMapper().writeValue(invoiceFile, invoiceDetailsDTO);
                return invoiceDetailsDTO;
            } catch (IOException e) {
                throw new NoInvoiceFoundException( "Unable to update invoice data", e );
            }
        } else {
            throw new NoInvoiceFoundException( "Invoice " + invoiceDetailsDTO.getInvoiceID() + " does not exist" );
        }
    }

    protected ObjectMapper getObjectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        return mapper;
    }

    protected File getInvoiceFile( String invoiceID ){
        return new File(getWorkingDirectory(), "INV" + sanitizeInvoiceID(invoiceID) + ".txt");
    }

    protected static String sanitizeInvoiceID( String invoiceID ){
        if( invoiceID == null || invoiceID.isEmpty() ) return invoiceID;
        return invoiceID.replaceAll( " ", "-" );
    }

    public synchronized File getWorkingDirectory(){
        if( workingDirectory == null ){
            if( invoiceFilePath == null || invoiceFilePath.isEmpty() ){
                workingDirectory = new File(".");
            } else {
                try{
                    workingDirectory = new File( invoiceFilePath );
                } catch( Exception e ){
                    workingDirectory = new File(".");
                }
            }
        }
        return workingDirectory;
    }
}
