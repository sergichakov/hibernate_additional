package net.hibernate.additional.service;

import jakarta.servlet.http.HttpSession;
import net.hibernate.additional.command.CommentCommandDTO;
//import net.hibernate.additional.command.CommandDTO;
import net.hibernate.additional.command.UserCommandDTO;
import net.hibernate.additional.command.mapper.CommentCommandDtoEntityMapper;
import net.hibernate.additional.command.mapper.TaskCommandDtoEntityMapper;
import net.hibernate.additional.dto.CommentDTO;
import net.hibernate.additional.dto.TaskDTO;
import net.hibernate.additional.mapper.CommentEntityDtoMapper;
import net.hibernate.additional.model.CommentEntity;
import net.hibernate.additional.model.SessionObject;
import net.hibernate.additional.model.TaskEntity;
import net.hibernate.additional.model_kill_this.TaskStatus;
import net.hibernate.additional.repository.SessionRepoHelper;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Order;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CommentService {
    private Logger logger= null;
    public CommentService(){
        logger= LoggerFactory.getLogger(TaskService.class);
    }
    public List<CommentDTO>listAllComments(Long taskId,String userName){
        List<CommentEntity> commentList=null;
        try (Session session = SessionRepoHelper.getSession().openSession()) {
            Query<CommentEntity> comments;
            if (userName == null || userName.equals("ADMIN") || userName.isEmpty() || userName.equals("Unknown")) {
                //request="from TaskEntity";
                comments = session.createQuery("from CommentEntity ", CommentEntity.class);
            } else {
                comments = session.createQuery("from CommentEntity where name= :userName", CommentEntity.class);
                comments.setParameter("userName", userName);
            }
            comments.setOrder(Order.asc(CommentEntity.class, "user"));//user_id column
            commentList=comments.list();
        }
        List<CommentDTO>commentDtoList=new ArrayList<>();
        for(CommentEntity commentEntity:commentList){
            //TaskEntity unProxy= (TaskEntity) Hibernate.unproxy(taskEntity);
            CommentDTO commentDto= CommentEntityDtoMapper.INSTANCE.toDTO(commentEntity);
            commentDtoList.add(commentDto);
        }
        return commentDtoList;
    }
    public boolean editComment(CommentCommandDTO commentCommandDto,UserCommandDTO userCommandDTO){
        CommentCommandDtoEntityMapper commentCommandDtoEntityMapper=CommentCommandDtoEntityMapper.INSTANCE;
        CommentEntity commentEntity=commentCommandDtoEntityMapper.toModel(commentCommandDto);
        //HttpSession currentSession = request.getSession();
        //SessionObject sessionObject=(SessionObject) currentSession.getAttribute("session");
        try(Session session = SessionRepoHelper.getSession().openSession()) {
            Transaction transaction=session.beginTransaction();
            session.merge(commentEntity);
            transaction.commit();
        }catch(ConstraintViolationException e){
            logger.info("attempt to insert new duplicate key into the table");
            return false;
        }
        return true;
    }
    public void createComment(CommentCommandDTO commentCommandDTO, UserCommandDTO userCommandDTO){
        CommentCommandDtoEntityMapper commentCommandToEntityMapper=CommentCommandDtoEntityMapper.INSTANCE;//new TaskCommandDtoEntityMapperImpl() ;
        CommentEntity commentEntity=commentCommandToEntityMapper.toModel(commentCommandDTO);
        try(Session session = SessionRepoHelper.getSession().openSession()){
            Transaction transaction=session.beginTransaction();
            session.persist(commentEntity);
            transaction.commit();
        }
    }
    public boolean deleteComment(CommentCommandDTO commandDTO,UserCommandDTO userCommandDTO){
        CommentCommandDtoEntityMapper commandToEntityMapper=CommentCommandDtoEntityMapper.INSTANCE;//new TaskCommandDtoEntityMapperImpl() ;
        CommentEntity commentEntity=commandToEntityMapper.toModel(commandDTO);
        try(Session session = SessionRepoHelper.getSession().openSession()) {
            Transaction transaction=session.beginTransaction();
            session.remove(commentEntity);
            transaction.commit();
        }
        return true;
    }

}
