package dataaccess;

public interface DataDAO {
    public void clear() throws DataAccessException;

    public boolean isEmpty() throws DataAccessException;
}
