package com.duytran.kdtrace.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserContextDto {
    private Long id;
    private String name;
    private String account;
    private String affiliation;
    private String mspId;
}
