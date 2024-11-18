package com.kafka.example.kafkafilterdlt.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.example.kafkafilterdlt.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaFilteredListener {

    private final ObjectMapper mapper;
    @RetryableTopic(attempts = "${spring.kafka.consumer.retry}",
            backoff = @Backoff(delay = 1_000, multiplier = 2, maxDelay = 16_000),
            autoCreateTopics = "${spring.kafka.consumer.autocreate-topics}",
            autoStartDltHandler = "${spring.kafka.consumer.dlt-handler.auto-startup}",
            dltStrategy = DltStrategy.FAIL_ON_ERROR)
    @KafkaListener(id = "PrioritisedListener",
            topics = "${kafka.topic}",
            groupId = "${spring.kafka.consumer.group-id-prioritised}",
            autoStartup = "${spring.kafka.consumer.auto-startup}",
            concurrency = "${spring.kafka.consumer.concurrency}",
            filter = "recordHeaderFilterStrategyPrioritised",
            clientIdPrefix = "com.kafka.example.kafka-filter-dlt-client")
    public void prioritisedListener(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
        processRecordForConsumer(consumerRecord, "Prioritised");
    }

    @RetryableTopic(attempts = "${spring.kafka.consumer.retry}",
            backoff = @Backoff(delay = 1_000, multiplier = 2, maxDelay = 16_000),
            autoCreateTopics = "${spring.kafka.consumer.autocreate-topics}",
            autoStartDltHandler = "${spring.kafka.consumer.dlt-handler.auto-startup}",
            dltStrategy = DltStrategy.FAIL_ON_ERROR)
    @KafkaListener(id = "NonPrioritisedListener",
            topics = "${kafka.topic}",
            groupId = "${spring.kafka.consumer.group-id-non-prioritised}",
            autoStartup = "${spring.kafka.consumer.auto-startup}",
            concurrency = "${spring.kafka.consumer.concurrency}",
            filter = "recordHeaderFilterStrategyNonPrioritised",
            clientIdPrefix = "com.kafka.example.kafka-filter-dlt-client")
    public void nonPrioritisedListener(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
        processRecordForConsumer(consumerRecord,"Non-Prioritised");
    }

    @DltHandler
    public void dltListener(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
        processRecordForConsumer(consumerRecord,"DLT");
    }

    private void processRecordForConsumer(ConsumerRecord<String, String> consumerRecord, String consumer) throws JsonProcessingException {
        var eventType = new String(consumerRecord.headers().lastHeader("eventType").value());
        log.info("Message consumed by {} consumer. EventType: {}, topic {}, offset {}, header {}, message {}", consumer, eventType, consumerRecord.topic(),
                consumerRecord.offset(), consumerRecord.headers(), consumerRecord.value());
        var user = mapper.readValue(consumerRecord.value(), User.class);
        log.info("Processed event type {} message successfully in {} consumer. User details: {}", eventType, consumer, user.toString());
    }
}
