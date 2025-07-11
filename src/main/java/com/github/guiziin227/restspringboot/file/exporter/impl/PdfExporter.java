package com.github.guiziin227.restspringboot.file.exporter.impl;

import com.github.guiziin227.restspringboot.dto.PersonDTO;
import com.github.guiziin227.restspringboot.file.exporter.contract.FileExporter;
import com.github.guiziin227.restspringboot.service.QRCodeService;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PdfExporter implements FileExporter {

    @Autowired
    private QRCodeService qrCodeService;


    @Override
    public Resource exportPeople(List<PersonDTO> people) throws Exception {

        InputStream inputStream = getClass().getResourceAsStream("/templates/people.jrxml");

        if (inputStream == null) {
            throw new RuntimeException("Template file not found");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(people);
        Map<String, Object> parameters = new HashMap<String, Object>();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
            return new ByteArrayResource(baos.toByteArray());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error exporting PDF", e);
        }
    }

    @Override
    public Resource exportPerson(PersonDTO person) throws Exception {
        InputStream mainStream = getClass().getResourceAsStream("/templates/person.jrxml");

        if (mainStream == null) {
            throw new RuntimeException("Template file not found");
        }

        InputStream subStream = getClass().getResourceAsStream("/templates/books.jrxml");

        if (subStream == null) {
            throw new RuntimeException("Template file not found");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(mainStream);
        JasperReport subReport = JasperCompileManager.compileReport(subStream);

        InputStream qrCodeStream = qrCodeService.generateQRCode(person.getProfileUrl(), 200, 200);

        String path = getClass().getResource("/templates/books.jasper").getPath();

        JRBeanCollectionDataSource  subSource = new JRBeanCollectionDataSource(person.getBooks());
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("SUB_REPORT_DATA_SOURCE", subSource);
        parameters.put("BOOK_SUB_REPORT", subReport);
        parameters.put("SUB_REPORT_DIR", path);
        parameters.put("QR_CODEIMAGE", qrCodeStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList(person));


        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
            return new ByteArrayResource(baos.toByteArray());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error exporting PDF", e);
        }
    }
}
