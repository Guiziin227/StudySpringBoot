package com.github.guiziin227.restspringboot.file.exporter.impl;

import com.github.guiziin227.restspringboot.dto.PersonDTO;
import com.github.guiziin227.restspringboot.file.exporter.contract.FileExporter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class XlsxExporter implements FileExporter {

    @Override
    public Resource exportPeople(List<PersonDTO> people) throws Exception {
        try(Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("People");

            Row headerRow = sheet.createRow(0);
            String[] headers = {"First Name", "Last Name", "Address", "Gender", "Enabled"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderCellStyle(workbook));
            }

            int rowNum = 1;
            for (PersonDTO person : people) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(person.getFirstName());
                row.createCell(1).setCellValue(person.getLastName());
                row.createCell(2).setCellValue(person.getAddress());
                row.createCell(3).setCellValue(person.getGender());
                row.createCell(4).setCellValue(person.getEnabled() != null && person.getEnabled() ? "Yes" : "No");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            return new ByteArrayResource(baos.toByteArray());
        } catch (Exception e) {
            throw new Exception("Error while exporting data to XLSX file", e);
        }
    }

    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle headerCellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerCellStyle.setFillForegroundColor((short) 0xA);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setFont(font);
        return headerCellStyle;
    }
}
