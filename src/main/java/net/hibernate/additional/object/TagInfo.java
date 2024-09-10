package net.hibernate.additional.object;

import net.hibernate.additional.model.TaskEntity;

import java.util.Set;

public class TagInfo
{
    private Long id;
    private String str;
    //@Column(name="tasks_set",length=50)
    private Set<TaskEntity> task;//=new HashSet<>();
}
