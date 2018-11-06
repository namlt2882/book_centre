package namlt.xml.asm.prj.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public abstract class Repository<K, T> {

    protected Connection connection;
    protected ResultSet resultSet;
    protected PreparedStatement preparedStatement;
    protected final Object TRANSACTION_KEY = new Object();
    private static DataSource dataSource;

    static {
        try {
            dataSource = initNewDataSource();
        } catch (Exception ex) {
            Logger.getLogger(Repository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Repository() {
    }

    private static DataSource initNewDataSource() throws NamingException, SQLException {
        Context context = (Context) new InitialContext();
        Context tomcatContext = (Context) context.lookup("java:comp/env");
        DataSource dataSource = (DataSource) tomcatContext.lookup("BookStoreDatabase");
        return dataSource;
    }

    protected Connection getConnection() throws NamingException, SQLException {
        if (connection == null) {
            connection = dataSource.getConnection();
        }
        return connection;
    }

    protected PreparedStatement newPreparedStatement(String sql) throws NamingException, SQLException {
        connection = getConnection();
        return connection.prepareStatement(sql);
    }

    protected void closeResources() {
        try {
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }
            if (preparedStatement != null) {
                preparedStatement.close();
                preparedStatement = null;
            }
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract T insert(T t) throws Exception;

    public abstract void update(T t) throws Exception;

    public abstract T get(K key) throws Exception;

    public abstract List<T> find(String s, Integer startAt, Integer nextRow) throws Exception;

    public abstract int count() throws Exception;
}
