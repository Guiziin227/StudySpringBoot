package com.github.guiziin227.restspringboot.file.importer.contract;

import com.github.guiziin227.restspringboot.dto.PersonDTO;

import java.io.InputStream;
import java.util.List;

public interface FileImporter {

    List<PersonDTO> importFile(InputStream inputStream) throws Exception ;
}
