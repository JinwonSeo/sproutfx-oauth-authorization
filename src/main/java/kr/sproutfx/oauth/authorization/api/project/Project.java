package kr.sproutfx.oauth.authorization.api.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import kr.sproutfx.oauth.authorization.api.client.entity.Client;
import kr.sproutfx.oauth.authorization.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED) @EqualsAndHashCode(of = "id", callSuper = false)
@Entity @Table(name = "projects")
@DynamicInsert @DynamicUpdate @Audited
@SQLDelete(sql = "UPDATE project SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Project extends BaseEntity implements Serializable {
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
