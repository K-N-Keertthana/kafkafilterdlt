package com.kafka.example.kafkafilterdlt.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.retrytopic.DestinationTopic;
import org.springframework.kafka.retrytopic.RetryTopicComponentFactory;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationSupport;
import org.springframework.kafka.retrytopic.RetryTopicNamesProviderFactory;
import org.springframework.kafka.retrytopic.SuffixingRetryTopicNamesProviderFactory;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConditionalOnProperty(value = "spring.kafka.consumer.retry.custom-naming", havingValue = "true")
public class CustomNamingStrategy extends RetryTopicConfigurationSupport {


    @Override
    protected RetryTopicComponentFactory createComponentFactory() {
        return new RetryTopicComponentFactory() {
            @Override
            public RetryTopicNamesProviderFactory retryTopicNamesProviderFactory() {
                return new CustomRetryTopicNamesProviderFactory();
            }
        };
    }

    public class CustomRetryTopicNamesProviderFactory implements RetryTopicNamesProviderFactory {

        @Override
        public RetryTopicNamesProvider createRetryTopicNamesProvider(DestinationTopic.Properties properties) {

            if (properties.isMainEndpoint()) {
                return new SuffixingRetryTopicNamesProviderFactory.SuffixingRetryTopicNamesProvider(properties);
            } else if (properties.isRetryTopic()) {
                return new SuffixingRetryTopicNamesProviderFactory.SuffixingRetryTopicNamesProvider(properties) {

                    @Override
                    public String getTopicName(String topic) {
                        return "kafka.internal.retry." + super.getTopicName(topic);
                    }

                };
            } else {
                return new SuffixingRetryTopicNamesProviderFactory.SuffixingRetryTopicNamesProvider(properties) {
                    @Override
                    public String getTopicName(String topic) {
                        return "kafka.internal.dlt." + super.getTopicName(topic);
                    }

                };
            }
        }
    }
}
