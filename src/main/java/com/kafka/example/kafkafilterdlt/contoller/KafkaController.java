package com.kafka.example.kafkafilterdlt.contoller;

import com.kafka.example.kafkafilterdlt.model.KafkaListenerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kafka")
public class KafkaController {

    private final KafkaListenerEndpointRegistry registry;

    @GetMapping(path = "/listeners", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Boolean>> getListeners() {
        var containersMap = registry.getListenerContainers().stream().collect(Collectors.toMap(MessageListenerContainer::getListenerId, MessageListenerContainer::isRunning));
        return ResponseEntity.ok(containersMap);
    }

    @PostMapping(path = "/listeners", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Boolean>> getListeners(@RequestBody final KafkaListenerRequest kafkaListenerRequest) {
        if (kafkaListenerRequest.getOperation().equals(KafkaListenerRequest.Operation.START)) {
            Objects.requireNonNull(registry.getListenerContainer(kafkaListenerRequest.getContainerId())).start();
        } else if (kafkaListenerRequest.getOperation().equals(KafkaListenerRequest.Operation.STOP)) {
            Objects.requireNonNull(registry.getListenerContainer(kafkaListenerRequest.getContainerId())).stop();
        }
        var containersMap = registry.getListenerContainers().stream().collect(Collectors.toMap(MessageListenerContainer::getListenerId, MessageListenerContainer::isRunning));
        return ResponseEntity.ok(containersMap);
    }
}
