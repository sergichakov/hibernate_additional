package net.hibernate.additional.dto;

import lombok.Data;

import java.util.Set;
@Data
public class TagDTO {
    private Long tag_id;
    private String str;
    //@Column(name="tasks_set",length=50)
    private Set<TaskDTO> task;//=new HashSet<>();
}
