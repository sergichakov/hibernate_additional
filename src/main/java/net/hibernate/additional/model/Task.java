package net.hibernate.additional.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@Entity
public class Task{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long task_id;
    @ElementCollection
    @CollectionTable
    private List<Message> comments2;
}