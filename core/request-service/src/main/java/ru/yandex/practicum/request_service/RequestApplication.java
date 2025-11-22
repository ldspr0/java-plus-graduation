package ru.yandex.practicum.request_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {
        "ru.yandex.practicum.request_service",
        "ru.yandex.practicum.core_api.exception"
})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {
        "ru.yandex.practicum.core_api.feign"
})
public class RequestApplication {
    public static void main(String[] args) {
        SpringApplication.run(RequestApplication.class, args);
    }
}