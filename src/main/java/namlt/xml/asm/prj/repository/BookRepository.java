package namlt.xml.asm.prj.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.naming.NamingException;
import namlt.xml.asm.prj.model.Book;

public class BookRepository extends Repository<String, Book> {

    public static final String INSERT_QUERY = "INSERT INTO "
            + "Book(Id, Title, Author, Translator, "
            + "Price, PageSize, PageNumber, Isbn, "
            + "Status, ImageUrl, Url, InsertDate, Quantity, Description) "
            + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    @Override
    public Book insert(Book b) throws NamingException, SQLException {
        synchronized (TRANSACTION_KEY) {
            try {
                PreparedStatement preparedStatement = newPreparedStatement(INSERT_QUERY);
                preparedStatement.setString(1, b.getId());
                preparedStatement.setString(2, b.getTitle());
                preparedStatement.setString(3, b.getAuthor());
                preparedStatement.setString(4, b.getTranslator());
                preparedStatement.setDouble(5, b.getPrice());
                preparedStatement.setString(6, b.getPageSize());
                preparedStatement.setInt(7, b.getPageNumber());
                preparedStatement.setString(8, b.getIsbn());
                preparedStatement.setInt(9, b.getStatus());
                preparedStatement.setString(10, b.getImageUrl());
                preparedStatement.setString(11, b.getUrl());
                preparedStatement.setDate(12, new Date(b.getInsertDate().getTime()));
                preparedStatement.setInt(13, b.getQuantity());
                preparedStatement.setString(14, b.getDescription());
                int tmp = preparedStatement.executeUpdate();
                if (tmp <= 0) {
                    throw new SQLException("Insert book with id '" + b.getId() + "' fail!");
                }
            } finally {
                closeResources();
            }
        }
        return b;
    }

    @Override
    public void update(Book t) {
        synchronized (TRANSACTION_KEY) {

        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Book find(String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
