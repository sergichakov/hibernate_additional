package net.hibernate.additional.service;

import net.hibernate.additional.model.TagEntity;
import net.hibernate.additional.model.TaskEntity;
import net.hibernate.additional.repository.SessionRepoHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.HashSet;
import java.util.Set;

public class TestService {
    public static void main(String[] args){
        SessionFactory sessionFactory= SessionRepoHelper.getSession();

        TagEntity tagEntity=TagEntity.builder().str("tag1").build();
        Set<TagEntity> tagEntitySet=new HashSet<TagEntity>();
        tagEntitySet.add(tagEntity);
        TaskEntity taskEntity=TaskEntity.builder()
                .name("first task")
                .task_id(1L)
                .tag(tagEntitySet)
                .build();
        TaskEntity taskEntityTwo=null;
        try(Session session=sessionFactory.openSession()){
            Transaction transaction=session.beginTransaction();//session.tr
                    session.saveOrUpdate(tagEntity);
            //taskEntityTwo=
                    session.saveOrUpdate(taskEntity);//update(taskEntity);"net.hibernate.additional.model.TaskEntity",
                    session.flush();
            transaction.commit();
        }
        System.out.println("TaskEntityTwo"+taskEntityTwo);
    }
}
