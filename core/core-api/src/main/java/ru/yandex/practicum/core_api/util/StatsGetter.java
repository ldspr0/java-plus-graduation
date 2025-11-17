package ru.yandex.practicum.core_api.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.core_api.model.event.dto.EventViewsParameters;
import ru.yandex.practicum.stats.client.StatClient;
import ru.yandex.practicum.stats.dto.ViewStats;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatsGetter {
    private final StatClient statClient;

    public List<ViewStats> getEventViewStats(EventViewsParameters params) {
        log.info("STAT GETTER: get event view stats with params: {}", params);
        return statClient.getStats(
                params.getStart(),
                params.getEnd(),
                params.getEventIdUris(),
                params.isUnique()).getBody();
    }
}
