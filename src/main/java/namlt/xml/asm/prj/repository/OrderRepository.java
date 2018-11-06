package namlt.xml.asm.prj.repository;

import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import namlt.xml.asm.prj.model.Order;

public class OrderRepository extends Repository<Integer, Order> {

    public static final String INSERT_QUERY = "INSERT INTO "
            + "[Order](CustomerId, InsertDate, Status) "
            + "VALUES(?,?,?)";

    @Override
    public Order insert(Order t) throws Exception {
        synchronized (TRANSACTION_KEY) {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, t.getCustomerId());
            preparedStatement.setTimestamp(2, new Timestamp(t.getInsertDate().getTime()));
            preparedStatement.setInt(3, t.getStatus());
            int rs = preparedStatement.executeUpdate();
            if (rs > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                resultSet.next();
                int newId = resultSet.getInt(1);
                t.setId(newId);
            } else {
                throw new Exception("Fail to insert Order");
            }
        }
        return t;
    }

    @Override
    public void update(Order t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Order get(Integer key) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Order> find(String s, Integer startAt, Integer nextRow) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int count() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
