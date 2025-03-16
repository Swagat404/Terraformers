package com.terraformers.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/



// line 169 "model.ump"
// line 262 "model.ump"

import java.time.LocalDateTime;

public class SensorReading extends Reading
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //SensorReading Attributes
  private float value;

  //SensorReading Associations
  private Sensor sensor;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public SensorReading(int aReadingID, LocalDateTime aTimeStamp, float aValue, Sensor aSensor)
  {
    super(aReadingID, aTimeStamp);
    value = aValue;
    boolean didAddSensor = setSensor(aSensor);
    if (!didAddSensor)
    {
      throw new RuntimeException("Unable to create sensorReading due to sensor. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setValue(float aValue)
  {
    boolean wasSet = false;
    value = aValue;
    wasSet = true;
    return wasSet;
  }

  public float getValue()
  {
    return value;
  }
  /* Code from template association_GetOne */
  public Sensor getSensor()
  {
    return sensor;
  }
  /* Code from template association_SetOneToMany */
  public boolean setSensor(Sensor aSensor)
  {
    boolean wasSet = false;
    if (aSensor == null)
    {
      return wasSet;
    }

    Sensor existingSensor = sensor;
    sensor = aSensor;
    if (existingSensor != null && !existingSensor.equals(aSensor))
    {
      existingSensor.removeSensorReading(this);
    }
    sensor.addSensorReading(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Sensor placeholderSensor = sensor;
    this.sensor = null;
    if(placeholderSensor != null)
    {
      placeholderSensor.removeSensorReading(this);
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "value" + ":" + getValue()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "sensor = "+(getSensor()!=null?Integer.toHexString(System.identityHashCode(getSensor())):"null");
  }
}


