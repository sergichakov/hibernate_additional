package net.hibernate.additional.model;

import jakarta.persistence.*;
import lombok.*;
import net.hibernate.additional.dto.UserDTO;
import net.hibernate.additional.model.TaskEntity;
import org.hibernate.annotations.Type;
import org.hibernate.type.BasicType;
import org.hibernate.usertype.UserType;

import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name="comments")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@ManyToOne
    @ToString.Exclude
    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.PERSIST},fetch = FetchType.LAZY)
    @JoinColumn(name="task_id")//1"task_id") nullable=false)
    private TaskEntity task;
    @Column (name="comment_of_task", length=512)
    private String comment;
    //@OneToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST},fetch=FetchType.LAZY)
    //@Column(name="user")
    //@JoinColumn(name = "user_id")
    //@ElementCollection
    //@Type( (Class<Ent<String>>) ((Class)Ent.class))//(Class<String>)(UserEntity.class)//Class<String>)((Class)UserEntity.class))//type = "net.hibernate.additional.model.UserEntity")
    @Type(UserDefinedType.class)
    @Column(name="user_type")
    private UserEntity user;

    //@ElementCollection
    //private List<UserDTO> striKill;
    public void setTask(TaskEntity taskEntity){
        this.task=(taskEntity);
    }
}
//@Entity
/*class Ent<String> implements UserType<String> {
    @Id
    private Long id;
}*/