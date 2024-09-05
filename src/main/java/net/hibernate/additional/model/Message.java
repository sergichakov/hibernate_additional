package net.hibernate.additional.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
@ToString
@Getter
@Setter
@Entity
@Embeddable
public class Message{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long message_id;
    /*@OneToOne
    //@Embedded
    private Users user;*/
    /*@Type(
            value = UsersType.class,
            parameters = @Parameter(name = UsersType.TYPE_NAME_PARAM_KEY, value = "userstype")

    )*/
    @Column(name="user_new_table")
    @Type(UsersType.class)
    private Users user;
}