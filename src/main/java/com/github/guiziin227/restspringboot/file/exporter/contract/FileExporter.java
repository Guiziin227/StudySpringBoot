package com.github.guiziin227.restspringboot.file.exporter.contract;

import com.github.guiziin227.restspringboot.dto.PersonDTO;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.List;

public interface FileExporter {

    Resource exportFile(List<PersonDTO> people) throws Exception ;
}
