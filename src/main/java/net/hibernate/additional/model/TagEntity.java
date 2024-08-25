package net.hibernate.additional.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name="tags")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tag_id;
    @Column (name="str")
    private String str;
    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(cascade={CascadeType.MERGE,CascadeType.PERSIST},fetch = FetchType.LAZY,mappedBy = "tag")//  CascadeType.PERSIST}

    /*@JoinTable(name="employee_task",
        joinColumns=  @JoinColumn(name="task_id", referencedColumnName="tag_id"),
        inverseJoinColumns= @JoinColumn(name="tag_id", referencedColumnName="task_id") )

     */
    //@Column(name="tasks_set",length=50)
    private Set<TaskEntity> task;//=new HashSet<>();
    public void add(TaskEntity taskEntity){
        //this.task.add(taskEntity);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof TagEntity)) return false;
        final TagEntity other = (TagEntity) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$tag_id = this.getTag_id();
        final Object other$tag_id = other.getTag_id();
        if (this$tag_id == null ? other$tag_id != null : !this$tag_id.equals(other$tag_id)) return false;
        final Object this$str = this.getStr();
        final Object other$str = other.getStr();
        if (this$str == null ? other$str != null : !this$str.equals(other$str)) return false;
        /*final Object this$task = this.getTask();
        final Object other$task = other.getTask();
        if (this$task == null ? other$task != null : !this$task.equals(other$task)) return false;

         */
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof TagEntity;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $tag_id = this.getTag_id();
        result = result * PRIME + ($tag_id == null ? 43 : $tag_id.hashCode());
        final Object $str = this.getStr();
        result = result * PRIME + ($str == null ? 43 : $str.hashCode());
        /*final Object $task = this.getTask();
        result = result * PRIME + ($task == null ? 43 : $task.hashCode());

         */
        return result;
    }
/*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagEntity tagEntity = (TagEntity) o;
        return tag_id.equals(tagEntity.tag_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag_id,str);
    }

 */
}
