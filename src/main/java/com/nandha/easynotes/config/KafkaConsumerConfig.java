package com.nandha.easynotes.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.nandha.easynotes.model.Note;
import com.nandha.easynotes.properties.ApplicationProperties;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
	
	@Autowired
	ApplicationProperties applicationProperties;

	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, applicationProperties.getBootstrapAddress());
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "group-id");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(props);
   	}

	@Bean
   	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
   		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
   		factory.setConsumerFactory(consumerFactory());
   		return factory;
   	}
   	
	@Bean
   	public ConsumerFactory<String, Note> noteConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, applicationProperties.getBootstrapAddress());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "note");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(Note.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Note> noteKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Note> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(noteConsumerFactory());
        return factory;
    }
}
