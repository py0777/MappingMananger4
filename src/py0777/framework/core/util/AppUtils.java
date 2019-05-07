package py0777.framework.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;



public class AppUtils
{
  private static final String PROPERTIES_LOCATION = "config/parameter/application.properties";
  private static final String READ_ENCODING = "UTF-8";
  private static Properties applicationProperties = null;
  
  private static Map<String, Boolean> applicationBooleanProperties = null;
  

  private static Properties environmentalValues = null;
  

  private static String osName = System.getProperty("os.name");
  

  private static String javaVersion = System.getProperty("java.version");
  




  public static String getProperty(String key)
  {
    if (applicationProperties == null) {
      applicationProperties = getProperties();
    }
    String value = applicationProperties.getProperty(key);
    if (value == null) {
      value = System.getProperty(key);
    }
    if (value == null) {
      if (javaVersion.indexOf("1.4") > -1)
      {
        value = System.getenv(key);
      } else {
        value = getEnv(key);
      }
    }
    return value;
  }
  



  public static boolean getBooleanProperty(String key, boolean defaultValue)
  {
    if (applicationBooleanProperties == null) {
      applicationBooleanProperties = new HashMap();
    }
    Boolean b = (Boolean)applicationBooleanProperties.get(key);
    if (b == null) {
      String value = getProperty(key);
      if ((value == null) || (value.length() == 0)) {
        return defaultValue;
      }
      switch (value.charAt(0)) {
      case 'T': 
      case 't': 
        b = Boolean.TRUE;
        break;
      case 'F': 
      case 'f': 
        b = Boolean.FALSE;
        break;
      default: 
        return defaultValue;
      }
      
      applicationBooleanProperties.put(key, b);
    }
    return b.booleanValue();
  }
  


  private static Properties getProperties()
  {
    Properties p = null; 

    p = new Properties();
 
    return p;
  }
  



  private static String getEnv(String key)
  {
    
    return environmentalValues.getProperty(key);
  }
  
  public static void reset()
  {
    applicationProperties = null;
    applicationBooleanProperties = null;
    environmentalValues = null;
  }
}
