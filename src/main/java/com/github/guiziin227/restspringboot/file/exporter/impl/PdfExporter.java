package com.github.guiziin227.restspringboot.file.exporter.impl;

import com.github.guiziin227.restspringboot.dto.PersonDTO;
import com.github.guiziin227.restspringboot.file.exporter.contract.FileExporter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PdfExporter implements FileExporter {


    @Override
    public Resource exportFile(List<PersonDTO> people) throws Exception {

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
        }catch (RuntimeException e) {
            throw new RuntimeException("Error exporting PDF", e);
        }
    }
}
