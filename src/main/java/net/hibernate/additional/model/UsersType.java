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

/*public class UsersType
        extends AbstractSingleColumnStandardBasicType<Users> {
    public static final UsersType INSTANCE=new UsersType(LongVarcharJdbcType.INSTANCE, (JavaType) LongJavaType.INSTANCE);

    public UsersType(JdbcType jdbcType, JavaType<Users> javaType) {
        super(jdbcType, javaType);
    }

    @Override
    public String getName() {
        return "UsersType";
    }
}*/
public class UsersType implements UserType<Users> {
    public static final UsersType INSTANCE=new UsersType();
    private Users usersValue=null;
    public UsersType(){//Users users){
        //super(Users.class);
        //this.usersValue=users;
    }

    @Override
    public int getSqlType() {
        return SqlTypes.LONGVARCHAR;
    }

    @Override
    public Class returnedClass() {
        return Users.class;
    }

    @Override
    public boolean equals(Users x, Users y) {
        return Objects.equals(x,y);
    }

    @Override
    public int hashCode(Users x) {
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

    @Override
    public Users nullSafeGet(ResultSet resultSet, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        //resultSet.getInt(names[0]);
        Long result=resultSet.getLong(position);
        if (resultSet.wasNull()) {
            return null;
            //result = true;
        }
        Users users=new Users();
        users.setUser_id(result);
        return users;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Users value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (value == null) {
            st.setNull(index, SqlTypes.LONGVARCHAR);

        }else {
            st.setLong(index, value.getUser_id());
        }
    }

    @Override
    public Users deepCopy(Users value) {
        return value == null ? null :
                (Users) SerializationUtils.clone(this.usersValue);
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
    public Serializable disassemble(Users value) {
        return deepCopy(value);
    }

    /*@Override
    public Serializable disassemble(Object value) {
        return null;
    }*/

    @Override
    public Users assemble(Serializable cached, Object owner) {
        return deepCopy((Users)cached);
    }
}