package com.example.demo.config;

import com.example.demo.model.user.UserEntity;
import com.example.demo.security.UserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.messaging.DefaultMessageListenerContainer;
import org.springframework.data.mongodb.core.messaging.MessageListenerContainer;

import java.util.Optional;

@Configuration
@EnableMongoAuditing
public class MongoConfig {

    @Bean
    AuditorAware<String> auditorAware(UserContext context) {
        return () -> Optional.ofNullable(context.get()).map(UserEntity::getId);
    }

    @Bean
    MessageListenerContainer messageListenerContainer(MongoTemplate mongoTemplate){
         return new DefaultMessageListenerContainer(mongoTemplate);
    }

}
