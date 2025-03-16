package com.terraformers.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.time.LocalDateTime;
import java.util.*;

// line 145 "model.ump"
// line 250 "model.ump"
public class Motor
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum MotorPosition { Front, Back, Left, Right }
  public enum MotorStatus { Active, Inactive, Faulty }
  public enum MotorType { TypeX, TypeY }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Motor Attributes
  private int motorID;
  private float maxSpeed;
  private float powerConsumption;
  private MotorPosition position;
  private MotorStatus status;
  private MotorType motorType;

  //Motor Associations
  private List<MotorReading> motorReadings;
  private AvatarBrain avatarBrain;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Motor(int aMotorID, float aMaxSpeed, float aPowerConsumption, MotorPosition aPosition, MotorStatus aStatus, MotorType aMotorType, AvatarBrain aAvatarBrain)
  {
    motorID = aMotorID;
    maxSpeed = aMaxSpeed;
    powerConsumption = aPowerConsumption;
    position = aPosition;
    status = aStatus;
    motorType = aMotorType;
    motorReadings = new ArrayList<MotorReading>();
    boolean didAddAvatarBrain = setAvatarBrain(aAvatarBrain);
    if (!didAddAvatarBrain)
    {
      throw new RuntimeException("Unable to create motor due to avatarBrain. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setMotorID(int aMotorID)
  {
    boolean wasSet = false;
    motorID = aMotorID;
    wasSet = true;
    return wasSet;
  }

  public boolean setMaxSpeed(float aMaxSpeed)
  {
    boolean wasSet = false;
    maxSpeed = aMaxSpeed;
    wasSet = true;
    return wasSet;
  }

  public boolean setPowerConsumption(float aPowerConsumption)
  {
    boolean wasSet = false;
    powerConsumption = aPowerConsumption;
    wasSet = true;
    return wasSet;
  }

  public boolean setPosition(MotorPosition aPosition)
  {
    boolean wasSet = false;
    position = aPosition;
    wasSet = true;
    return wasSet;
  }

  public boolean setStatus(MotorStatus aStatus)
  {
    boolean wasSet = false;
    status = aStatus;
    wasSet = true;
    return wasSet;
  }

  public boolean setMotorType(MotorType aMotorType)
  {
    boolean wasSet = false;
    motorType = aMotorType;
    wasSet = true;
    return wasSet;
  }

  public int getMotorID()
  {
    return motorID;
  }

  public float getMaxSpeed()
  {
    return maxSpeed;
  }

  public float getPowerConsumption()
  {
    return powerConsumption;
  }

  public MotorPosition getPosition()
  {
    return position;
  }

  public MotorStatus getStatus()
  {
    return status;
  }

  public MotorType getMotorType()
  {
    return motorType;
  }
  /* Code from template association_GetMany */
  public MotorReading getMotorReading(int index)
  {
    MotorReading aMotorReading = motorReadings.get(index);
    return aMotorReading;
  }

  /**
   * Association: Each Motor belongs to one AvatarBrain.
   * Association: One Motor has many MotorReadings.
   */
  public List<MotorReading> getMotorReadings()
  {
    List<MotorReading> newMotorReadings = Collections.unmodifiableList(motorReadings);
    return newMotorReadings;
  }

  public int numberOfMotorReadings()
  {
    int number = motorReadings.size();
    return number;
  }

  public boolean hasMotorReadings()
  {
    boolean has = motorReadings.size() > 0;
    return has;
  }

  public int indexOfMotorReading(MotorReading aMotorReading)
  {
    int index = motorReadings.indexOf(aMotorReading);
    return index;
  }
  /* Code from template association_GetOne */
  public AvatarBrain getAvatarBrain()
  {
    return avatarBrain;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfMotorReadings()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public MotorReading addMotorReading(int aReadingID, LocalDateTime aTimeStamp, float aCurrentSpeed, String aDirection, float aCurrentPower)
  {
    return new MotorReading(aReadingID, aTimeStamp, aCurrentSpeed, aDirection, aCurrentPower, this);
  }

  public boolean addMotorReading(MotorReading aMotorReading)
  {
    boolean wasAdded = false;
    if (motorReadings.contains(aMotorReading)) { return false; }
    Motor existingMotor = aMotorReading.getMotor();
    boolean isNewMotor = existingMotor != null && !this.equals(existingMotor);
    if (isNewMotor)
    {
      aMotorReading.setMotor(this);
    }
    else
    {
      motorReadings.add(aMotorReading);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeMotorReading(MotorReading aMotorReading)
  {
    boolean wasRemoved = false;
    //Unable to remove aMotorReading, as it must always have a motor
    if (!this.equals(aMotorReading.getMotor()))
    {
      motorReadings.remove(aMotorReading);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addMotorReadingAt(MotorReading aMotorReading, int index)
  {  
    boolean wasAdded = false;
    if(addMotorReading(aMotorReading))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfMotorReadings()) { index = numberOfMotorReadings() - 1; }
      motorReadings.remove(aMotorReading);
      motorReadings.add(index, aMotorReading);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveMotorReadingAt(MotorReading aMotorReading, int index)
  {
    boolean wasAdded = false;
    if(motorReadings.contains(aMotorReading))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfMotorReadings()) { index = numberOfMotorReadings() - 1; }
      motorReadings.remove(aMotorReading);
      motorReadings.add(index, aMotorReading);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addMotorReadingAt(aMotorReading, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToMany */
  public boolean setAvatarBrain(AvatarBrain aAvatarBrain)
  {
    boolean wasSet = false;
    if (aAvatarBrain == null)
    {
      return wasSet;
    }

    AvatarBrain existingAvatarBrain = avatarBrain;
    avatarBrain = aAvatarBrain;
    if (existingAvatarBrain != null && !existingAvatarBrain.equals(aAvatarBrain))
    {
      existingAvatarBrain.removeMotor(this);
    }
    avatarBrain.addMotor(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    for(int i=motorReadings.size(); i > 0; i--)
    {
      MotorReading aMotorReading = motorReadings.get(i - 1);
      aMotorReading.delete();
    }
    AvatarBrain placeholderAvatarBrain = avatarBrain;
    this.avatarBrain = null;
    if(placeholderAvatarBrain != null)
    {
      placeholderAvatarBrain.removeMotor(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "motorID" + ":" + getMotorID()+ "," +
            "maxSpeed" + ":" + getMaxSpeed()+ "," +
            "powerConsumption" + ":" + getPowerConsumption()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "position" + "=" + (getPosition() != null ? !getPosition().equals(this)  ? getPosition().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "status" + "=" + (getStatus() != null ? !getStatus().equals(this)  ? getStatus().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "motorType" + "=" + (getMotorType() != null ? !getMotorType().equals(this)  ? getMotorType().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "avatarBrain = "+(getAvatarBrain()!=null?Integer.toHexString(System.identityHashCode(getAvatarBrain())):"null");
  }
}


