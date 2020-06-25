package com.duytran.kdtrace.entity;

import java.io.Serializable;

public enum RoleName implements Serializable {
    ROLE_ADMIN,
    ROLE_USER,
    ROLE_PRODUCER,
    ROLE_TRANSPORT,
    ROLE_DISTRIBUTOR;

    @Override
    public String toString() {
        return name();
    }
}
