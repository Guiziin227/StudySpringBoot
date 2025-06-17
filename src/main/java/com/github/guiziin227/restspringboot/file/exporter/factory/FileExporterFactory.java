package com.github.guiziin227.restspringboot.file.exporter.factory;

import com.github.guiziin227.restspringboot.exception.BadRequestException;
import com.github.guiziin227.restspringboot.file.exporter.contract.FileExporter;
import com.github.guiziin227.restspringboot.file.exporter.impl.CsvExporter;
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
    public FileExporter getFileExporter(String fileName) {
        logger.info("Criando o FileExporter");
        if (fileName.endsWith(".xlsx")) {
            logger.info("Criando o XlsxExporter");
            return applicationContext.getBean(XlsxExporter.class);
            //return new XlsxExporter();
        } else if (fileName.endsWith(".csv")) {
            logger.info("Criando o CsvExporter");
            return applicationContext.getBean( CsvExporter.class);
        } else {
            logger.error("Tipo de arquivo não suportado: {}", fileName);
            throw new BadRequestException();
        }

    }
}
