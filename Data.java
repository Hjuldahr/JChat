import java.io.Serializable;
import java.util.HashMap;

public class Data implements Serializable
{
    private int id;
    private String status;
    private HashMap<String,String> contents;

    public Data(int id, String status, HashMap<String,String> contents)
    {
        this.id = id;
        this.status = status;
        this.contents = contents;
    }
    public Data(int id, String status)
    {
        this(id, status, new HashMap<>());
    }
    public Data(int id, String status, String[] key_values)
    {
        this(id, status);
        setContents(key_values);
    }

    public int getID()
    {
        return id;
    }

    public String getStatus()
    {
        return status;
    }

    private void setContents(String[] key_values)
    {
        for (int i = 0; i < key_values.length; i+=2)
        {
            contents.put(key_values[i], key_values[i+1]);
        }
    }

    public String getContent(String key)
    {
        return contents.get(key);
    }

    public HashMap<String,String> getContents()
    {
        return contents;
    }

    @Override
    public String toString() 
    {
        return "data={status=\'" + status + "\', content=" + contents + "}";
    }
}
