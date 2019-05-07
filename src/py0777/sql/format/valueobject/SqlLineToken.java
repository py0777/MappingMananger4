package py0777.sql.format.valueobject;

public class SqlLineToken {

	  private int fIndent;
	  private int fIndex;
	  private String fString;
	  
	  
	  public void setIndent(int indent)
	  {
		  fIndent = indent;
	  }
	 
	  public int getIndent()
	  {
	    return fIndent;
	  }
	  
	  public void setIndex(int index)
	  {
		  fIndex = index;
	  }
	 
	  public int getIndex()
	  {
	    return fIndex;
	  }
	  
	  public void setValue(String value)
	  {
	    fString = value;
	  }
	  
	  public String getValue()
	  {
	    return fString;
	  }
	  
	  public SqlLineToken(int indent, int index, String value)
	  {
	    setIndent(indent);
	    setIndex(index);
	    setValue(value);
	    
	  }
}
