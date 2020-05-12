package model;

import com.duytran.kdtrace.entity.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
public class LedgerProcess {
    private Long id;

    private Long distributorId;

    private List<Long> qrCodes;

    private LedgerDeliveryTruck deliveryTruck;

    private String statusProcess;

    private String delivery_at;

    private String receipt_at;

}
