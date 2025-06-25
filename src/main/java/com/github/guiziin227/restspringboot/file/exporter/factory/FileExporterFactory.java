package com.github.guiziin227.restspringboot.file.exporter.factory;

import com.github.guiziin227.restspringboot.exception.BadRequestException;
import com.github.guiziin227.restspringboot.file.exporter.MediaTypes;
import com.github.guiziin227.restspringboot.file.exporter.contract.FileExporter;
import com.github.guiziin227.restspringboot.file.exporter.impl.CsvExporter;
import com.github.guiziin227.restspringboot.file.exporter.impl.PdfExporter;
import com.github.guiziin227.restspringboot.file.exporter.impl.XlsxExporter;
import com.github.guiziin227.restspringboot.file.importer.contract.FileImporter;
import com.github.guiziin227.restspringboot.file.importer.impl.CsvImporter;
import com.github.guiziin227.restspringboot.file.importer.impl.XlsxImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@Component //Para poder injetar esta classe em outras classes
public class FileExporterFactory {

    private Logger logger = LoggerFactory.getLogger(FileExporterFactory.class);

    @Autowired
    private ApplicationContext applicationContext;

    //usando o padrao de projeto Factory Method
    public FileExporter getFileExporter(String acceptHeader) {
        logger.info("Criando o FileExporter");
        if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_XLSX)) {
            logger.info("Criando o XlsxExporter");
            return applicationContext.getBean(XlsxExporter.class);
            //return new XlsxExporter();
        } else if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_CSV)) {
            logger.info("Criando o CsvExporter");
            return applicationContext.getBean(CsvExporter.class);
        } else if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_PDF)) {
            logger.info("Criando o PdfExporter");
            return applicationContext.getBean(PdfExporter.class);
        } else {
            logger.error("Tipo de arquivo não suportado: {}", acceptHeader);
            throw new BadRequestException();
        }

    }
}
