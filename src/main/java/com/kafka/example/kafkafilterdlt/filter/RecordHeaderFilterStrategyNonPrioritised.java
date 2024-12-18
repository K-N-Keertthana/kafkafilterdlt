package com.kafka.example.kafkafilterdlt.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class RecordHeaderFilterStrategyNonPrioritised<K,V> implements RecordFilterStrategy<K,V> {

    @Value("${kafka.eventType.filter.list.non-prioritised.anything-but}")
    private List<String> skipEventTypes;

    @Override
    public boolean filter(ConsumerRecord<K,V> consumerRecord) {
        var eventType = new String(consumerRecord.headers().lastHeader("eventType").value());
        if(skipEventTypes.contains(eventType)){
            log.info("Message skipped on non prioritised filter. EventType: {}, topic {}, offset {}", eventType, consumerRecord.topic(), consumerRecord.offset());
            return true;
        }
        return false;
    }

}
