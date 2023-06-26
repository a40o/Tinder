package com.volasoftware.tinder.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

import static jakarta.persistence.TemporalType.TIMESTAMP;

@NoArgsConstructor
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<U> {

    @Column(name = "CREATED_DATE")
    protected LocalDateTime createdDate;

    @Column(name = "LAST_MODIFIED")
    protected LocalDateTime lastModified;

    @PrePersist
    private void init(){
        createdDate = LocalDateTime.now();
    }

    @PreUpdate
    private void modify(){
        lastModified = LocalDateTime.now();
    }
}
