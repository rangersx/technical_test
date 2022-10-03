package com.trello.clone.common.config.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@Configuration
public class MongoReplacementConfig {

    /**
     * map dot (.) with "_" when saving and replacing it with dot (.) again when it is retrieved
     */
    @Autowired
    public void setMapKeyDotReplacement(MappingMongoConverter mongoConverter) {
        mongoConverter.setMapKeyDotReplacement("_");
    }
}
