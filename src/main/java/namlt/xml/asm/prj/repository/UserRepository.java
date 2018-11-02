package namlt.xml.asm.prj.repository;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import namlt.xml.asm.prj.common.UserCommon;
import namlt.xml.asm.prj.model.User;

public class UserRepository extends Repository<String, User> implements UserCommon {

    private static final String INSERT_QUERY = "INSERT INTO "
            + "[User](Username,[Password],Name,[Address],Birthday,Phone,[Status],[Role])"
            + " VALUES(?,?,?,?,?,?,?,?)";
    private static final String QUERY_GET_BY_ID = "SELECT * FROM [User] WHERE Username=? AND [STATUS]=" + STATUS_ACTIVE;
    private static final String QUERY_GET_BY_USERNAME_AND_PASSWORD = "SELECT * FROM [User] "
            + "WHERE Username=? AND [Password]=? AND [STATUS]=" + STATUS_ACTIVE;

    @Override
    public User insert(User t) throws Exception {
        synchronized (TRANSACTION_KEY) {
            preparedStatement = newPreparedStatement(INSERT_QUERY);
            preparedStatement.setString(1, t.getUsername());
            preparedStatement.setString(2, t.getPassword());
            preparedStatement.setString(3, t.getName());
            preparedStatement.setString(4, t.getAddress());
            if (t.getBirthday() != null) {
                preparedStatement.setDate(5, new Date(t.getBirthday().getTime()));
            } else {
                preparedStatement.setNull(5, Types.DATE);
            }
            preparedStatement.setString(6, t.getPhone());
            preparedStatement.setInt(7, t.getStatus());
            preparedStatement.setInt(8, t.getRole());
            int status = preparedStatement.executeUpdate();
            if (status <= 0) {
                throw new SQLException("INSERT user fail with username='" + t.getUsername() + "'");
            }
        }
        return t;
    }

    @Override
    public void update(User t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User get(String key) throws Exception {
        User rs = null;
        synchronized (TRANSACTION_KEY) {
            preparedStatement = newPreparedStatement(QUERY_GET_BY_ID);
            preparedStatement.setString(1, key);
            resultSet = preparedStatement.executeQuery();
            List<User> resultList = extractDataFromResultSet();
            if (resultList.size() > 0) {
                rs = resultList.get(0);
            }
        }
        return rs;
    }

    public User getByUsernameAndPassword(String username, String password) throws SQLException, NamingException {
        User rs = null;
        synchronized (TRANSACTION_KEY) {
            preparedStatement = newPreparedStatement(QUERY_GET_BY_USERNAME_AND_PASSWORD);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            List<User> resultList = extractDataFromResultSet();
            if (resultList.size() > 0) {
                rs = resultList.get(0);
            }
        }
        return rs;
    }

    private List<User> extractDataFromResultSet() throws SQLException {
        List<User> rs = new ArrayList<>();
        User user;
        while (resultSet.next()) {
            user = new User();
            user.setUsername(resultSet.getString("Username"));
            user.setPassword(resultSet.getString("Password"));
            user.setName(resultSet.getNString("Name"));
            user.setAddress(resultSet.getNString("Address"));
            user.setPhone(resultSet.getString("Phone"));
            user.setBirthday(resultSet.getDate("Birthday"));
            user.setStatus(resultSet.getInt("Status"));
            user.setRole(resultSet.getInt("Role"));
            rs.add(user);
        }
        return rs;
    }

    @Override
    public List<User> find(String s, Integer startAt, Integer nextRow) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int count() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
