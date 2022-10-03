package com.trello.clone.common.persistence;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class BaseMongoEntity {

    @Version
    private Long version;

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date modifiedDate;

    @CreatedBy
    private UUID createdBy;

    @LastModifiedBy
    private UUID lastModifiedBy;
}
