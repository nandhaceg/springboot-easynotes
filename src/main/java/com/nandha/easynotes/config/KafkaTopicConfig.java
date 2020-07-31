package com.nandha.easynotes.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import com.nandha.easynotes.properties.ApplicationProperties;

@Configuration
public class KafkaTopicConfig {
	
	@Autowired
	ApplicationProperties applicationProperties;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, applicationProperties.getBootstrapAddress());
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic stringTopic() {
        return new NewTopic(applicationProperties.getStringTopic(), 1, (short) 1);
    }

    @Bean
    public NewTopic noteTopic() {
        return new NewTopic(applicationProperties.getNoteTopic(), 1, (short) 1);
    }
}
