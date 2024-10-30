package com.example.greenproject.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MailBody {
    private String to;
    private String subject;
    private String text;
}
