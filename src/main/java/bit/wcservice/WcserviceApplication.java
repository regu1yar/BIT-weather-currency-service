package bit.wcservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class WcserviceApplication {

    public static void main(String[] args) {
//        WebClient client = WebClient.create("http://www.cbr.ru");
//        String response = client
//                .get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("scripts/XML_daily.asp")
//                        .queryParam("date_req", "02/03/2002")
//                        .build())
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//        System.out.println(response);

        SpringApplication.run(WcserviceApplication.class, args);
    }

}
