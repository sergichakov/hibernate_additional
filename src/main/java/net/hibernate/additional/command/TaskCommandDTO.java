package net.hibernate.additional.command;

import lombok.*;
import net.hibernate.additional.model_kill_this.TaskStatus;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder=true)
public class TaskCommandDTO {
    private Long task_id;

    private String name;

    private Date createDate;

    private Date startDate;

    private Date endDate;

    private TaskStatus status;

    private UserCommandDTO user;
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
