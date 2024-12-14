import java.sql.Connection;
import java.util.ArrayList;

public abstract class DAO<T>
{
    public Connection connection;
    
    public DAO(Connection connection)
    {
        this.connection = connection;
    }
    
    public abstract void add(T entity);
    public abstract T get(int id);
    public abstract ArrayList<T> getAll();
    public abstract void update(T entity);
    public abstract void remove(int id);
}
