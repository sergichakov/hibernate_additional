package net.hibernate.additional.service;

import lombok.NoArgsConstructor;
import net.hibernate.additional.command.TaskCommandDTO;
import net.hibernate.additional.command.mapper.TaskCommandDtoEntityMapper;
import net.hibernate.additional.command.mapper.TaskCommandDtoEntityMapperImpl;
import net.hibernate.additional.dto.TaskDTO;
import net.hibernate.additional.mapper.TaskEntityDtoMapper;
import net.hibernate.additional.mapper.TaskEntityDtoMapperImpl;
import net.hibernate.additional.model.TagEntity;
import net.hibernate.additional.model.TaskEntity;
import net.hibernate.additional.repository.SessionRepoHelper;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Order;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@NoArgsConstructor
public class TaskService {
    private Logger logger= null;
    public TaskService(){
        logger=LoggerFactory.getLogger(TaskService.class);
    }

    //public List<TaskDTO> listAllTasks(Integer page,Integer count){return null;}
    //public static void main(String[] args){Integer page=null;Integer count=null;
    public List<TaskDTO> listAllTasks(String userName,Integer pageNumber,Integer pageSize){
        TaskEntityDtoMapper taskMapper=TaskEntityDtoMapper.INSTANCE;//new TaskEntityDtoMapperImpl() ;
        List<TaskEntity> taskEntities=null;

/*        Integer fullCount=getAllCount();
        if(pageNumber*pageSize<=fullCount){
            pageNumber=pageNumber-1;
        }*/
        List<TaskDTO> dtoList=new ArrayList<>();
        System.out.println("pageNumber="+pageNumber+" pageSize="+pageSize);
        if(pageSize==null)pageSize=3;
        if (pageSize>50)pageSize=50;
        if(pageNumber==null)pageNumber=0;


        try (Session session = SessionRepoHelper.getSession().openSession()) {
            Query<TaskEntity> tasks;
            if(userName==null||userName.equals("ADMIN") ||userName.isEmpty() || userName.equals("Unknown")){
                //request="from TaskEntity";
                tasks=session.createQuery("from TaskEntity ",TaskEntity.class);
            }else {
                tasks=session.createQuery("from TaskEntity where name= :userName",TaskEntity.class);
                tasks.setParameter("userName",userName);
            }
            tasks.setOrder(Order.asc(TaskEntity.class,"task_id"));
            tasks.setFirstResult(pageNumber*pageSize);
            tasks.setMaxResults(pageSize);
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
        TaskCommandDtoEntityMapper commandToEntityMapper=TaskCommandDtoEntityMapper.INSTANCE;//new TaskCommandDtoEntityMapperImpl() ;
        TaskEntity taskEntity=commandToEntityMapper.toModel(commandDTO);
        TaskEntity taskEntityResponse=null;
        try(Session session = SessionRepoHelper.getSession().openSession()) {
            Transaction transaction=session.beginTransaction();
            session.merge(taskEntity);
            //taskEntityResponse=(TaskEntity) session.merge(taskEntity);
            transaction.commit();
        }catch(ConstraintViolationException e){
            logger.info("attempt to insert new duplicate key into the table");
        }
        //return taskEntityResponse;
        //TaskEntity taskEntityResponse=editTaskProcessing(commandDTO);
        if (taskEntityResponse==null)return false;
        return true;
    }
    public TaskDTO createTask(TaskCommandDTO commandDTO){
        TaskCommandDtoEntityMapper commandToEntityMapper=TaskCommandDtoEntityMapper.INSTANCE;//new TaskCommandDtoEntityMapperImpl() ;
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
            TaskEntityDtoMapper commandEntityMapper=TaskEntityDtoMapper.INSTANCE;//lnew TaskEntityDtoMapperImpl();
            taskDTO=commandEntityMapper.toDTO(taskEntityResponse);
            transaction.commit();
        }

        return taskDTO;
    }
    public boolean deleteTask(TaskCommandDTO commandDTO){
        TaskCommandDtoEntityMapper commandToEntityMapper=TaskCommandDtoEntityMapper.INSTANCE;//new TaskCommandDtoEntityMapperImpl() ;
        TaskEntity taskEntity=commandToEntityMapper.toModel(commandDTO);
        try(Session session = SessionRepoHelper.getSession().openSession()) {
            Transaction transaction=session.beginTransaction();
            session.remove(taskEntity);
            transaction.commit();
        }
        return true;
    }

    public int getAllCount() {
        try(Session session=SessionRepoHelper.getSession().openSession()){
            //Query<Long> query=session.createNamedQuery("namedQuery",Long.class);
            //query.setParameter(1,pageNumber);
            //query.setParameter(2,pageSize);

            Query<Long> query1=session.createQuery("select count(*) from TaskEntity",Long.class);
            Integer i= query1.list().get(0).intValue();
            System.out.println("query namedQuery executed "+i);
            return i;//query.list().get(0).intValue();
        }
        //return 0;
    }
    public Long getIdOfTag(String tagStr){
        logger.info("getIdOfTag processing of string tagStr="+tagStr);
        TagEntity singleTagId=null;
        Long l=null;
        try(Session session=SessionRepoHelper.getSession().openSession()){
            Query<TagEntity> queryTag=session.createQuery("from TagEntity t where t.str=:tagStr",TagEntity.class);
            queryTag.setParameter("tagStr",tagStr);
            //singleTagId=queryTag.getSingleResult();
            NativeQuery<Long> lon=session.createNativeQuery("select tag_id from tags where str=:tagStr",Long.class);
            lon.setParameter("tagStr",tagStr);
            l=lon.getSingleResultOrNull();
            logger.info("something wrong in NativeQuery="+tagStr+ " long="+l );
            //Optional<Long> optLong=queryTag.uniqueResultOptional();
            //singleTagId=queryTag.uniqueResult();
        }
        if (singleTagId!=null)logger.info("for tag="+tagStr+" found number="+singleTagId);
        else System.out.println("singleTagId="+singleTagId);
        //return singleTagId.getTag_id();
        return l;
    }
}
