package bit.utils;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

public class WebLoader {

    private final WebClient webClient;

    public WebLoader(WebClient webClient) {
        this.webClient = webClient;
    }

    public String loadData(String path, MultiValueMap<String, String> queryArguments) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParams(queryArguments)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
