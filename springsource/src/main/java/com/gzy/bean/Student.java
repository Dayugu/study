package com.gzy.bean;

import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class Student {

    private String name = "gzy";

    private Integer age;
}
