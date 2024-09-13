package net.hibernate.additional.service;

import net.hibernate.additional.dto.UserDTO;
import net.hibernate.additional.exception.AuthenticationException;
import net.hibernate.additional.mapper.UserEntityDtoMapper;
import net.hibernate.additional.model.UserEntity;
import net.hibernate.additional.repository.SessionRepoHelper;
import net.hibernate.additional.repository.SessionRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserRegistrationService {
    private volatile SessionRepository sessionRepoHelper=null;
    public UserRegistrationService(SessionRepository sessionRepoHelper){
        this.sessionRepoHelper=sessionRepoHelper;
    }
    public UserEntity registerUser(String userName, String password){
        UserEntity userEntity=null;
        try(Session session= sessionRepoHelper.getSession().openSession()){
            Transaction transaction=session.beginTransaction();
            userEntity=new UserEntity();
            userEntity.setUserName(userName);
            userEntity.setPassword(password);
            session.persist(userEntity);
            transaction.commit();
            session.refresh(userEntity);
        }
        System.out.println("userEntity id="+userEntity.getUser_id()+" username="+userName+" password="+password);
        return userEntity;//true;
    }
    public UserDTO getUserDTO(String userName, String password) throws AuthenticationException {
        UserEntity userEntity=getUserEntity(userName,password);
        UserEntityDtoMapper userEntityDtoMapper=UserEntityDtoMapper.INSTANCE;
        UserDTO userDTO=userEntityDtoMapper.toDTO(userEntity);
        return userDTO;
    }
    public UserEntity getUserEntity(String userName,String password) throws AuthenticationException {
        if (userName == null) throw new AuthenticationException("Given userName is null= " + userName);
        //session.setProperty("usergetName());
        UserEntity userEntity = null;

        try (Session session = sessionRepoHelper.getSession().openSession()) {//join fetch ue.user_id
            Query<UserEntity> userEntityQuery = session.createQuery("from UserEntity ue  where ue.userName = :userN", UserEntity.class);
            userEntityQuery.setParameter("userN", userName);
            List<UserEntity> listEntity = userEntityQuery.list();
            if (!listEntity.isEmpty()) {
                userEntity = userEntityQuery.list().get(0);
            }

            ////userEntity=userEntityQuery.getSingleResultOrNull();
            if (userEntity == null) {
                if (password==null|| password.isEmpty())return null;

                userEntity = registerUser(userName, password);
                return userEntity;
                //throw new AuthenticationException("No such userName= "+userName);
            }
            if (userEntity.getPassword() == null) {
                if (password == null || password.isEmpty()) {
                    throw new AuthenticationException("stored password and given is Null");

                } else {
                    Transaction transaction=session.beginTransaction();
                    userEntity.setPassword(password);
                    session.persist(userEntity);

                    //return null;
                    transaction.commit();
                    System.out.println("Entry password was empty. Password was updated to="+password);
                }
            } else {
                if (!userEntity.getPassword().equals(password)) {
                    throw new AuthenticationException("stored password Not equal to given");
                }

            }
            return userEntity;
        }
    }
    /*public UserDTO getUserDTO(){
        UserEntity userEntity=null;
        try(Session session = sessionRepoHelper.getSession().openSession()){

        }
    }*/
/*    public UserEntity getUserEntity222222222222222(String userName,String password) throws AuthenticationException {
        if (userName == null) throw new AuthenticationException("Given userName is null= " + userName);
        //session.setProperty("usergetName());
        UserEntity userEntity = null;
        try (Session session = sessionRepoHelper.getSession().openSession()) {
            Query<UserEntity> userEntityQuery = session.createQuery("from UserEntity where userName = :userN", UserEntity.class);
            userEntityQuery.setParameter("userN", userName);
            List<UserEntity> listEntity = userEntityQuery.list();
            if (!listEntity.isEmpty()) {
                userEntity = userEntityQuery.list().get(0);
            }

            ////userEntity=userEntityQuery.getSingleResultOrNull();
            if (userEntity == null) {
                //userEntity = registerUser(userName, password);
                return null;
                //throw new AuthenticationException("No such userName= "+userName);
            }
            if (userEntity.getPassword() == null) {
                if (password == null && password.isEmpty()) {
                    throw new AuthenticationException("stored password and given is Null");

                } else {
                    session.persist(userEntity);
                    //return null;
                }
            } else {
                if (!userEntity.getPassword().equals(password)) {
                    throw new AuthenticationException("stored password Not equal to given");
                }

            }
            return userEntity;
        }
    }

 */
}
