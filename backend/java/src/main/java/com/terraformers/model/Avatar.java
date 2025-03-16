package com.terraformers.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.util.*;
import java.sql.Date;
import java.sql.Time;

// line 109 "model.ump"
// line 230 "model.ump"
public class Avatar
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum LogType { Info, Warning, Error }
  public enum AvatarColor { Red, Blue, Green, Yellow }
  public enum SensorMountPosition { Front, Back, Left, Right }
  public enum SensorStatus { Active, Inactive, Faulty }
  public enum SensorType { Type1, Type2 }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Avatar Attributes
  private int avatarID;
  private String avatarName;
  private AvatarColor avatarColor;

  //Avatar Associations
  private Location location;
  private List<AvatarLog> avatarLogs;
  private List<Sensor> sensors;
  private AvatarBrain avatarBrain;
  private Mission mission;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Avatar(int aAvatarID, String aAvatarName, AvatarColor aAvatarColor, Location aLocation, AvatarBrain aAvatarBrain, Mission aMission)
  {
    avatarID = aAvatarID;
    avatarName = aAvatarName;
    avatarColor = aAvatarColor;
    boolean didAddLocation = setLocation(aLocation);
    if (!didAddLocation)
    {
      throw new RuntimeException("Unable to create avatar due to location. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    avatarLogs = new ArrayList<AvatarLog>();
    sensors = new ArrayList<Sensor>();
    boolean didAddAvatarBrain = setAvatarBrain(aAvatarBrain);
    if (!didAddAvatarBrain)
    {
      throw new RuntimeException("Unable to create avatar due to avatarBrain. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddMission = setMission(aMission);
    if (!didAddMission)
    {
      throw new RuntimeException("Unable to create avatar due to mission. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setAvatarID(int aAvatarID)
  {
    boolean wasSet = false;
    avatarID = aAvatarID;
    wasSet = true;
    return wasSet;
  }

  public boolean setAvatarName(String aAvatarName)
  {
    boolean wasSet = false;
    avatarName = aAvatarName;
    wasSet = true;
    return wasSet;
  }

  public boolean setAvatarColor(AvatarColor aAvatarColor)
  {
    boolean wasSet = false;
    avatarColor = aAvatarColor;
    wasSet = true;
    return wasSet;
  }

  public int getAvatarID()
  {
    return avatarID;
  }

  public String getAvatarName()
  {
    return avatarName;
  }

  public AvatarColor getAvatarColor()
  {
    return avatarColor;
  }
  /* Code from template association_GetOne */
  public Location getLocation()
  {
    return location;
  }
  /* Code from template association_GetMany */
  public AvatarLog getAvatarLog(int index)
  {
    AvatarLog aAvatarLog = avatarLogs.get(index);
    return aAvatarLog;
  }

  /**
   * Association: One Avatar has many AvatarLogs.
   */
  public List<AvatarLog> getAvatarLogs()
  {
    List<AvatarLog> newAvatarLogs = Collections.unmodifiableList(avatarLogs);
    return newAvatarLogs;
  }

  public int numberOfAvatarLogs()
  {
    int number = avatarLogs.size();
    return number;
  }

  public boolean hasAvatarLogs()
  {
    boolean has = avatarLogs.size() > 0;
    return has;
  }

  public int indexOfAvatarLog(AvatarLog aAvatarLog)
  {
    int index = avatarLogs.indexOf(aAvatarLog);
    return index;
  }
  /* Code from template association_GetMany */
  public Sensor getSensor(int index)
  {
    Sensor aSensor = sensors.get(index);
    return aSensor;
  }

  /**
   * Association: One Avatar has many Sensors.
   */
  public List<Sensor> getSensors()
  {
    List<Sensor> newSensors = Collections.unmodifiableList(sensors);
    return newSensors;
  }

  public int numberOfSensors()
  {
    int number = sensors.size();
    return number;
  }

  public boolean hasSensors()
  {
    boolean has = sensors.size() > 0;
    return has;
  }

  public int indexOfSensor(Sensor aSensor)
  {
    int index = sensors.indexOf(aSensor);
    return index;
  }
  /* Code from template association_GetOne */
  public AvatarBrain getAvatarBrain()
  {
    return avatarBrain;
  }
  /* Code from template association_GetOne */
  public Mission getMission()
  {
    return mission;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setLocation(Location aNewLocation)
  {
    boolean wasSet = false;
    if (aNewLocation == null)
    {
      //Unable to setLocation to null, as avatar must always be associated to a location
      return wasSet;
    }
    
    Avatar existingAvatar = aNewLocation.getAvatar();
    if (existingAvatar != null && !equals(existingAvatar))
    {
      //Unable to setLocation, the current location already has a avatar, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    Location anOldLocation = location;
    location = aNewLocation;
    location.setAvatar(this);

    if (anOldLocation != null)
    {
      anOldLocation.setAvatar(null);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfAvatarLogs()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public AvatarLog addAvatarLog(int aLogID, Date aLogDate, Time aLogTime, Log.LogType aLogType, String aLogMessage)
  {
    return new AvatarLog(aLogID, aLogDate, aLogTime, aLogType, aLogMessage, this);
  }

  public boolean addAvatarLog(AvatarLog aAvatarLog)
  {
    boolean wasAdded = false;
    if (avatarLogs.contains(aAvatarLog)) { return false; }
    Avatar existingAvatar = aAvatarLog.getAvatar();
    boolean isNewAvatar = existingAvatar != null && !this.equals(existingAvatar);
    if (isNewAvatar)
    {
      aAvatarLog.setAvatar(this);
    }
    else
    {
      avatarLogs.add(aAvatarLog);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeAvatarLog(AvatarLog aAvatarLog)
  {
    boolean wasRemoved = false;
    //Unable to remove aAvatarLog, as it must always have a avatar
    if (!this.equals(aAvatarLog.getAvatar()))
    {
      avatarLogs.remove(aAvatarLog);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addAvatarLogAt(AvatarLog aAvatarLog, int index)
  {  
    boolean wasAdded = false;
    if(addAvatarLog(aAvatarLog))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAvatarLogs()) { index = numberOfAvatarLogs() - 1; }
      avatarLogs.remove(aAvatarLog);
      avatarLogs.add(index, aAvatarLog);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveAvatarLogAt(AvatarLog aAvatarLog, int index)
  {
    boolean wasAdded = false;
    if(avatarLogs.contains(aAvatarLog))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAvatarLogs()) { index = numberOfAvatarLogs() - 1; }
      avatarLogs.remove(aAvatarLog);
      avatarLogs.add(index, aAvatarLog);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addAvatarLogAt(aAvatarLog, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfSensors()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Sensor addSensor(int aSensorID, Sensor.SensorMountPosition aMountPosition, Sensor.SensorStatus aStatus, Sensor.SensorType aSensorType)
  {
    return new Sensor(aSensorID, aMountPosition, aStatus, aSensorType, this);
  }

  public boolean addSensor(Sensor aSensor)
  {
    boolean wasAdded = false;
    if (sensors.contains(aSensor)) { return false; }
    Avatar existingAvatar = aSensor.getAvatar();
    boolean isNewAvatar = existingAvatar != null && !this.equals(existingAvatar);
    if (isNewAvatar)
    {
      aSensor.setAvatar(this);
    }
    else
    {
      sensors.add(aSensor);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeSensor(Sensor aSensor)
  {
    boolean wasRemoved = false;
    //Unable to remove aSensor, as it must always have a avatar
    if (!this.equals(aSensor.getAvatar()))
    {
      sensors.remove(aSensor);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addSensorAt(Sensor aSensor, int index)
  {  
    boolean wasAdded = false;
    if(addSensor(aSensor))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSensors()) { index = numberOfSensors() - 1; }
      sensors.remove(aSensor);
      sensors.add(index, aSensor);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveSensorAt(Sensor aSensor, int index)
  {
    boolean wasAdded = false;
    if(sensors.contains(aSensor))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSensors()) { index = numberOfSensors() - 1; }
      sensors.remove(aSensor);
      sensors.add(index, aSensor);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addSensorAt(aSensor, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setAvatarBrain(AvatarBrain aNewAvatarBrain)
  {
    boolean wasSet = false;
    if (aNewAvatarBrain == null)
    {
      //Unable to setAvatarBrain to null, as avatar must always be associated to a avatarBrain
      return wasSet;
    }
    
    Avatar existingAvatar = aNewAvatarBrain.getAvatar();
    if (existingAvatar != null && !equals(existingAvatar))
    {
      //Unable to setAvatarBrain, the current avatarBrain already has a avatar, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    AvatarBrain anOldAvatarBrain = avatarBrain;
    avatarBrain = aNewAvatarBrain;
    avatarBrain.setAvatar(this);

    if (anOldAvatarBrain != null)
    {
      anOldAvatarBrain.setAvatar(null);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setMission(Mission aMission)
  {
    boolean wasSet = false;
    if (aMission == null)
    {
      return wasSet;
    }

    Mission existingMission = mission;
    mission = aMission;
    if (existingMission != null && !existingMission.equals(aMission))
    {
      existingMission.removeAvatar(this);
    }
    mission.addAvatar(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Location existingLocation = location;
    location = null;
    if (existingLocation != null)
    {
      existingLocation.setAvatar(null);
    }
    for(int i=avatarLogs.size(); i > 0; i--)
    {
      AvatarLog aAvatarLog = avatarLogs.get(i - 1);
      aAvatarLog.delete();
    }
    for(int i=sensors.size(); i > 0; i--)
    {
      Sensor aSensor = sensors.get(i - 1);
      aSensor.delete();
    }
    AvatarBrain existingAvatarBrain = avatarBrain;
    avatarBrain = null;
    if (existingAvatarBrain != null)
    {
      existingAvatarBrain.setAvatar(null);
    }
    Mission placeholderMission = mission;
    this.mission = null;
    if(placeholderMission != null)
    {
      placeholderMission.removeAvatar(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "avatarID" + ":" + getAvatarID()+ "," +
            "avatarName" + ":" + getAvatarName()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "avatarColor" + "=" + (getAvatarColor() != null ? !getAvatarColor().equals(this)  ? getAvatarColor().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "location = "+(getLocation()!=null?Integer.toHexString(System.identityHashCode(getLocation())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "avatarBrain = "+(getAvatarBrain()!=null?Integer.toHexString(System.identityHashCode(getAvatarBrain())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "mission = "+(getMission()!=null?Integer.toHexString(System.identityHashCode(getMission())):"null");
  }
}


