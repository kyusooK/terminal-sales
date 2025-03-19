package terminalsales.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import terminalsales.UserApplication;

@Entity
@Table(name = "User_table")
@Data
//<<< DDD / Aggregate Root
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String region;

    private String gender;

    private String coupon;

    public static UserRepository repository() {
        UserRepository userRepository = UserApplication.applicationContext.getBean(
            UserRepository.class
        );
        return userRepository;
    }

    //<<< Clean Arch / Port Method
    public static void addCoupon(DiscountPolicyActivated discountPolicyActivated) {

        // discountPolicyActivated에 등록된 customerId를 통해 User에 저장된 정보중 같은 정보를 가진 id 조회
        repository().findById(discountPolicyActivated.getCustomerId()).ifPresent(user->{
            
            user.setCoupon("20% 할인 쿠폰");
            repository().save(user);
        });

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
