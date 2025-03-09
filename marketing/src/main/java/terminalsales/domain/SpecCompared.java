package terminalsales.domain;

import java.util.*;
import lombok.*;
import terminalsales.domain.*;
import terminalsales.infra.AbstractEvent;

@Data
@ToString
public class SpecCompared extends AbstractEvent {

    private Long id;
    private Object userId;
    private Object specId;
}
