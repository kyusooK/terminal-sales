package terminalsales.domain;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import terminalsales.domain.*;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "salesData", path = "salesData")
public interface SalesDataRepository
    extends PagingAndSortingRepository<SalesData, Long> {}
