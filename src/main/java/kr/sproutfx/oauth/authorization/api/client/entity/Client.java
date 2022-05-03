package kr.sproutfx.oauth.authorization.api.client.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.sproutfx.oauth.authorization.api.client.enumeration.ClientStatus;
import kr.sproutfx.oauth.authorization.api.project.entity.Project;
import kr.sproutfx.oauth.authorization.configuration.jpa.entity.JpaBaseEntity;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Builder @Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(of = "id", callSuper = false)
@Entity @Table(name = "clients")
@DynamicInsert @DynamicUpdate @Audited
@SQLDelete(sql = "UPDATE clients SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Client extends JpaBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "varchar(36)")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    private String code;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    private String name;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    private String secret;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    private String accessTokenSecret;

    @Column(nullable = false, columnDefinition = "integer")
    private Long accessTokenValidityInSeconds;
        
    @Column(nullable = false, columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private ClientStatus status;

    @Column(nullable = true, columnDefinition = "varchar(255)")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;
}
