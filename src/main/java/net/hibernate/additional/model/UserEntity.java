package net.hibernate.additional.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.BasicType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Setter
@Getter
@Entity
@Table(name="users")

public class  UserEntity implements Serializable{//UserType<String> {
//    public class UserEntity implements UserType{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE )
    private Long user_id;
    @Column(name="user_name")
    private String userName;
    @Column(name="password")
    @ToString.Exclude
    private String password;
    @OneToOne(fetch=FetchType.LAZY)
    private TaskEntity task;
/*
    @Override
    public int getSqlType() {
        return 0;
    }

    @Override
    public Class<T> returnedClass() {
        return (Class<T>) UserEntity.class;

    }

    @Override
    public boolean equals(String x, String y) {
        return false;
    }

    @Override
    public int hashCode(String x) {
        return 0;
    }

    @Override
    public boolean equals(T x, T y) {
        //return false;
        if (x == y) return true;
        if (x==null || y == null || getClass() != x.getClass()|| getClass() != y.getClass()) return false;
        UserEntity<?> current = (UserEntity<?>) x;
        UserEntity<?> that = (UserEntity<?>) y;
        return Objects.equals(current.user_id, that.user_id) && Objects.equals(current.userName, that.userName) && Objects.equals(current.password, that.password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity<?> that = (UserEntity<?>) o;
        return Objects.equals(user_id, that.user_id) && Objects.equals(userName, that.userName) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, userName, password);
    }

    @Override
    public int hashCode(T x) {
        return 0;
    }

    @Override
    public T nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        return null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, String value, int index, SharedSessionContractImplementor session) throws SQLException {

    }

    @Override
    public String deepCopy(String value) {
        return null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, T value, int index, SharedSessionContractImplementor session) throws SQLException {

    }

    @Override
    public T deepCopy(T value) {
        return null;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(String value) {
        return null;
    }

    @Override
    public Serializable disassemble(T value) {
        return null;
    }

    @Override
    public T assemble(Serializable cached, Object owner) {
        return null;
    }
*/

/*    @OneToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST},fetch=FetchType.LAZY)
    //@Column(name="comment")
    @JoinColumn(name = "id")

    private CommentEntity comment;
*/
}
