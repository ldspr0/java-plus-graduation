package ru.yandex.practicum.collector.mapper;

import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.ewm.stats.avro.ActionTypeAvro;
import stats.service.collector.ActionTypeProto;
import stats.service.collector.UserActionProto;

import java.time.Instant;


public class UserActionMapper {

    public static UserActionAvro toAvro(UserActionProto proto) {
        Instant instant = Instant.ofEpochSecond(
                proto.getTimestamp().getSeconds(),
                proto.getTimestamp().getNanos()
        );

        return UserActionAvro.newBuilder()
                .setUserId(proto.getUserId())
                .setEventId(proto.getEventId())
                .setActionType(toAvroActionType(proto.getActionType()))
                .setTimestamp(instant)
                .build();
    }

    private static ActionTypeAvro toAvroActionType(ActionTypeProto protoType) {
        return switch (protoType) {
            case ACTION_REGISTER -> ActionTypeAvro.REGISTER;
            case ACTION_LIKE -> ActionTypeAvro.LIKE;
            default -> ActionTypeAvro.VIEW;
        };
    }
}