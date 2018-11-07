package namlt.xml.asm.prj.repository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import namlt.xml.asm.prj.common.BookCommon;
import namlt.xml.asm.prj.model.Book;

public class BookRepository extends Repository<String, Book> implements BookCommon {

    public static final String INSERT_QUERY = "INSERT INTO "
            + "Book(Id, Title, Author, Translator, "
            + "Price, PageSize, PageNumber, Isbn, "
            + "Status, ImageUrl, Url, InsertDate, Quantity, Description) "
            + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static final String UPDATE_QUERY = "UPDATE Book "
            + "SET Title=?, Author=?, Translator=?, "
            + "Price=?, PageSize=?, PageNumber=?, Isbn=?, "
            + "Status=?, ImageUrl=?, Url=?, Quantity=?, Description=? "
            + "WHERE Id=?";
    public static final String QUERY_GET_NEW_BOOK = "SELECT * FROM Book WHERE Status=" + STATUS_ACTIVE + " ORDER BY InsertDate DESC "
            + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
    public static final String QUERY_GET_OUT_OF_STOCK_BOOK = "SELECT * FROM Book WHERE Status=" + STATUS_OUT_OF_STOCK + " ORDER BY InsertDate DESC "
            + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
    public static final String QUERY_GET_DISABLE_BOOK = "SELECT * FROM Book WHERE Status=" + STATUS_DISABLE + " ORDER BY InsertDate DESC "
            + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
    public static final String QUERY_COUNT = "SELECT COUNT(*) AS 'COUNT' FROM Book";
    public static final String QUERY_COUNT_ACTIVE = "SELECT COUNT(*) AS 'COUNT' FROM Book WHERE Status=" + STATUS_ACTIVE;
    public static final String QUERY_COUNT_OUT_OF_STOCK = "SELECT COUNT(*) AS 'COUNT' FROM Book WHERE Status=" + STATUS_OUT_OF_STOCK;
    public static final String QUERY_COUNT_DISABLE = "SELECT COUNT(*) AS 'COUNT' FROM Book WHERE Status=" + STATUS_DISABLE;
    public static final String QUERY_BOOK_BY_ID = "SELECT * FROM Book WHERE Id=?";
    public static final String QUERY_BOOK_BY_TITLE = "SELECT * FROM Book WHERE Title LIKE ? ORDER BY InsertDate DESC "
            + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
    public static final String QUERY_COUNT_BOOK_BY_TITLE = "SELECT COUNT(*) AS 'COUNT' FROM Book WHERE Title LIKE ?";
    public static final String QUERY_BOOK_BY_TITLE_AND_STATUS = "SELECT * FROM Book "
            + "WHERE Title LIKE ? AND [Status]=? "
            + "ORDER BY InsertDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
    public static final String QUERY_COUNT_BOOK_BY_TITLE_AND_STATUS = "SELECT COUNT(*) AS 'COUNT' FROM Book "
            + "WHERE Title LIKE ? AND [Status]=?";

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
                if (b.getPageNumber() != null) {
                    preparedStatement.setInt(7, b.getPageNumber());
                } else {
                    preparedStatement.setNull(7, Types.INTEGER);
                }
                preparedStatement.setString(8, b.getIsbn());
                preparedStatement.setInt(9, b.getStatus());
                preparedStatement.setString(10, b.getImageUrl());
                preparedStatement.setString(11, b.getUrl());
                preparedStatement.setTimestamp(12, new Timestamp(b.getInsertDate().getTime()));
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
    public void update(Book b) throws NamingException, SQLException {
        synchronized (TRANSACTION_KEY) {
            try {
                preparedStatement = newPreparedStatement(UPDATE_QUERY);
                preparedStatement.setString(1, b.getTitle());
                preparedStatement.setString(2, b.getAuthor());
                preparedStatement.setString(3, b.getTranslator());
                preparedStatement.setDouble(4, b.getPrice());
                preparedStatement.setString(5, b.getPageSize());
                if (b.getPageNumber() != null) {
                    preparedStatement.setInt(6, b.getPageNumber());
                } else {
                    preparedStatement.setNull(6, Types.INTEGER);
                }
                preparedStatement.setString(7, b.getIsbn());
                preparedStatement.setInt(8, b.getStatus());
                preparedStatement.setString(9, b.getImageUrl());
                preparedStatement.setString(10, b.getUrl());
                preparedStatement.setInt(11, b.getQuantity());
                preparedStatement.setString(12, b.getDescription());
                preparedStatement.setString(13, b.getId());
                int tmp = preparedStatement.executeUpdate();
                if (tmp <= 0) {
                    throw new SQLException("Update book with id '" + b.getId() + "' fail!");
                }
            } finally {
                closeResources();
            }
        }
    }

    @Override
    public Book get(String key) throws NamingException, SQLException {
        Book b = null;
        synchronized (TRANSACTION_KEY) {
            try {
                preparedStatement = newPreparedStatement(QUERY_BOOK_BY_ID);
                preparedStatement.setString(1, key);
                resultSet = preparedStatement.executeQuery();
                List<Book> rsList = extractDataFromResultSet();
                if (rsList.size() > 0) {
                    b = rsList.get(0);
                }
            } finally {
                closeResources();
            }
        }
        return b;
    }

    private List<Book> extractDataFromResultSet() throws SQLException {
        Book b;
        List<Book> rs = new ArrayList<>();
        while (resultSet.next()) {
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
            rs.add(b);
        }
        return rs;
    }

    public List<Book> getNew(int startAt, int nextRow) throws Exception {
        return getBookByQuery(QUERY_GET_NEW_BOOK, startAt, nextRow);
    }

    public List<Book> getOutOfStock(int startAt, int nextRow) throws Exception {
        return getBookByQuery(QUERY_GET_OUT_OF_STOCK_BOOK, startAt, nextRow);
    }

    public List<Book> getDisable(int startAt, int nextRow) throws Exception {
        return getBookByQuery(QUERY_GET_DISABLE_BOOK, startAt, nextRow);
    }

    private List<Book> getBookByQuery(String query, int startAt, int nextRow) throws Exception {
        List<Book> rs = null;
        synchronized (TRANSACTION_KEY) {
            try {
                preparedStatement = newPreparedStatement(query);
                preparedStatement.setInt(1, startAt);
                preparedStatement.setInt(2, nextRow);
                resultSet = preparedStatement.executeQuery();
                rs = extractDataFromResultSet();
            } finally {
                closeResources();
            }
        }
        return rs;
    }

    @Override
    public List<Book> find(String s, Integer startAt, Integer nextRow) throws Exception {
        List<Book> rs = null;
        synchronized (TRANSACTION_KEY) {
            try {
                preparedStatement = newPreparedStatement(QUERY_BOOK_BY_TITLE);
                preparedStatement.setString(1, '%' + s + '%');
                preparedStatement.setInt(2, startAt);
                preparedStatement.setInt(3, nextRow);
                resultSet = preparedStatement.executeQuery();
                rs = extractDataFromResultSet();
            } finally {
                closeResources();
            }
        }
        return rs;
    }

    @Override
    public int count() throws Exception {
        synchronized (TRANSACTION_KEY) {
            try {
                preparedStatement = newPreparedStatement(QUERY_COUNT);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("COUNT");
                }
            } finally {
                closeResources();
            }
        }
        return 0;
    }

    public int countActive() throws Exception {
        synchronized (TRANSACTION_KEY) {
            try {
                preparedStatement = newPreparedStatement(QUERY_COUNT_ACTIVE);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("COUNT");
                }
            } finally {
                closeResources();
            }
        }
        return 0;
    }

    public int countSearch(String s) throws Exception {
        synchronized (TRANSACTION_KEY) {
            try {
                preparedStatement = newPreparedStatement(QUERY_COUNT_BOOK_BY_TITLE);
                preparedStatement.setString(1, '%' + s + '%');
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("COUNT");
                }
            } finally {
                closeResources();
            }
        }
        return 0;
    }

    public int countOutOfStock() throws Exception {
        synchronized (TRANSACTION_KEY) {
            try {
                preparedStatement = newPreparedStatement(QUERY_COUNT_OUT_OF_STOCK);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("COUNT");
                }
            } finally {
                closeResources();
            }
        }
        return 0;
    }

    public int countDisable() throws Exception {
        synchronized (TRANSACTION_KEY) {
            try {
                preparedStatement = newPreparedStatement(QUERY_COUNT_DISABLE);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("COUNT");
                }
            } finally {
                closeResources();
            }
        }
        return 0;
    }

    public List<Book> findByTitleAndType(String s, int type, Integer startAt, Integer nextRow) throws Exception {
        List<Book> rs = null;
        synchronized (TRANSACTION_KEY) {
            try {
                preparedStatement = newPreparedStatement(QUERY_BOOK_BY_TITLE_AND_STATUS);
                preparedStatement.setString(1, '%' + s + '%');
                preparedStatement.setInt(2, type);
                preparedStatement.setInt(3, startAt);
                preparedStatement.setInt(4, nextRow);
                resultSet = preparedStatement.executeQuery();
                rs = extractDataFromResultSet();
            } finally {
                closeResources();
            }
        }
        return rs;
    }

    public int countSearchByTitleAndType(String s, int type) throws Exception {
        synchronized (TRANSACTION_KEY) {
            try {
                preparedStatement = newPreparedStatement(QUERY_COUNT_BOOK_BY_TITLE);
                preparedStatement.setString(1, '%' + s + '%');
                preparedStatement.setInt(2, type);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("COUNT");
                }
            } finally {
                closeResources();
            }
        }
        return 0;
    }
}
