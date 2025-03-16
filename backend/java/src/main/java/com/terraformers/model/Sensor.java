package com.terraformers.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.time.LocalDateTime;
import java.util.*;

// line 158 "model.ump"
// line 256 "model.ump"
public class Sensor
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum SensorMountPosition { Front, Back, Left, Right }
  public enum SensorStatus { Active, Inactive, Faulty }
  public enum SensorType { Type1, Type2 }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Sensor Attributes
  private int sensorID;
  private SensorMountPosition mountPosition;
  private SensorStatus status;
  private SensorType sensorType;

  //Sensor Associations
  private List<SensorReading> sensorReadings;
  private Avatar avatar;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Sensor(int aSensorID, SensorMountPosition aMountPosition, SensorStatus aStatus, SensorType aSensorType, Avatar aAvatar)
  {
    sensorID = aSensorID;
    mountPosition = aMountPosition;
    status = aStatus;
    sensorType = aSensorType;
    sensorReadings = new ArrayList<SensorReading>();
    boolean didAddAvatar = setAvatar(aAvatar);
    if (!didAddAvatar)
    {
      throw new RuntimeException("Unable to create sensor due to avatar. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setSensorID(int aSensorID)
  {
    boolean wasSet = false;
    sensorID = aSensorID;
    wasSet = true;
    return wasSet;
  }

  public boolean setMountPosition(SensorMountPosition aMountPosition)
  {
    boolean wasSet = false;
    mountPosition = aMountPosition;
    wasSet = true;
    return wasSet;
  }

  public boolean setStatus(SensorStatus aStatus)
  {
    boolean wasSet = false;
    status = aStatus;
    wasSet = true;
    return wasSet;
  }

  public boolean setSensorType(SensorType aSensorType)
  {
    boolean wasSet = false;
    sensorType = aSensorType;
    wasSet = true;
    return wasSet;
  }

  public int getSensorID()
  {
    return sensorID;
  }

  public SensorMountPosition getMountPosition()
  {
    return mountPosition;
  }

  public SensorStatus getStatus()
  {
    return status;
  }

  public SensorType getSensorType()
  {
    return sensorType;
  }
  /* Code from template association_GetMany */
  public SensorReading getSensorReading(int index)
  {
    SensorReading aSensorReading = sensorReadings.get(index);
    return aSensorReading;
  }

  /**
   * Association: Each Sensor belongs to one Avatar.
   * Association: One Sensor has many SensorReadings.
   */
  public List<SensorReading> getSensorReadings()
  {
    List<SensorReading> newSensorReadings = Collections.unmodifiableList(sensorReadings);
    return newSensorReadings;
  }

  public int numberOfSensorReadings()
  {
    int number = sensorReadings.size();
    return number;
  }

  public boolean hasSensorReadings()
  {
    boolean has = sensorReadings.size() > 0;
    return has;
  }

  public int indexOfSensorReading(SensorReading aSensorReading)
  {
    int index = sensorReadings.indexOf(aSensorReading);
    return index;
  }
  /* Code from template association_GetOne */
  public Avatar getAvatar()
  {
    return avatar;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfSensorReadings()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public SensorReading addSensorReading(int aReadingID, LocalDateTime aTimeStamp, float aValue)
  {
    return new SensorReading(aReadingID, aTimeStamp, aValue, this);
  }

  public boolean addSensorReading(SensorReading aSensorReading)
  {
    boolean wasAdded = false;
    if (sensorReadings.contains(aSensorReading)) { return false; }
    Sensor existingSensor = aSensorReading.getSensor();
    boolean isNewSensor = existingSensor != null && !this.equals(existingSensor);
    if (isNewSensor)
    {
      aSensorReading.setSensor(this);
    }
    else
    {
      sensorReadings.add(aSensorReading);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeSensorReading(SensorReading aSensorReading)
  {
    boolean wasRemoved = false;
    //Unable to remove aSensorReading, as it must always have a sensor
    if (!this.equals(aSensorReading.getSensor()))
    {
      sensorReadings.remove(aSensorReading);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addSensorReadingAt(SensorReading aSensorReading, int index)
  {  
    boolean wasAdded = false;
    if(addSensorReading(aSensorReading))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSensorReadings()) { index = numberOfSensorReadings() - 1; }
      sensorReadings.remove(aSensorReading);
      sensorReadings.add(index, aSensorReading);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveSensorReadingAt(SensorReading aSensorReading, int index)
  {
    boolean wasAdded = false;
    if(sensorReadings.contains(aSensorReading))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSensorReadings()) { index = numberOfSensorReadings() - 1; }
      sensorReadings.remove(aSensorReading);
      sensorReadings.add(index, aSensorReading);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addSensorReadingAt(aSensorReading, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToMany */
  public boolean setAvatar(Avatar aAvatar)
  {
    boolean wasSet = false;
    if (aAvatar == null)
    {
      return wasSet;
    }

    Avatar existingAvatar = avatar;
    avatar = aAvatar;
    if (existingAvatar != null && !existingAvatar.equals(aAvatar))
    {
      existingAvatar.removeSensor(this);
    }
    avatar.addSensor(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    for(int i=sensorReadings.size(); i > 0; i--)
    {
      SensorReading aSensorReading = sensorReadings.get(i - 1);
      aSensorReading.delete();
    }
    Avatar placeholderAvatar = avatar;
    this.avatar = null;
    if(placeholderAvatar != null)
    {
      placeholderAvatar.removeSensor(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "sensorID" + ":" + getSensorID()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "mountPosition" + "=" + (getMountPosition() != null ? !getMountPosition().equals(this)  ? getMountPosition().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "status" + "=" + (getStatus() != null ? !getStatus().equals(this)  ? getStatus().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "sensorType" + "=" + (getSensorType() != null ? !getSensorType().equals(this)  ? getSensorType().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "avatar = "+(getAvatar()!=null?Integer.toHexString(System.identityHashCode(getAvatar())):"null");
  }
}