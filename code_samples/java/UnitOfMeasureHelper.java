package com.hidden.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.hidden.dictionary.DictionaryControllerHome;
import com.hidden.dictionary.DictionaryController;
import com.hidden.dictionary.UnitOfMeasureAttributes;

import com.hidden.base.InfoLogger;

/** 
  UnitOfMeasureHelper singleton helper class. This class loads the 
  unit of measure table from the database, and has a method that 
  gives the type of measure, namely Discrete or Continuous.

  @author  asalaam@hidden.com
  @version $Revision: 1.5 $
*/

public class UnitOfMeasureHelper
{

  /** 
    Private constructor for UnitOfMeasureHelper class. This constructor
    is responsible to populate the hash map with the unit of measure
    table from the database. In this table, we have a boolean flag
    named isDiscrete, true value signifies Discrete Measure, and a false
    value signifies Continuous Measure.
  */

  private UnitOfMeasureHelper() 
  {

   /*
	m_map = new HashMap();
    // Load the hash map with key-value pairs.
    // The first argument is the key.
    // The second argument is of Boolean type, and a true value
    // signifies the discrete unit of measure.

    m_map.put("EA", new Boolean(true));
    m_map.put("INCH", new Boolean(false));
    m_map.put("KG", new Boolean(false));

   */
   
    try
	{
      m_uomList = new ArrayList();
      Context context = new InitialContext();


      DictionaryControllerHome dictionaryCtrlHome =
         (DictionaryControllerHome) context.lookup("DictionaryController");
    
      m_dictionaryCtrlRemote = dictionaryCtrlHome.create();
         

      m_uomList  = m_dictionaryCtrlRemote.getDictionaryList("UOM");
	}
	catch(Exception e)
	{
	  InfoLogger.writeException(e);
	  // We are not throwing these Exceptions up since we need to keep the Interface
	  // from not throwing anything other than NoSuchUnitOfMeasureException
	  // (thrownfrom  isDiscrete() method). This will eventually change since
	  //the Dictionary will provide UnitOfMeasureHelper which will replace this
	  //UnitOfMeasureHelper
	}


/*

    try
    {

      String jdbcURL = "jdbc:oracle:oci8:@devexch";
      String userName = "aamir";
      String password = "aamir";

      DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
      Connection conn = DriverManager.getConnection(jdbcURL, userName, password);

      com.hidden.base.CallableStatement callableStatement = 
                                            new com.hidden.base.CallableStatement(conn, "COMMON_PKG.GET_UOM", 0, 1);

      ResultSet resultSet = callableStatement.executeQuery();

      while (resultSet.next())
      {
        m_map.put(resultSet.getString("UNIT_OF_MEASURE_CD"), new Boolean(resultSet.getString("IS_DISCRETE")));
      } // end while

      conn.close();

    } // end try

    catch(SQLException e)
    {
      System.out.println("SQLException " + e + "\n");
    } // end catch

*/

  } // end method


  /**
    This method is used to get the instance of this singleton helper class.

    @return UnitOfMeasureHelper
  */

  public static UnitOfMeasureHelper getSingleton() {
    if (null == m_singleton)
    {
      synchronized (m_lock)
      {
        if (null == m_singleton)
        {
          m_singleton = new UnitOfMeasureHelper();
        } // end if
      } // end synchronized
    } // end if

    return m_singleton;
  } // end method


  /**
    This business method returns the boolean type, distinguishing
    between discrete and continuous measures. True signifies Discrete
    Measure and a false signifies Continuous Measure. If unitOfMeasure
    doesn't have it's key in the hash map, then throw 
    NoSuchUnitOfMeasureException.

    @param String
    @return boolean
    @throws NoSuchUnitOfMeasureException
  */

  public boolean isDiscrete(String unitOfMeasure) 
    throws NoSuchUnitOfMeasureException 
  {

    boolean isDiscrete = false;
    if (null == unitOfMeasure)
      throw new IllegalArgumentException("UnitOfMeasureHelper::isDiscrete: required param unitOfMeasure is NULL");
      
     
     if(!m_uomList.contains(unitOfMeasure) ) 
	 {
	   throw new NoSuchUnitOfMeasureException();
	 }

     UnitOfMeasureAttributes uomAttrib = null;
	 try
	 {
        uomAttrib = 
         (UnitOfMeasureAttributes) m_dictionaryCtrlRemote.getDictionaryAttribute("UOM",unitOfMeasure);
	 }
	 catch(Exception e)
	 {
	   InfoLogger.writeException(e);
	  // We are not throwing these Exceptions up since we need to keep the Interface
	  // from not throwing anything other than NoSuchUnitOfMeasureException
	  // (thrownfrom  isDiscrete() method). This will eventually change since
	  //the Dictionary will provide UnitOfMeasureHelper which will replace this
	  //UnitOfMeasureHelper
	 }

     if ( uomAttrib!=null)
         isDiscrete = uomAttrib.isDiscrete();

     
     //This method might return false if it got Exceptions when trying to find
	 //the UOMAttributes.For now we are leaving this as it is.But we should change this Soon

     
     return isDiscrete;
 
 
    /*  
    
    
    
    
    if (unitOfMeasure.equals("EA"))
    {

      // Since most of the discrete measures are "EA", as part
      // of optimization we have this special check.

      return true;

    }
    else
    {

      if (m_map.containsKey(unitOfMeasure))
      {
        return ((Boolean) m_map.get(unitOfMeasure)).booleanValue();
      }
      else
      {
        // No record of this key in the database table.
        throw new NoSuchUnitOfMeasureException();
      } // end if

    } // end if

    */

  } // end method


  // Member Data
  private static UnitOfMeasureHelper m_singleton = null;
  private static Object m_lock = new Object();
  private HashMap m_map = null; 
  private List  m_uomList = null; 
  private DictionaryController m_dictionaryCtrlRemote = null;


  /* 
    Remove this method implementation when EntityAdapter provides this method.
  */

/*
  private static java.sql.Connection getConnection() throws java.sql.SQLException
  {
    String jdbcURL = "jdbc:oracle:oci8:@devexch";
    String userName = "aamir";
    String password = "aamir";

    java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
    java.sql.Connection conn = java.sql.DriverManager.getConnection(jdbcURL, userName, password);

    return conn;
  } // end method

*/

} // end class
