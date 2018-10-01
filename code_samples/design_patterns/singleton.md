
# SINGLETON DESIGN PATTERN

## AUTHOR

Aamir Ahmed
Email: aamir.salaam@gmail.com
Phone: 408 931 3133

## PROBLEM DEFINITION

Develop an object that has only one instance and holds read-only data for other clients to refer.

## APPROACH

Used Java to create a helper class that holds some Key-Value pairs for clients to refer.

## EXAMPLE CODE

```java

/** 
  UnitOfMeasureHelper singleton helper class. This class loads the 
  unit of measure table from the database, and has a method that 
  gives the type of measure, namely Discrete or Continuous.
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
  } // end method


  // Member Data
  private static UnitOfMeasureHelper m_singleton = null;
  private static Object m_lock = new Object();
  private HashMap m_map = null; 
  private List  m_uomList = null; 
  private DictionaryController m_dictionaryCtrlRemote = null;
} // end class

```


## COPYRIGHT

Use these samples at your own risk.

