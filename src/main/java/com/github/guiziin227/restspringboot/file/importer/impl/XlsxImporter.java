package com.github.guiziin227.restspringboot.file.importer.impl;

import com.github.guiziin227.restspringboot.dto.PersonDTO;
import com.github.guiziin227.restspringboot.file.importer.contract.FileImporter;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class XlsxImporter implements FileImporter {

    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws Exception {

        try(XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();// Skip header row
            }

            return parseRowsToPersonDTOs(rowIterator);
        }
    }

    private List<PersonDTO> parseRowsToPersonDTOs(Iterator<Row> rowIterator) {
        List<PersonDTO> personDTOs = new ArrayList<>();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getCell(0) != null && row.getCell(0).getCellType() == CellType.BLANK) {
                PersonDTO personDTO = new PersonDTO();
                personDTO.setFirstName(row.getCell(0).getStringCellValue());
                personDTO.setLastName(row.getCell(1).getStringCellValue());
                personDTO.setAddress(row.getCell(2).getStringCellValue());
                personDTO.setGender(row.getCell(3).getStringCellValue());
                personDTO.setEnabled(true);
                personDTOs.add(personDTO);
            }
        }
        return personDTOs;
    }
}
