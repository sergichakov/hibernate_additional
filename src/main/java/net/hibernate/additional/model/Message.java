package net.hibernate.additional.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Getter
@Setter
@Entity
@Embeddable
public class Message{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long message_id;
    @OneToOne
    //@Embedded
    private Users user;
    /*@Type(
            value = UsersType.class,
            parameters = @Parameter(name = UsersType.TYPE_NAME_PARAM_KEY, value = "userstype")

    )*/
}