package net.hibernate.additional.service;

import lombok.NoArgsConstructor;
import net.hibernate.additional.command.TaskCommandDTO;
import net.hibernate.additional.command.mapper.TaskCommandDtoEntityMapper;
import net.hibernate.additional.command.mapper.TaskCommandDtoEntityMapperImpl;
import net.hibernate.additional.dto.TaskDTO;
import net.hibernate.additional.mapper.TaskEntityDtoMapper;
import net.hibernate.additional.mapper.TaskEntityDtoMapperImpl;
import net.hibernate.additional.model.TaskEntity;
import net.hibernate.additional.repository.SessionRepoHelper;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class TaskService {

    //public List<TaskDTO> listAllTasks(Integer page,Integer count){return null;}
    //public static void main(String[] args){Integer page=null;Integer count=null;
    public List<TaskDTO> listAllTasks(String userName,Integer page,Integer count){
        TaskEntityDtoMapper taskMapper=new TaskEntityDtoMapperImpl() ;
        List<TaskEntity> taskEntities=null;
        List<TaskDTO> dtoList=new ArrayList<>();

        if(count==null)count=5;
        if (count>50)count=50;
        if(page==null)page=0;


        try (Session session = SessionRepoHelper.getSession().openSession()) {
            Query<TaskEntity> tasks;
            if(userName==null||userName.equals("ADMIN") ||userName.isEmpty() || userName.equals("Unknown")){
                //request="from TaskEntity";
                tasks=session.createQuery("from TaskEntity ",TaskEntity.class);
            }else {
                tasks=session.createQuery("from TaskEntity where name= :userName",TaskEntity.class);
                tasks.setParameter("userName",userName);
            }
            tasks.setFirstResult(page);
            tasks.setMaxResults(count);
            taskEntities=tasks.list();
            for(TaskEntity taskEntity:taskEntities){
                //TaskEntity unProxy= (TaskEntity) Hibernate.unproxy(taskEntity);
                TaskDTO task=taskMapper.toDTO(taskEntity);
                dtoList.add(task);
            }
        }

        return dtoList;
    }
    public boolean editTask(TaskCommandDTO commandDTO){
        TaskCommandDtoEntityMapper commandToEntityMapper=new TaskCommandDtoEntityMapperImpl() ;
        TaskEntity taskEntity=commandToEntityMapper.toModel(commandDTO);
        TaskEntity taskEntityResponse;
        try(Session session = SessionRepoHelper.getSession().openSession()) {
            Transaction transaction=session.beginTransaction();
            taskEntityResponse=(TaskEntity) session.merge(taskEntity);
            transaction.commit();
        }
        //return taskEntityResponse;
        //TaskEntity taskEntityResponse=editTaskProcessing(commandDTO);
        if (taskEntityResponse==null)return false;
        return true;
    }
    public TaskDTO createTask(TaskCommandDTO commandDTO){
        TaskCommandDtoEntityMapper commandToEntityMapper=new TaskCommandDtoEntityMapperImpl() ;
        TaskEntity taskEntity=commandToEntityMapper.toModel(commandDTO);
        TaskEntity taskEntityResponse;
        TaskDTO taskDTO=null;
        try(Session session = SessionRepoHelper.getSession().openSession()) {
            Transaction transaction=session.beginTransaction();
            taskEntityResponse=(TaskEntity) session.merge(taskEntity);
        //    transaction.commit();
        //}

        //TaskEntity taskEntityResponse=editTaskProcessing(commandDTO);
        System.out.println("taskEntity_createTask"+taskEntityResponse);
        if (taskEntityResponse==null)return null;
        //TaskDTO taskDTO=null;
        //try (Session session = SessionRepoHelper.getSession().openSession()) {
            //Transaction transaction=session.beginTransaction();
            session.persist(taskEntityResponse);//saveOrUpdate
            session.flush();
            session.refresh(taskEntityResponse);
            TaskEntityDtoMapperImpl commandEntityMapper=new TaskEntityDtoMapperImpl();
            taskDTO=commandEntityMapper.toDTO(taskEntityResponse);
            transaction.commit();
        }

        return taskDTO;
    }
    public boolean deleteTask(TaskCommandDTO commandDTO){
        TaskCommandDtoEntityMapper commandToEntityMapper=new TaskCommandDtoEntityMapperImpl() ;
        TaskEntity taskEntity=commandToEntityMapper.toModel(commandDTO);
        try(Session session = SessionRepoHelper.getSession().openSession()) {
            Transaction transaction=session.beginTransaction();
            session.remove(taskEntity);
            transaction.commit();
        }
        return true;
    }
}
