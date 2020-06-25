package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LedgerTransport {
    private Long userId;

    private String username;

    private String role;

    private Long transportId;

    private String companyName;

    private String email;

    private String address;

    private String phone;

    private String avatar;

    private String create_at;

    private String update_at;
}
