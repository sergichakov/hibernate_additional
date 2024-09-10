package net.hibernate.additional.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.hibernate.additional.model.UserEntity;
import net.hibernate.additional.model_kill_this.TaskStatus;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
@Setter
@Data
@Getter
public class TaskDTO {
    private Long task_id;

    private String name;

    private Date createDate;

    private Date startDate;

    private Date endDate;

    private TaskStatus status;

    private UserDTO user;
    private String title;
    private Set<TagDTO> tag;//TagEntity

   // private List<CommentDTO> comment;
   ///////////////////////////////////// private Set<CommentDTO> comments;//CommentEntity

}
