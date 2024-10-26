package com.WebDev.StockQuotes.service;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AlphaService {
	private final WebClient webClient;
	public AlphaService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://www.alphavantage.co").build();
    }

    public Mono<String> getGlobalQuote(String symbol) {
        return this.webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/query")
                .queryParam("function", "GLOBAL_QUOTE")
                .queryParam("symbol", symbol)
                .queryParam("apikey", "IZ8RR2O50IK1DVFJ")
                .build())
            .retrieve()
            .bodyToMono(String.class);
    }

}
