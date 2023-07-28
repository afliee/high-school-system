package com.highschool.highschoolsystem.util.spreadsheet;

import com.highschool.highschoolsystem.entity.StudentEntity;
import com.highschool.highschoolsystem.util.mapper.ExcelMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

public class ExcelUtil {
    private static final String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String SHEET = "Students";
    private static final String DEFAULT_SHEET = "Sheet1";
    private static final String[] classHeader = new String[] {
            "Card ID",
            "Full Name",
            "UserName",
            "Password Default",
            "Email",
            "Gender",
            "Location",
            "Enter Date",
    };
    public static boolean isExcel(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<StudentEntity> extractStudents(InputStream inputStream, PasswordEncoder passwordEncoder) {
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(SHEET);
            if (sheet == null) sheet = workbook.getSheet(DEFAULT_SHEET);

            List<StudentEntity> students = ExcelMapper.mapStudents(sheet, passwordEncoder);

            workbook.close();
            return students;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream toExcel(List<StudentEntity> students) {
        try {
            Workbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Sheet sheet = workbook.createSheet(SHEET);
            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < classHeader.length; i++) {
                headerRow.createCell(i).setCellValue(classHeader[i]);
            }

            int rowIdx = 1;
            for (StudentEntity student : students) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(student.getCardId());
                row.createCell(1).setCellValue(student.getFullName());
                row.createCell(2).setCellValue(student.getName());
                row.createCell(3).setCellValue(student.getPassword());
                row.createCell(4).setCellValue(student.getEmail());
                row.createCell(5).setCellValue(student.isGender() ? 1 : 0);
                row.createCell(6).setCellValue(student.getLocation());
                row.createCell(7).setCellValue(student.getEnteredDate().toString());
            }

            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
