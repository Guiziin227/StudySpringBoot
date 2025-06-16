package com.github.guiziin227.restspringboot.file.importer.factory;

import com.github.guiziin227.restspringboot.exception.BadRequestException;
import com.github.guiziin227.restspringboot.file.importer.contract.FileImporter;
import com.github.guiziin227.restspringboot.file.importer.impl.CsvImporter;
import com.github.guiziin227.restspringboot.file.importer.impl.XlsxImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@Component //Para poder injetar esta classe em outras classes
public class FileImporterFactory {

    private Logger logger = LoggerFactory.getLogger(FileImporterFactory.class);

    @Autowired
    private ApplicationContext applicationContext;

    //usando o padrao de projeto Factory Method
    public FileImporter getFileImporter(String fileName) {
        logger.info("Criando o FileImporter");
        if (fileName.endsWith(".xlsx")) {
            logger.info("Criando o XlsxImporter");
            return applicationContext.getBean(XlsxImporter.class);
            //return new XlsxImporter();
        } else if (fileName.endsWith(".csv")) {
            logger.info("Criando o CsvImporter");
            return applicationContext.getBean( CsvImporter.class);
        } else {
            logger.error("Tipo de arquivo não suportado: {}", fileName);
            throw new BadRequestException();
        }

    }
}
