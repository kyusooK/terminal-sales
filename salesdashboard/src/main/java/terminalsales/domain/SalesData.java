package terminalsales.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import terminalsales.SalesdashboardApplication;
import terminalsales.domain.DataCollected;

@Entity
@Table(name = "SalesData_table")
@Data
//<<< DDD / Aggregate Root
public class SalesData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String phoneName;

    private String phoneColor;

    private String gender;

    private String region;

    private Integer price;


    public static SalesDataRepository repository() {
        SalesDataRepository salesDataRepository = SalesdashboardApplication.applicationContext.getBean(
            SalesDataRepository.class
        );
        return salesDataRepository;
    }

    //<<< Clean Arch / Port Method
    public static void collectData(OrderPlaced orderPlaced) {
         // ObjectMapper mapper = new ObjectMapper();
        // Map<Long, Object> orderMap = mapper.convertValue(orderPlaced.getSpecId(), Map.class);
        // Map<Long, Object> orderMap = mapper.convertValue(orderPlaced.getUserId(), Map.class);
        
        DataCollected dataCollected = new DataCollected(this);
        dataCollected.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
