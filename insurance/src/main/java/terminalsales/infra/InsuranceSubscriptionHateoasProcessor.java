package terminalsales.infra;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import terminalsales.domain.*;

@Component
public class InsuranceSubscriptionHateoasProcessor
    implements
        RepresentationModelProcessor<EntityModel<InsuranceSubscription>> {

    @Override
    public EntityModel<InsuranceSubscription> process(
        EntityModel<InsuranceSubscription> model
    ) {
        return model;
    }
}
