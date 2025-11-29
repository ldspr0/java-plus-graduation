package ru.yandex.practicum.collector.service;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import stats.service.collector.UserActionControllerGrpc;
import stats.service.collector.UserActionProto;
import ru.yandex.practicum.collector.mapper.UserActionMapper;
import ru.practicum.ewm.stats.avro.UserActionAvro;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class UserActionController extends UserActionControllerGrpc.UserActionControllerImplBase {

    private final MessageProducer messageProducer;

    @Override
    public void collectUserAction(UserActionProto request, StreamObserver<Empty> responseObserver) {
        try {
            UserActionAvro userActionAvro = UserActionMapper.toAvro(request);
            messageProducer.sendUserAction(userActionAvro);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            log.error("IllegalArgumentException collectUserAction: {}", e.getMessage(), e);
            responseObserver.onError(
                    new StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).withCause(e))
            );
        } catch (Exception e) {
            log.error("Error collectUserAction: {}", e.getMessage(), e);
            responseObserver.onError(
                    new StatusRuntimeException(Status.UNKNOWN.withDescription("Error").withCause(e))
            );
        }
    }
}