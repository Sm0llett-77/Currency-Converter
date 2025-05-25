package com.example.currency.controller

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.util.concurrent.Executors

@Controller
class CurrencyController(
    @Value("\${app.openexchangerates.url}") private val apiUrl: String,
    @Value("\${app.openexchangerates.app_id}") private val appId: String
) {
    private lateinit var client: WebClient
    private var currencies: List<String> = listOf()

    @PostConstruct
    fun init() {
        client = WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(HttpClient.create()))
            .baseUrl(apiUrl)
            .build()

        currencies = listOf("USD", "EUR", "RUB", "GBP", "JPY", "CNY")
    }

    @GetMapping("/")
    suspend fun index(model: Model): String {
        model.addAttribute("currencies", currencies)
        return "index"
    }

    @GetMapping("/rate")
    suspend fun rate(@RequestParam from: String, @RequestParam to: String, model: Model): String {
        model.addAttribute("currencies", currencies)
        return try {
            val rate = withContext(Dispatchers.IO) {
                val response = client.get()
                    .uri("?app_id={appId}", appId)
                    .retrieve()
                    .bodyToMono(Map::class.java)
                    .block()

                val ratesMap = response?.get("rates") as? Map<*, *>
                    ?: throw IllegalArgumentException("Rates not found")

                val fromRate = (ratesMap[from] as? Number)?.toDouble()
                    ?: throw IllegalArgumentException("Unknown currency: $from")

                val toRate = (ratesMap[to] as? Number)?.toDouble()
                    ?: throw IllegalArgumentException("Unknown currency: $to")

                toRate / fromRate
            }

            model.addAttribute("rate", "%.4f".format(rate))
            "index"
        } catch (e: Exception) {
            model.addAttribute("rate", "Error: ${e.message}")
            "index"
        }
    }

    @PostMapping("/shutdown")
    fun shutdown(): String {
        Executors.newSingleThreadExecutor().submit {
            Thread.sleep(1000)
            System.exit(0)
        }
        return "redirect:/"
    }
}