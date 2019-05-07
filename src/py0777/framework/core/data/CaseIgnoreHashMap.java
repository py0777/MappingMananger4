package py0777.framework.core.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class CaseIgnoreHashMap<K, V>
  implements Map<Object, Object>, Serializable
{
  private static final long serialVersionUID = 4268511433216020229L;
  private LinkedHashMap hmap = null;
  
  private LinkedHashSet keySet = null;
  
  public CaseIgnoreHashMap()
  {
    hmap = new LinkedHashMap();
    keySet = new LinkedHashSet();
  }
 
  public CaseIgnoreHashMap(int initialCapacity)
  {
    hmap = new LinkedHashMap(initialCapacity);
    keySet = new LinkedHashSet(initialCapacity);
  }
  
  public CaseIgnoreHashMap(int initialCapacity, float loadFactor) {
    hmap = new LinkedHashMap(initialCapacity, loadFactor);
    keySet = new LinkedHashSet(initialCapacity, loadFactor);
  }
  


  public void clear()
  {
    hmap.clear();
    keySet.clear();
  }
  


  public boolean containsKey(Object key)
  {
    return hmap.containsKey(upper(key));
  }
  



  public boolean containsValue(Object value)
  {
    return hmap.containsValue(value);
  }
  


  public Set entrySet()
  {
    return hmap.entrySet();
  }
  


  public Object get(Object key)
  {
    return hmap.get(upper(key));
  }
  


  public boolean isEmpty()
  {
    return hmap.isEmpty();
  }
  


  public Set keySet()
  {
    return keySet;
  }
  


  public Object put(Object key, Object value)
  {
    Object obj = hmap.put(upper(key), value);
    keySet.add(key);
    return obj;
  }
  


  public void putAll(Map t)
  {
    Iterator it = t.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry ent = (Map.Entry)it.next();
      hmap.put(upper(ent.getKey()), ent.getValue());
      keySet.add(ent.getKey());
    }
  }
  


  public Object remove(Object key)
  {
    Object obj = hmap.remove(upper(key));
    if (key == null) {
      keySet.remove(key);
    } else if ((key instanceof String)) {
      Iterator it = keySet.iterator();
      while (it.hasNext()) {
        Object each = it.next();
        if (((each instanceof String)) && 
          (((String)key).equalsIgnoreCase((String)each))) {
          keySet.remove(each);
          break;
        }
      }
    }
    
    return obj;
  }
  


  public int size()
  {
    return hmap.size();
  }
  


  public Collection values()
  {
    return hmap.values();
  }
  



  private Object upper(Object key)
  {
    if ((key instanceof String)) {
      key = ((String)key).toUpperCase();
    }
    return key;
  }
  




  public String toString()
  {
    StringBuilder buf = new StringBuilder();
    buf.append("{");
    
    Iterator i = keySet().iterator();
    boolean hasNext = i.hasNext();
    while (hasNext) {
      Object key = i.next();
      Object value = hmap.get(upper(key.toString()));
      buf.append((key == this ? "(this Map)" : key) + "=" + (value == this ? "(this Map)" : value));
      

      hasNext = i.hasNext();
      if (hasNext) {
        buf.append(", ");
      }
    }
    buf.append("}");
    return buf.toString();
  }
  



  public boolean equals(Object obj)
  {
    if (!(obj instanceof CaseIgnoreHashMap)) {
      return false;
    }
    return hmap.equals(hmap);
  }
}