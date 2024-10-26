package com.WebDev.StockQuotes.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import com.WebDev.StockQuotes.service.AlphaService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;

@Controller
public class StockQuotesController {
    private final AlphaService alphaService;
    public StockQuotesController(AlphaService alphaService) {
        this.alphaService = alphaService;
    }

    @GetMapping("/")
    public String showHomePage() {
        return "index";
    }

    @GetMapping("/quote-view")
    public String getGlobalQuoteView(@RequestParam String symbol, Model model) {
        String response = alphaService.getGlobalQuote(symbol).block();

        if (response == null || response.isEmpty()) {
            model.addAttribute("error", "No data found for symbol: " + symbol);
            return "error";
        } 
        
        Map<String, String> quoteData = parseQuoteResponse(response);
        if (quoteData.isEmpty()) {
            model.addAttribute("error", "Unable to retrieve data for symbol: " + symbol);
            return "error";
        }
        model.addAttribute("symbol", symbol);
        model.addAttribute("quoteData", quoteData);
        return "quote";
    }

    private Map<String, String> parseQuoteResponse(String response) {
        Map<String, String> quoteData = new LinkedHashMap<>();
        String[] orderedKeys = {
                "01. symbol", "02. open", "03. high", "04. low",
                "05. price", "06. volume", "07. latest trading day",
                "08. previous close", "09. change", "10. change percent"
        };

        String[] lines = response.split("\n");
        for (String key : orderedKeys) {
            for (String line : lines) {
                line = line.trim();
                if (line.startsWith("\"Global Quote")) {
                    continue;
                }
                if (line.startsWith("\"" + key)) {
                    String[] parts = line.split(":");
                    if (parts.length > 1) {
                        String value = parts[1].replace("\"", "").replace(",", "").trim();
                        quoteData.put(key, value);
                    }
                    break; // Exit inner loop once the key is found
                }
            }
        }
        return quoteData;
    }

}
