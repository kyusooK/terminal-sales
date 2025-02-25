package terminalsales.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import terminalsales.MarketingApplication;
import terminalsales.domain.DiscountPolicyActivated;

@Entity
@Table(name = "Retargetting_table")
@Data
//<<< DDD / Aggregate Root
public class Retargetting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String customerId;

    private String productId;

    private Integer returnCount;

    public static RetargettingRepository repository() {
        RetargettingRepository retargettingRepository = MarketingApplication.applicationContext.getBean(
            RetargettingRepository.class
        );
        return retargettingRepository;
    }

    //<<< Clean Arch / Port Method
    public static void increaseTargettingValue(SpecCompared specCompared) {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> specComparationList = mapper.convertValue(specCompared.getSpecId(), List.class);
        Map<String, Object> userMap = mapper.convertValue(specCompared.getCustomerId(), Map.class);
    
        repository().findByCustomerId(userMap.get("id").toString()).ifPresentOrElse(retargetting -> {
            // Retargetting 항목이 존재하는 경우
            retargetting.setReturnCount(retargetting.getReturnCount() + 1); 
            repository().save(retargetting); 

            if (retargetting.getReturnCount() >= 3) {

                terminalsales.external.Reservation reservation = new terminalsales.external.Reservation();
                
                reservation.setTaskId(retargetting.getId().toString());
                reservation.setTitle("쿠폰발행");
                // reservation.setTargetUserIds();
                reservation.setDescription("20% 할인 쿠폰이 발행되었습니다.");
                reservation.setNow(true);

<<<<<<< HEAD
                MarketingApplication.applicationContext.getBean(terminalsales.external.ReservationService.class)
                    .createReservation(reservation);
=======
        /** Example 2:  finding and process
        
        // if specCompared.specIduserId exists, use it
        
        // ObjectMapper mapper = new ObjectMapper();
        // Map<Long, Object> specComparationMap = mapper.convertValue(specCompared.getSpecId(), Map.class);
        // Map<Long, Object> specComparationMap = mapper.convertValue(specCompared.getUserId(), Map.class);
>>>>>>> origin/template

                DiscountPolicyActivated discountPolicyActivated = new DiscountPolicyActivated(retargetting);
                discountPolicyActivated.publishAfterCommit(); // 변경 사항을 커밋한 후 이벤트를 발행
            }
    

        }, () -> {
            // Retargetting 항목이 존재하지 않는 경우
            Retargetting retargetting = new Retargetting();
            retargetting.setCustomerId(userMap.get("id").toString()); 
    
            // specId에서 productId 추출 (specId가 여러 개의 맵으로 이루어진 리스트라고 가정)
            if (!specComparationList.isEmpty()) {
                // 여러 개의 phoneName을 연결하기 위해 StringBuilder 사용
                StringBuilder productIds = new StringBuilder();
                for (Map<String, Object> specMap : specComparationList) {
                    if (specMap.containsKey("phoneName")) {
                        productIds.append(specMap.get("phoneName").toString()).append(", "); 
                    }
                }
                // productIds가 비어있지 않으면 마지막 쉼표와 공백 제거
                if (productIds.length() > 0) {
                    productIds.setLength(productIds.length() - 2); 
                }
                retargetting.setProductId(productIds.toString());
            }
    
            retargetting.setReturnCount(1); 
            repository().save(retargetting); 
    
            
        });
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
