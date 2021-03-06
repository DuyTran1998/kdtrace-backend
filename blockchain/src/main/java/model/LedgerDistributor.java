package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LedgerDistributor {
    private Long userId;

    private String username;

    private String role;

    private Long distributorId;

    private String companyName;

    private String email;

    private String address;

    private String phone;

    private String avatar;

    private String website;

    private String tin;

    private String create_at;

    private String update_at;
}
