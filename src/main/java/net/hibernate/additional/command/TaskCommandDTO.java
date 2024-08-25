package net.hibernate.additional.command;

import lombok.Data;
import lombok.Getter;
import net.hibernate.additional.model_kill_this.TaskStatus;

import java.time.ZonedDateTime;
import java.util.Set;
@Data
@Getter
public class TaskCommandDTO {
    private Long task_id;

    private String name;

    private ZonedDateTime createDate;

    private ZonedDateTime startDate;

    private ZonedDateTime endDate;

    private TaskStatus status;


    private String title;
    private Set<TagCommandDTO> tag;//TagEntity

    ////////////////////////////////private Set<CommentCommandDTO> comments;//CommentEntity
    /*
    @SuppressWarnings("unchecked")
    @JsonProperty("tag")
    public void convertTagToTask(Set<String> tags){
        this.tag=tags;
    }

     */
}
