package com.duytran.kdtrace.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/producer")
@PreAuthorize(("hasRole('ROLE_PRODUCER')"))
public class ProducerController {

}