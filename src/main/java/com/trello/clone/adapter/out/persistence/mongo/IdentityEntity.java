package com.trello.clone.adapter.out.persistence.mongo;

import java.util.UUID;

import com.trello.clone.common.persistence.BaseMongoEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "identity")
public class IdentityEntity extends BaseMongoEntity {

    @Id
    private UUID id;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String emailAddress;

    private String password;

    private UUID sessionId;

    // References
    private UUID userId;
}
