package com.highschool.highschoolsystem.util.mapper;

import com.highschool.highschoolsystem.config.Role;
import com.highschool.highschoolsystem.entity.StudentEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ExcelMapper {
    public static List<StudentEntity> mapStudents(Sheet sheet, PasswordEncoder passwordEncoder) {
        List<StudentEntity> students = new ArrayList<>();

        int row = 0;

        Iterator<Row> rows = sheet.iterator();
        while (rows.hasNext()) {
            Row currentRow = rows.next();
            if (row == 0) {
                row++;
                continue;
            }

            Iterator<Cell> cells = currentRow.iterator();
            StudentEntity student = new StudentEntity();
            int cellIndex = 0;

            while (cells.hasNext()) {
                Cell currentCell = cells.next();
                switch (cellIndex) {
                    case 0: {
                        student.setCardId(currentCell.getStringCellValue());
                        break;
                    }
                    case 1: {
                        student.setFullName(currentCell.getStringCellValue());
                        break;
                    }
                    case 2: {
                        student.setName(currentCell.getStringCellValue());
                        break;
                    }
                    case 3: {
                        String password = currentCell.getStringCellValue();
                        student.setPassword(passwordEncoder.encode(password));
                        break;
                    }
                    case 4: {
                        student.setEmail(currentCell.getStringCellValue());
                        break;
                    }
                    case 5: {
                        Integer gender = (int) currentCell.getNumericCellValue();
                        student.setGender(gender == 1);
                        break;
                    }
                    case 6: {
                        student.setLocation(currentCell.getStringCellValue());
                        break;
                    }
                    case 7: {
                        Date enteredDate = currentCell.getDateCellValue();
                        student.setEnteredDate(enteredDate.toString());
                        break;
                    }
                    default:
                        break;
                }
                cellIndex++;
            }
            student.setRole(Role.STUDENT);
            students.add(student);
        }

        return students;
    }
}
