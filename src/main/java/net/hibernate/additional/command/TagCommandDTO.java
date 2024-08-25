package net.hibernate.additional.command;

import lombok.Data;
import lombok.Getter;
import net.hibernate.additional.model.TaskEntity;

import java.util.Set;
@Getter
@Data
public class TagCommandDTO {
    private Long tag_id;
    private String str;
    //@Column(name="tasks_set",length=50)
    private Set<TaskEntity> task;//=new HashSet<>();
}
