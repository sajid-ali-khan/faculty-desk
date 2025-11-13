package com.sajid.college_app.models.raw;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class Employee {
    @CsvBindByName(column = "empid", required = true)
    private String empId;

    @CsvBindByName(column = "pwd", required = true)
    private String pwd;

    @CsvBindByName(column = "gender", required = false)
    private String gender;
    
    @CsvBindByName(column = "salu", required = false)
    private String salutation;

    @CsvBindByName(column = "name", required = false)
    private String name;
}
