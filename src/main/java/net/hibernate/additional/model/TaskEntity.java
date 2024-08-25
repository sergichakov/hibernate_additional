package net.hibernate.additional.model;

import jakarta.persistence.*;
import lombok.*;
import net.hibernate.additional.model_kill_this.TaskStatus;

import java.time.ZonedDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name="tasks")
public class TaskEntity {
    @Id
    @Column(name="task_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY )

    private Long task_id;
    //@Column(name="name")
    private String name;
    @Column(name="create_date")
    private ZonedDateTime createDate;
    @Column(name="start_date")
    private ZonedDateTime startDate;
    @Column(name="end_date")
    private ZonedDateTime endDate;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    @Column(name="task_title")
    private String title;
    //@JsonIgnore
    @ToString.Exclude
    @ManyToMany(cascade={CascadeType.MERGE,CascadeType.PERSIST},fetch = FetchType.LAZY)//,mappedBy = "task")//s_set")   CascadeType.PERSIST}
    //@Column(name="tags_field")

    @JoinTable(name="employee_task",
            joinColumns=  @JoinColumn(name="tag_id", referencedColumnName="task_id"),
            inverseJoinColumns= @JoinColumn(name="task_id", referencedColumnName="tag_id") )


    private Set<TagEntity> tag;//=new HashSet<>();
    //@ElementCollection
    //@OneToOne
    //private TagEntity tan;

    /*@OneToMany(cascade={CascadeType.MERGE,CascadeType.PERSIST},mappedBy = "task",fetch = FetchType.LAZY)//_of_task")
    @Column(name="task_comments")//,unique = true)
    //@JoinColumn(name="id")

    private Set<CommentEntity> comments;*/
    public void addTag(TagEntity tagEntity){
        this.tag.add(tagEntity);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof TaskEntity)) return false;
        final TaskEntity other = (TaskEntity) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$task_id = this.getTask_id();
        final Object other$task_id = other.getTask_id();
        if (this$task_id == null ? other$task_id != null : !this$task_id.equals(other$task_id)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$createDate = this.getCreateDate();
        final Object other$createDate = other.getCreateDate();
        if (this$createDate == null ? other$createDate != null : !this$createDate.equals(other$createDate))
            return false;
        final Object this$startDate = this.getStartDate();
        final Object other$startDate = other.getStartDate();
        if (this$startDate == null ? other$startDate != null : !this$startDate.equals(other$startDate)) return false;
        final Object this$endDate = this.getEndDate();
        final Object other$endDate = other.getEndDate();
        if (this$endDate == null ? other$endDate != null : !this$endDate.equals(other$endDate)) return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        final Object this$title = this.getTitle();
        final Object other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
        /*final Object this$tag = this.getTag();
        final Object other$tag = other.getTag();
        if (this$tag == null ? other$tag != null : !this$tag.equals(other$tag)) return false;
        */
        return true;

    }

    protected boolean canEqual(final Object other) {
        return other instanceof TaskEntity;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $task_id = this.getTask_id();
        result = result * PRIME + ($task_id == null ? 43 : $task_id.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $createDate = this.getCreateDate();
        result = result * PRIME + ($createDate == null ? 43 : $createDate.hashCode());
        final Object $startDate = this.getStartDate();
        result = result * PRIME + ($startDate == null ? 43 : $startDate.hashCode());
        final Object $endDate = this.getEndDate();
        result = result * PRIME + ($endDate == null ? 43 : $endDate.hashCode());
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        final Object $title = this.getTitle();
        result = result * PRIME + ($title == null ? 43 : $title.hashCode());
        /*
        final Object $tag = this.getTag();
        result = result * PRIME + ($tag == null ? 43 : $tag.hashCode());
        */
        return result;

    }
    ////////////////////////////////public void addComment(CommentEntity commentEntity){ this.comments.add(commentEntity); }
/*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskEntity that = (TaskEntity) o;
        return task_id.equals(that.task_id) && Objects.equals(name, that.name) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(task_id, name, title);
    }

 */

}
