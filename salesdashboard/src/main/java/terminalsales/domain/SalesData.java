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

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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
        ObjectMapper mapper = new ObjectMapper();
        Map<Long, Object> specMap = mapper.convertValue(orderPlaced.getSpecId(), Map.class);
        Map<Long, Object> userMap = mapper.convertValue(orderPlaced.getUserId(), Map.class);
        
        // ID 값 추출 및 변환
        Long specId = Long.valueOf(specMap.get("id").toString());
        Long userId = Long.valueOf(userMap.get("id").toString());
        
        // RestTemplate 생성
        RestTemplate restTemplate = new RestTemplate();
        
        // Spec 정보 가져오기
        String specUrl = "http://localhost:8082/specs/" + specId;
        ResponseEntity<Map> specData = restTemplate.getForEntity(specUrl, Map.class);
        
        // User 정보 가져오기
        String userUrl = "http://localhost:8087/users/" + userId;
        ResponseEntity<Map> userData = restTemplate.getForEntity(userUrl, Map.class);
        
        // 데이터 수집 및 발행
        SalesData salesData = new SalesData();
        salesData.setPhoneName(specData.getBody().get("phoneName").toString());
        salesData.setPhoneColor(specData.getBody().get("phoneColor").toString());
        salesData.setPrice(Integer.valueOf(specData.getBody().get("price").toString()));
        salesData.setGender(userData.getBody().get("gender").toString());
        salesData.setRegion(userData.getBody().get("region").toString());


        DataCollected dataCollected = new DataCollected(salesData);
        dataCollected.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
