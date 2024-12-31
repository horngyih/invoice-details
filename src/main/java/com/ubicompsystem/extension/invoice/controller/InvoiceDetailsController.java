package com.ubicompsystem.extension.invoice.controller;

import com.ubicompsystem.extension.invoice.model.InvoiceDetailsDTO;
import com.ubicompsystem.extension.invoice.service.DuplicateInvoiceException;
import com.ubicompsystem.extension.invoice.service.InvoiceDetailService;
import com.ubicompsystem.extension.invoice.service.NoInvoiceFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/invoice")
public class InvoiceDetailsController {
    public static final String DETAILS_FORM = "details-form";
    public static final String SUCCESS = "invoice-success";
    public static final String ERROR = "invoice-error";

    @Autowired
    InvoiceDetailService invoiceDetailService;

    @RequestMapping( value = "/register")
    public String registerInvoice(@RequestParam String invoiceID, RedirectAttributes redirectAttributes ){
        try {
            invoiceDetailService.registerInvoice(invoiceID);
            InvoiceDetailsDTO invoiceDetails = new InvoiceDetailsDTO();
            invoiceDetails.setInvoiceID(invoiceID);
            redirectAttributes.addFlashAttribute("invoiceDetails", invoiceDetails);
        } catch (DuplicateInvoiceException e) {
            return "redirect:/invoice/error";
        }
        return "redirect:/invoice/success";
    }

    @GetMapping(produces=MediaType.TEXT_HTML_VALUE)
    public String invoiceDetails( @RequestParam String invoiceID, Model model, RedirectAttributes redirectAttributes ){
        InvoiceDetailsDTO invoiceDetails = null;
        try {
            invoiceDetails = invoiceDetailService.retrieveInvoice(invoiceID);
        } catch (NoInvoiceFoundException|DuplicateInvoiceException e) {
            redirectAttributes.addFlashAttribute( "errorMsg", e.getMessage() );
            return "redirect:/invoice/error";
        }

        if( invoiceDetails == null ){
            redirectAttributes.addFlashAttribute( "errorMsg", "No such invoice ID registered" );
            return "redirect:/invoice/error";
        }
        model.addAttribute( "invoiceDetails", invoiceDetails);
        return DETAILS_FORM;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String saveInvoiceDetails(InvoiceDetailsDTO invoiceDetails, RedirectAttributes redirectAttributes ){
        try {
            invoiceDetails = invoiceDetailService.updateInvoice(invoiceDetails);
        } catch (NoInvoiceFoundException|DuplicateInvoiceException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/invoice/error";
        }
        redirectAttributes.addFlashAttribute("invoiceDetails", invoiceDetails);
        return "redirect:/invoice/success";
    }

    @GetMapping(value = "/success", produces = MediaType.TEXT_HTML_VALUE)
    public String success(){
        return SUCCESS;
    }

    @GetMapping(value="/error", produces = MediaType.TEXT_HTML_VALUE )
    public String error( Model model  ){
        return ERROR;
    }

}
