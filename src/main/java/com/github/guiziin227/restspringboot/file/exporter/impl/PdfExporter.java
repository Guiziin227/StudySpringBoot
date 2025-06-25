package com.github.guiziin227.restspringboot.file.exporter.impl;

import com.github.guiziin227.restspringboot.dto.PersonDTO;
import com.github.guiziin227.restspringboot.file.exporter.contract.FileExporter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PdfExporter implements FileExporter {


    @Override
    public Resource exportFile(List<PersonDTO> people) throws Exception {
        return null;
    }
}
