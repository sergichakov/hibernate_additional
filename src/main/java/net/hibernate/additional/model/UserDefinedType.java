package net.hibernate.additional.model;



import org.apache.commons.lang3.SerializationUtils;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.SqlTypes;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/*public class UserDefinedType
        extends AbstractSingleColumnStandardBasicType<Users> {
    public static final UserDefinedType INSTANCE=new UserDefinedType(LongVarcharJdbcType.INSTANCE, (JavaType) LongJavaType.INSTANCE);

    public UserDefinedType(JdbcType jdbcType, JavaType<Users> javaType) {
        super(jdbcType, javaType);
    }

    @Override
    public String getName() {
        return "UserDefinedType";
    }
}*/
public class UserDefinedType implements UserType<UserEntity> {
    public static final UserDefinedType INSTANCE=new UserDefinedType();
    private UserEntity usersValue=null;
    public UserDefinedType(){//UserEntityusers){
        //super(Users.class);
        //this.usersValue=users;
    }

    @Override
    public int getSqlType() {
        return SqlTypes.LONGVARCHAR;
    }

    @Override
    public Class returnedClass() {
        return UserEntity.class;
    }

    @Override
    public boolean equals(UserEntity x, UserEntity y) {
        return Objects.equals(x,y);
    }

    @Override
    public int hashCode(UserEntity x) {
        return 0;
    }

    /*@Override
    public boolean equals(Object x, Object y) {
        return Objects.equals(x,y);
    }

    @Override
    public int hashCode(Object x) {
        return Objects.hashCode(x);
    }*/
/*    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws SQLException {
        return new Object();
    }
*/
    @Override
    public UserEntity nullSafeGet(ResultSet resultSet, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        //resultSet.getInt(names[0]);
        Long result=resultSet.getLong(position);
        if (resultSet.wasNull()) {
            return null;
            //result = true;
        }
        UserEntity users=new UserEntity();
        users.setUser_id(result);
        return users;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, UserEntity value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (value == null) {
            st.setNull(index, SqlTypes.LONGVARCHAR);

        }else {
            st.setLong(index, value.getUser_id());
        }
    }

    @Override
    public UserEntity deepCopy(UserEntity value) {
        return value == null ? null :
                (UserEntity)  SerializationUtils.clone(this.usersValue);
        //Users.valueOf(Users.class.cast(value).toLongArray());;
    }

    /*@Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws SQLException {

    }*/

    /*@Override
    public Object deepCopy(Object value) {
        return null;
    }*/

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(UserEntity value) {
        return deepCopy(value);
    }

    /*@Override
    public Serializable disassemble(Object value) {
        return null;
    }*/

    @Override
    public UserEntity assemble(Serializable cached, Object owner) {
        return deepCopy((UserEntity) cached);
    }
}