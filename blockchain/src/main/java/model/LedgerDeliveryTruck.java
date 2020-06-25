package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LedgerDeliveryTruck {
    private Long id;

    private String numberPlate;

    private String autoMaker;

    private String status;

    private String transportId;

    private String create_at;
}
