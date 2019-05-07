package py0777.framework.core.data;

public abstract interface IPagination
{
  public abstract int getPageNo();
  
  public abstract int getRecordCountPerPage();
  
  public abstract int getTotalRecordCount();
  
  public abstract boolean isPaging();
  
  public abstract boolean hasNextPage();
}

