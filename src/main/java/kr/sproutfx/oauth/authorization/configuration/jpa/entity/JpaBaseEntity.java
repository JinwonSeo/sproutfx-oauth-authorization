package kr.sproutfx.oauth.authorization.configuration.jpa.entity;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class JpaBaseEntity {
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean deleted = Boolean.FALSE;

    @CreatedDate
    @Column(nullable = false, updatable = false, columnDefinition = "timestamp")
    private LocalDateTime createdOn;

    @CreatedBy
    @Column(nullable = true, updatable = false, columnDefinition = "varchar(36)")
    @Type(type = "uuid-char")
    private UUID createdBy;

    @LastModifiedDate
    @Column(nullable = true, columnDefinition = "timestamp")
    private LocalDateTime lastModifiedOn;

    @LastModifiedBy
    @Column(nullable = true, columnDefinition = "varchar(36)")
    @Type(type = "uuid-char")
    private UUID lastModifiedBy;
}
