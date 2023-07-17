package com.highschool.highschoolsystem.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BreadCrumb {
    private String name;
    private String url;
    private boolean isActive;
}
