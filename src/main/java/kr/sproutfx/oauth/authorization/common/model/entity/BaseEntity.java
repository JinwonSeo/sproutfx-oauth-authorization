package kr.sproutfx.oauth.authorization.common.model.entity;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean deleted = Boolean.FALSE;

    @CreatedDate
    @Column(nullable = false, updatable = false, columnDefinition = "timestamp")
    private Timestamp createdOn;

    @CreatedBy
    @Column(nullable = true, updatable = false, columnDefinition = "varchar(36)")
    @Type(type = "uuid-char")
    private UUID createdBy;

    @LastModifiedDate
    @Column(nullable = true, columnDefinition = "timestamp")
    private Timestamp lastModifiedOn;

    @LastModifiedBy
    @Column(nullable = true, columnDefinition = "varchar(36)")
    @Type(type = "uuid-char")
    private UUID lastModifiedBy;
}
