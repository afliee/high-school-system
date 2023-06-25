package com.highschool.highschoolsystem.util.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDetails {
    private String to;
    private String subject;
    private String content;
    private String pathToAttachment;
}
