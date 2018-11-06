package namlt.xml.asm.prj.repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import namlt.xml.asm.prj.model.OrderDetail;
import namlt.xml.asm.prj.model.id.OrderDetailId;

public class OrderDetailRepository extends Repository<OrderDetailId, OrderDetail> {

    public static final String INSERT_QUERY = "INSERT INTO "
            + "OrderDetail(OrderId, BookId, Author, Title, ImageUrl, Quantity, ItemPrice) "
            + "VALUES(?,?,?,?,?,?,?)";
    public static final String QUERY_GET_BY_ORDER_ID = "SELECT * FROM OrderDetail WHERE OrderId=?";

    @Override
    public OrderDetail insert(OrderDetail t) throws Exception {
        synchronized (TRANSACTION_KEY) {
            try {
                preparedStatement = newPreparedStatement(INSERT_QUERY);
                preparedStatement.setInt(1, t.getOrderId());
                preparedStatement.setString(2, t.getBookId());
                preparedStatement.setString(3, t.getAuthor());
                preparedStatement.setString(4, t.getTitle());
                preparedStatement.setString(5, t.getImageUrl());
                preparedStatement.setInt(6, t.getQuantity());
                preparedStatement.setDouble(7, t.getItemPrice());
                int rs = preparedStatement.executeUpdate();
                if (rs < 0) {
                    throw new Exception("Fail to insert Order detail for Order has id=" + t.getOrderId());
                }
            } finally {
                closeResources();
            }
        }
        return t;
    }

    public List<OrderDetail> getByOrderId(int orderId) throws SQLException, NamingException {
        List<OrderDetail> rs = null;
        synchronized (TRANSACTION_KEY) {
            try {
                preparedStatement = newPreparedStatement(QUERY_GET_BY_ORDER_ID);
                preparedStatement.setInt(1, orderId);
                resultSet = preparedStatement.executeQuery();
                rs = extractResultFromResultList();
            } finally {
                closeResources();
            }
        }
        return rs;
    }

    private List<OrderDetail> extractResultFromResultList() throws SQLException {
        List<OrderDetail> rs = new ArrayList<>();
        OrderDetail orderDetail;
        Integer tmpInt;
        while (resultSet.next()) {
            orderDetail = new OrderDetail();
            orderDetail.setOrderId(resultSet.getInt("OrderId"));
            orderDetail.setBookId(resultSet.getString("BookId"));
            orderDetail.setAuthor(resultSet.getString("Author"));
            orderDetail.setTitle(resultSet.getString("Title"));
            orderDetail.setImageUrl(resultSet.getString("ImageUrl"));
            orderDetail.setQuantity(resultSet.getInt("Quantity"));
            orderDetail.setItemPrice(resultSet.getDouble("ItemPrice"));
            rs.add(orderDetail);
        }
        return rs;
    }

    @Override
    public void update(OrderDetail t) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OrderDetail get(OrderDetailId key) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<OrderDetail> find(String s, Integer startAt, Integer nextRow) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int count() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
