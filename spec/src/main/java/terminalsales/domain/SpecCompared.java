package terminalsales.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import terminalsales.domain.*;
import terminalsales.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class SpecCompared extends AbstractEvent {

    private Long id;
    private UserId userId;
    private List<SpecId> specId;

    public SpecCompared(SpecComparation aggregate) {
        super(aggregate);
    }

    public SpecCompared() {
        super();
    }
}
//>>> DDD / Domain Event
