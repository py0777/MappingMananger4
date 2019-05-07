package py0777.framework.core.data;

public class RecordHeader
  implements IRecordHeader
{
  private static final long serialVersionUID = -5744284934241510825L;
  

  private String name = null;
  
  private String label = null;
  
  private int type = 0;
  


  public RecordHeader(String name)
  {
    this(name, name, 12);
  }
  

  public RecordHeader(String name, int type)
  {
    this(name, name, type);
  }
  


  public RecordHeader(String name, String label)
  {
    this(name, label, 12);
  }
  



  public RecordHeader(String name, String label, int type)
  {
    setName(name);
    setLabel(label);
    setType(type);
  }
  

  public String getLabel()
  {
    return label;
  }
  


  public String getName()
  {
    return name;
  }
  


  public int getType()
  {
    return type;
  }
  



  public void setLabel(String label)
  {
    this.label = label;
  }
  


  public void setName(String name)
  {
    if ((name == null) || (name.trim().length() < 1)) {
      throw new NullPointerException("RecordHeader must have a valid name.");
    }
    
    this.name = name;
  }
  


  public void setType(int type)
  {
    this.type = type;
  }
  


  public Object clone()
  {
    return new RecordHeader(getName(), getLabel(), getType());
  }
  


  public String toString()
  {
    return getName();
  }
  


  public boolean equals(Object obj)
  {
    if (obj == null) {
      return false;
    }
    if ((obj instanceof IRecordHeader)) {
      return name.equalsIgnoreCase(((IRecordHeader)obj).getName());
    }
    if ((obj instanceof String)) {
      return name.equalsIgnoreCase((String)obj);
    }
    return super.equals(obj);
  }
}