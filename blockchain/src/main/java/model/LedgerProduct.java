package model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class LedgerProduct {
    private Long id;

    private String name;

    private String type;

    private Date mfg;

    private Date exp;

    private List<Long> codes;

    private String producerId;

    private String userId;

    private int quantity;

    private String unit;
}
