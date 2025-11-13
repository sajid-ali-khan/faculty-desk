package com.sajid.college_app.models.raw;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @CsvBindByName(column = "roll", required = true)
    private String roll;

    @CsvBindByName(column = "name", required = true)
    private String name;

    @CsvBindByName(column = "branch", required = true)
    private String branch;

    @CsvBindByName(column = "section", required = true)
    private String section;

    @CsvBindByName(column = "semester", required = true)
    private int sem;

    @CsvBindByName(column = "scheme", required = true)
    private String scheme;

    @CsvBindByName(column = "degree", required = true)
    private String degree;
}
