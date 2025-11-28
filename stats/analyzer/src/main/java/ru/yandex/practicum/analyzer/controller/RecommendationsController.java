package ru.yandex.practicum.analyzer.controller;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import stats.service.collector.RecommendationsControllerGrpc;
import stats.service.collector.Recommendations;
import ru.yandex.practicum.analyzer.entity.RecommendedEvent;
import ru.yandex.practicum.analyzer.service.RecommendationService;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class RecommendationsController
        extends RecommendationsControllerGrpc.RecommendationsControllerImplBase {

    private final RecommendationService recService;

    @Override
    public void getSimilarEvents(Recommendations.SimilarEventsRequestProto request,
                                 StreamObserver<Recommendations.RecommendedEventProto> responseObserver) {
        try {
            List<RecommendedEvent> list = recService.getSimilarEvents(request);
            for (RecommendedEvent re : list) {
                Recommendations.RecommendedEventProto proto = Recommendations.RecommendedEventProto.newBuilder()
                        .setEventId(re.eventId())
                        .setScore(re.score())
                        .build();
                responseObserver.onNext(proto);
            }
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            log.error("Invalid Argument getSimilarEvents: {}", e.getMessage(), e);
            responseObserver.onError(
                    new StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).withCause(e))
            );
        } catch (Exception e) {
            log.error("Error getSimilarEvents: {}", e.getMessage(), e);
            responseObserver.onError(
                    new StatusRuntimeException(Status.UNKNOWN.withDescription("Error unknown").withCause(e))
            );
        }
    }

    @Override
    public void getRecommendationsForUser(Recommendations.UserPredictionsRequestProto request,
                                          StreamObserver<Recommendations.RecommendedEventProto> responseObserver) {
        try {
            List<RecommendedEvent> list = recService.getRecommendationsForUser(request);
            for (RecommendedEvent re : list) {
                Recommendations.RecommendedEventProto proto = Recommendations.RecommendedEventProto.newBuilder()
                        .setEventId(re.eventId())
                        .setScore(re.score())
                        .build();
                responseObserver.onNext(proto);
            }
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            log.error("Invalid Argument getRecommendationsForUser: {}", e.getMessage(), e);
            responseObserver.onError(
                    new StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).withCause(e))
            );
        } catch (Exception e) {
            log.error("Error getRecommendationsForUser: {}", e.getMessage(), e);
            responseObserver.onError(
                    new StatusRuntimeException(Status.UNKNOWN.withDescription("Error Unknown").withCause(e))
            );
        }
    }

    @Override
    public void getInteractionsCount(Recommendations.InteractionsCountRequestProto request,
                                     StreamObserver<Recommendations.RecommendedEventProto> responseObserver) {
        try {
            List<RecommendedEvent> list = recService.getInteractionsCount(request);
            for (RecommendedEvent re : list) {
                Recommendations.RecommendedEventProto proto = Recommendations.RecommendedEventProto.newBuilder()
                        .setEventId(re.eventId())
                        .setScore(re.score())
                        .build();
                responseObserver.onNext(proto);
            }
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            log.error("Invalid Argument getInteractionsCount: {}", e.getMessage(), e);
            responseObserver.onError(
                    new StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).withCause(e))
            );
        } catch (Exception e) {
            log.error("Error getInteractionsCount: {}", e.getMessage(), e);
            responseObserver.onError(
                    new StatusRuntimeException(Status.UNKNOWN.withDescription("Error Unknown").withCause(e))
            );
        }
    }
}