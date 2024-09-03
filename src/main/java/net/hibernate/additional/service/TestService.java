package net.hibernate.additional.service;

import net.hibernate.additional.command.CommentCommandDTO;
import net.hibernate.additional.command.UserCommandDTO;
import net.hibernate.additional.command.mapper.CommentCommandDtoEntityMapper;
import net.hibernate.additional.command.mapper.UserCommandDtoEntityMapper;
import net.hibernate.additional.dto.CommentDTO;
import net.hibernate.additional.dto.UserDTO;
import net.hibernate.additional.mapper.CommentEntityDtoMapper;
import net.hibernate.additional.mapper.UserEntityDtoMapper;
import net.hibernate.additional.model.*;
import net.hibernate.additional.repository.SessionRepoHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestService {
    public static void main(String[] args){
        SessionFactory sessionFactory= SessionRepoHelper.getSession();


        /*=TaskEntity.builder()
                .name("first task")
                .task_id(1L)
                .tag(tagEntitySet)
                .comments(commentList)
                .build();*/
        UserEntity user=null;
        CommentEntity comment=null;
        TaskEntity taskEntityTwo=null;
        try(Session session=sessionFactory.openSession()){
            Transaction transaction=session.beginTransaction();//session.tr
            TaskEntity taskEntity=null;
            TagEntity tagEntity=TagEntity.builder().str("tag1").build();
            Set<TagEntity> tagEntitySet=new HashSet<TagEntity>();
            tagEntitySet.add(tagEntity);
            user=new UserEntity();
            user.setUserName("Anton");
             comment=CommentEntity.builder().comment("Comment1").user(user).build();
            List<CommentEntity> commentList=null;//new ArrayList<>();
            commentList=new ArrayList<>();


            commentList.add(comment);
            taskEntity=TaskEntity.builder()
                    .name("first task")
                    .task_id(1L)
                    .tag(tagEntitySet)
                    .comments(commentList)
                    .build();
            comment.setTask(taskEntity);
            comment.setUser(user);
            //TaskEntity task=
            //session.persist(tagEntity);
            //session.persist(comment);
                    //Long identy= (Long)
            //session.evict(comment);

                    //session.save(taskEntity);//saveOrUpdate(tagEntity);
            taskEntityTwo=session.merge(taskEntity);
            //taskEntityTwo=
            //session.persist(tagEntity);
            //session.persist(comment);
                    //session.saveOrUpdate(taskEntity);//update(taskEntity);"net.hibernate.additional.model.TaskEntity",
            //session.flush();
            transaction.commit();
        }
        System.out.println("TaskEntityTwo"+taskEntityTwo);
        CommentCommandDtoEntityMapper ccdem=CommentCommandDtoEntityMapper.INSTANCE;
        CommentCommandDTO commentCommDto=ccdem.toDTO(comment);//нужно только в toModel
        System.out.println("commentCommandDTO="+commentCommDto);
        CommentEntityDtoMapper commEntDto=CommentEntityDtoMapper.INSTANCE;
        CommentDTO commentDto=commEntDto.toDTO(comment);
        System.out.println("CommentDto="+commentDto);
        UserCommandDtoEntityMapper userComMapper=UserCommandDtoEntityMapper.INSTANCE;
        UserCommandDTO userCommDto=userComMapper.toDTO(user);//нужно только в toModel
        System.out.println("userCommDto="+userCommDto);
        UserEntityDtoMapper userDtoMapper=UserEntityDtoMapper.INSTANCE;
        UserDTO userDto=userDtoMapper.toDTO(user);
        System.out.println("userDto="+userDto);
        try(Session session=sessionFactory.openSession()){
            Transaction transaction=session.beginTransaction();
            Message message=new Message();
            List<Message> listMess=new ArrayList<>();
            listMess.add(message);
            Users users=new Users();
            //users.setUser_id(50L);
            users.setMessage(message);
            message.setUser(users);
            Task task=new Task();
            task.setComments2(listMess);

            session.persist(users);
            session.persist(message);
            session.persist(task);
            transaction.commit();
        }
    }
}
