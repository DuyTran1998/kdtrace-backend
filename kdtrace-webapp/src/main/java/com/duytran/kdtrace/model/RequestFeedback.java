package com.duytran.kdtrace.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RequestFeedback {
    @NotNull
    private String code;

    private String feedback;
}
