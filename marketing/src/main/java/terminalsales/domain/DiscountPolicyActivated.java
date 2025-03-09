package terminalsales.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import terminalsales.domain.*;
import terminalsales.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class DiscountPolicyActivated extends AbstractEvent {

    private Long id;
    private Long customerId;
    private String productId;

    public DiscountPolicyActivated(Retargetting aggregate) {
        super(aggregate);
    }

    public DiscountPolicyActivated() {
        super();
    }
}
//>>> DDD / Domain Event
