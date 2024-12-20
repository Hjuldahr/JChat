import java.util.List;

public interface DAO<T> {

    /**
     * select a single entity by ID
     * @param id
     * @return entity, not null indicates success, null indicates failure
     */
    public T get(int id);
    /**
     * selects a subset of entities between start and start+size
     * @param start
     * @param size
     * @return list of entities, size >0 indicates success, size of 0 indicates no results or failure, 
     */
    public List<T> getAll(int start, int size); 
    /**
     * insert entities
     * @param entity
     * @return entity id, >-1 indicates success, -1 indicates failure to set
     */
    public int set(T entity);
    /**
     * update entities
     * @param entity
     * @return success flag, true for success, false for failure
     */
    public boolean update(T entity);
    /**
     * delete entities
     * @param id
     * @return success flag, true for success, false for failure
     */
    public boolean remove(int id);
}
