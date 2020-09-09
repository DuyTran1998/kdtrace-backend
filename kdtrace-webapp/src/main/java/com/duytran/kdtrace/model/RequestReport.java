package com.duytran.kdtrace.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RequestReport {
    @NotNull
    private String code;

    @Nullable
    private String name;

    @Nullable
    private String phone;

    @NotNull
    private String reportContent;

    @NotNull
    private Float rate;
}
