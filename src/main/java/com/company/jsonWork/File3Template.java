package com.company.jsonWork;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class File3Template {

    int id;

    List<String> urls;
}
