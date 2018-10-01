
# SINGLETON DESIGN PATTERN

## AUTHOR

Aamir Ahmed
Email: aamir.salaam@gmail.com
Phone: 408 931 3133

## PROBLEM DEFINITION

Develop an object that has only one instance and holds read-only data for other clients to refer.

## APPROACH

Used Java to create a helper class that holds some Key-Value pairs for clients to refer.

Only one instance of this object is to be created.

The above constraint is achieved by making the constructor of this class private and providing a public method to get the instance of this class.


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

    m_map = new HashMap();
    // Load the hash map with key-value pairs.
    // The first argument is the key.
    // The second argument is of Boolean type, and a true value
    // signifies the discrete unit of measure.

    m_map.put("EA", new Boolean(true));
    m_map.put("INCH", new Boolean(false));
    m_map.put("KG", new Boolean(false));

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

