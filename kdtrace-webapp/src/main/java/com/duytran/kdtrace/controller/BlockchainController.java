package com.duytran.kdtrace.controller;

import com.duytran.kdtrace.dto.UserContextDto;
import com.duytran.kdtrace.service.BlockchainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/blockchain")
@Slf4j
public class BlockchainController {

    private final BlockchainService blockchainService;

    @Autowired
    public BlockchainController(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping({"/users/identities/register"})
    public UserContextDto registerIdentity(
            @RequestParam(value = "organization", defaultValue = "Org1") String organization,
            @RequestParam(value = "username", defaultValue = "") String username
    ) {
        return blockchainService.registerIdentity(username, organization);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping({"/identity/admin/enroll"})
    public UserContextDto enrollAdminIdentity(
            @RequestParam(value = "organization", defaultValue = "Org1") String organization
    ) {
        return blockchainService.enrollAdmin(organization);
    }
}
