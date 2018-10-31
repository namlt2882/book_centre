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

    public static final String QUERY_BOOK_BY_ID = "SELECT * FROM Book WHERE Id=?";

    @Override
    public Book insert(Book b) throws NamingException, SQLException {
        synchronized (TRANSACTION_KEY) {
            try {
                preparedStatement = newPreparedStatement(INSERT_QUERY);
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
    public Book find(String key) throws NamingException, SQLException {
        Book b = null;
        synchronized (TRANSACTION_KEY) {
            try {
                preparedStatement = newPreparedStatement(QUERY_BOOK_BY_ID);
                preparedStatement.setString(1, key);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    b = new Book();
                    b.setId(resultSet.getString("Id"));
                    b.setTitle(resultSet.getString("Title"));
                    b.setAuthor(resultSet.getString("Author"));
                    b.setTranslator(resultSet.getString("Translator"));
                    b.setPrice(resultSet.getDouble("Price"));
                    b.setPageSize(resultSet.getString("PageSize"));
                    b.setPageNumber(resultSet.getInt("PageNumber"));
                    b.setIsbn(resultSet.getString("Isbn"));
                    b.setStatus(resultSet.getInt("Status"));
                    b.setImageUrl(resultSet.getString("ImageUrl"));
                    b.setUrl(resultSet.getString("Url"));
                    b.setInsertDate(resultSet.getDate("InsertDate"));
                    b.setQuantity(resultSet.getInt("Quantity"));
                    b.setDescription(resultSet.getString("Description"));
                }
            } finally {
                closeResources();
            }
        }
        return b;
    }

}
