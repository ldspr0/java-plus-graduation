package ru.yandex.practicum.explore.with.me;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(scanBasePackages = {
        "ru.yandex.practicum.explore.with.me",
        "ru.yandex.practicum.stat",
        "ru.yandex.practicum.core_api"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {
        "ru.yandex.practicum.core_api.feign"
})
public class MainApp {
    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }
}
