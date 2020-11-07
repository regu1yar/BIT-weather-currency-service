package bit.wcservice.services.dataloaders;

import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

public class WebLoader {
    private final WebClient webClient;

    public WebLoader(String baseUrl) {
        webClient = WebClient.create(baseUrl);
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