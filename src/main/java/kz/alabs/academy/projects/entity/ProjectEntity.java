package kz.alabs.academy.projects.entity;

import jakarta.persistence.*;
import kz.alabs.academy.core.entity.AuditEntity;
import kz.alabs.academy.users.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Set;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE projects SET is_deleted = true, deleted_at = now() WHERE id =?")
@Where(clause = "is_deleted = false")
public class ProjectEntity extends AuditEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private UserEntity author;

    @ElementCollection
    @CollectionTable(
            name = "project_links",
            joinColumns = @JoinColumn(name = "project_id")
    )
    private Set<String> url;

}
