package com.kafka.example.kafkafilterdlt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaListenerRequest {
    private String containerId;
    private Operation operation;

    public enum Operation {
        START, STOP
    }
}
