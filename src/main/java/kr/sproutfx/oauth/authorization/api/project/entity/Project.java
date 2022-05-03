package kr.sproutfx.oauth.authorization.api.project.entity;

import kr.sproutfx.oauth.authorization.api.client.entity.Client;
import kr.sproutfx.oauth.authorization.api.project.enumeration.ProjectStatus;
import kr.sproutfx.oauth.authorization.configuration.jpa.entity.JpaBaseEntity;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;


@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED) @EqualsAndHashCode(of = "id", callSuper = false)
@Entity @Table(name = "projects")
@DynamicInsert @DynamicUpdate @Audited
@SQLDelete(sql = "UPDATE projects SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Project extends JpaBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "varchar(36)")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    private String name;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @Column(nullable = true, columnDefinition = "varchar(255)")
    private String description;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Client> clients;
}
