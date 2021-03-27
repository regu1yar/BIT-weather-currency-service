package bit.wcservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("bit.utils.database.entity.datarecord")
public class WCServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WCServiceApplication.class, args);
    }

}
