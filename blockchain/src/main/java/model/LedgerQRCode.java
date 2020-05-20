package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LedgerQRCode {
    private Long id;

    private Long productId;

    private Long processId;

    private String code;

    private String ower;

    private String link;

    private String statusQRCode;

    private String create_at;
}
