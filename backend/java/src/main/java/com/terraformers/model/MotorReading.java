package com.terraformers.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/



// line 177 "model.ump"
// line 267 "model.ump"

import java.time.LocalDateTime;

public class MotorReading extends Reading
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //MotorReading Attributes
  private float currentSpeed;
  private String direction;
  private float currentPower;

  //MotorReading Associations
  private Motor motor;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public MotorReading(int aReadingID, LocalDateTime aTimeStamp, float aCurrentSpeed, String aDirection, float aCurrentPower, Motor aMotor)
  {
    super(aReadingID, aTimeStamp);
    currentSpeed = aCurrentSpeed;
    direction = aDirection;
    currentPower = aCurrentPower;
    boolean didAddMotor = setMotor(aMotor);
    if (!didAddMotor)
    {
      throw new RuntimeException("Unable to create motorReading due to motor. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setCurrentSpeed(float aCurrentSpeed)
  {
    boolean wasSet = false;
    currentSpeed = aCurrentSpeed;
    wasSet = true;
    return wasSet;
  }

  public boolean setDirection(String aDirection)
  {
    boolean wasSet = false;
    direction = aDirection;
    wasSet = true;
    return wasSet;
  }

  public boolean setCurrentPower(float aCurrentPower)
  {
    boolean wasSet = false;
    currentPower = aCurrentPower;
    wasSet = true;
    return wasSet;
  }

  public float getCurrentSpeed()
  {
    return currentSpeed;
  }

  public String getDirection()
  {
    return direction;
  }

  public float getCurrentPower()
  {
    return currentPower;
  }
  /* Code from template association_GetOne */
  public Motor getMotor()
  {
    return motor;
  }
  /* Code from template association_SetOneToMany */
  public boolean setMotor(Motor aMotor)
  {
    boolean wasSet = false;
    if (aMotor == null)
    {
      return wasSet;
    }

    Motor existingMotor = motor;
    motor = aMotor;
    if (existingMotor != null && !existingMotor.equals(aMotor))
    {
      existingMotor.removeMotorReading(this);
    }
    motor.addMotorReading(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Motor placeholderMotor = motor;
    this.motor = null;
    if(placeholderMotor != null)
    {
      placeholderMotor.removeMotorReading(this);
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "currentSpeed" + ":" + getCurrentSpeed()+ "," +
            "direction" + ":" + getDirection()+ "," +
            "currentPower" + ":" + getCurrentPower()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "motor = "+(getMotor()!=null?Integer.toHexString(System.identityHashCode(getMotor())):"null");
  }
}


