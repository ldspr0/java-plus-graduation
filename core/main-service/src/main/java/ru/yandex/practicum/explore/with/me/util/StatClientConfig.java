package ru.yandex.practicum.explore.with.me.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestClient;
import ru.yandex.practicum.stats.client.StatClient;
import ru.yandex.practicum.stats.client.StatClientImpl;

@Configuration
public class StatClientConfig {

    @Bean
    @Lazy
    public StatClient statClient(@Value("${stats.server-url}") String serverUrl) {
        RestClient client = RestClient.builder()
                .baseUrl(serverUrl)
                .build();

        return new StatClientImpl(client);
    }
}
