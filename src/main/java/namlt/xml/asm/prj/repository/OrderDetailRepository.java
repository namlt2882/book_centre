package namlt.xml.asm.prj.repository;

import java.util.List;
import namlt.xml.asm.prj.model.OrderDetail;
import namlt.xml.asm.prj.model.id.OrderDetailId;

public class OrderDetailRepository extends Repository<OrderDetailId, OrderDetail> {

    public static final String INSERT_QUERY = "INSERT INTO "
            + "OrderDetail(OrderId, BookId, Author, Title, ImageUrl, Quantity, ItemPrice) "
            + "VALUES(?,?,?,?,?,?,?)";

    @Override
    public OrderDetail insert(OrderDetail t) throws Exception {
        synchronized (TRANSACTION_KEY) {
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
        }
        return t;
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
