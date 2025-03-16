package com.terraformers.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.util.*;

// line 132 "model.ump"
// line 244 "model.ump"
public class AvatarBrain
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum AvatarBrainType { TypeA, TypeB }
  public enum MotorPosition { Front, Back, Left, Right }
  public enum MotorStatus { Active, Inactive, Faulty }
  public enum MotorType { TypeX, TypeY }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //AvatarBrain Attributes
  private int avatarBrainID;
  private AvatarBrainType avatarBrainType;
  private int avatarSpeed;
  private int avatarMaxJumpHeight;
  private float avatarLength;
  private float avatarWidth;
  private float avatarHeight;

  //AvatarBrain Associations
  private List<Motor> motors;
  private Avatar avatar;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public AvatarBrain(int aAvatarBrainID, AvatarBrainType aAvatarBrainType, int aAvatarSpeed, int aAvatarMaxJumpHeight, float aAvatarLength, float aAvatarWidth, float aAvatarHeight)
  {
    avatarBrainID = aAvatarBrainID;
    avatarBrainType = aAvatarBrainType;
    avatarSpeed = aAvatarSpeed;
    avatarMaxJumpHeight = aAvatarMaxJumpHeight;
    avatarLength = aAvatarLength;
    avatarWidth = aAvatarWidth;
    avatarHeight = aAvatarHeight;
    motors = new ArrayList<Motor>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setAvatarBrainID(int aAvatarBrainID)
  {
    boolean wasSet = false;
    avatarBrainID = aAvatarBrainID;
    wasSet = true;
    return wasSet;
  }

  public boolean setAvatarBrainType(AvatarBrainType aAvatarBrainType)
  {
    boolean wasSet = false;
    avatarBrainType = aAvatarBrainType;
    wasSet = true;
    return wasSet;
  }

  public boolean setAvatarSpeed(int aAvatarSpeed)
  {
    boolean wasSet = false;
    avatarSpeed = aAvatarSpeed;
    wasSet = true;
    return wasSet;
  }

  public boolean setAvatarMaxJumpHeight(int aAvatarMaxJumpHeight)
  {
    boolean wasSet = false;
    avatarMaxJumpHeight = aAvatarMaxJumpHeight;
    wasSet = true;
    return wasSet;
  }

  public boolean setAvatarLength(float aAvatarLength)
  {
    boolean wasSet = false;
    avatarLength = aAvatarLength;
    wasSet = true;
    return wasSet;
  }

  public boolean setAvatarWidth(float aAvatarWidth)
  {
    boolean wasSet = false;
    avatarWidth = aAvatarWidth;
    wasSet = true;
    return wasSet;
  }

  public boolean setAvatarHeight(float aAvatarHeight)
  {
    boolean wasSet = false;
    avatarHeight = aAvatarHeight;
    wasSet = true;
    return wasSet;
  }

  public int getAvatarBrainID()
  {
    return avatarBrainID;
  }

  public AvatarBrainType getAvatarBrainType()
  {
    return avatarBrainType;
  }

  public int getAvatarSpeed()
  {
    return avatarSpeed;
  }

  public int getAvatarMaxJumpHeight()
  {
    return avatarMaxJumpHeight;
  }

  public float getAvatarLength()
  {
    return avatarLength;
  }

  public float getAvatarWidth()
  {
    return avatarWidth;
  }

  public float getAvatarHeight()
  {
    return avatarHeight;
  }
  /* Code from template association_GetMany */
  public Motor getMotor(int index)
  {
    Motor aMotor = motors.get(index);
    return aMotor;
  }

  /**
   * Association: One AvatarBrain controls many Motors.
   */
  public List<Motor> getMotors()
  {
    List<Motor> newMotors = Collections.unmodifiableList(motors);
    return newMotors;
  }

  public int numberOfMotors()
  {
    int number = motors.size();
    return number;
  }

  public boolean hasMotors()
  {
    boolean has = motors.size() > 0;
    return has;
  }

  public int indexOfMotor(Motor aMotor)
  {
    int index = motors.indexOf(aMotor);
    return index;
  }
  /* Code from template association_GetOne */
  public Avatar getAvatar()
  {
    return avatar;
  }

  public boolean hasAvatar()
  {
    boolean has = avatar != null;
    return has;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfMotors()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Motor addMotor(int aMotorID, float aMaxSpeed, float aPowerConsumption, Motor.MotorPosition aPosition, Motor.MotorStatus aStatus, Motor.MotorType aMotorType)
  {
    return new Motor(aMotorID, aMaxSpeed, aPowerConsumption, aPosition, aStatus, aMotorType, this);
  }

  public boolean addMotor(Motor aMotor)
  {
    boolean wasAdded = false;
    if (motors.contains(aMotor)) { return false; }
    AvatarBrain existingAvatarBrain = aMotor.getAvatarBrain();
    boolean isNewAvatarBrain = existingAvatarBrain != null && !this.equals(existingAvatarBrain);
    if (isNewAvatarBrain)
    {
      aMotor.setAvatarBrain(this);
    }
    else
    {
      motors.add(aMotor);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeMotor(Motor aMotor)
  {
    boolean wasRemoved = false;
    //Unable to remove aMotor, as it must always have a avatarBrain
    if (!this.equals(aMotor.getAvatarBrain()))
    {
      motors.remove(aMotor);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addMotorAt(Motor aMotor, int index)
  {  
    boolean wasAdded = false;
    if(addMotor(aMotor))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfMotors()) { index = numberOfMotors() - 1; }
      motors.remove(aMotor);
      motors.add(index, aMotor);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveMotorAt(Motor aMotor, int index)
  {
    boolean wasAdded = false;
    if(motors.contains(aMotor))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfMotors()) { index = numberOfMotors() - 1; }
      motors.remove(aMotor);
      motors.add(index, aMotor);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addMotorAt(aMotor, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOptionalOneToOne */
  public boolean setAvatar(Avatar aNewAvatar)
  {
    boolean wasSet = false;
    if (avatar != null && !avatar.equals(aNewAvatar) && equals(avatar.getAvatarBrain()))
    {
      //Unable to setAvatar, as existing avatar would become an orphan
      return wasSet;
    }

    avatar = aNewAvatar;
    AvatarBrain anOldAvatarBrain = aNewAvatar != null ? aNewAvatar.getAvatarBrain() : null;

    if (!this.equals(anOldAvatarBrain))
    {
      if (anOldAvatarBrain != null)
      {
        anOldAvatarBrain.avatar = null;
      }
      if (avatar != null)
      {
        avatar.setAvatarBrain(this);
      }
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    for(int i=motors.size(); i > 0; i--)
    {
      Motor aMotor = motors.get(i - 1);
      aMotor.delete();
    }
    Avatar existingAvatar = avatar;
    avatar = null;
    if (existingAvatar != null)
    {
      existingAvatar.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "avatarBrainID" + ":" + getAvatarBrainID()+ "," +
            "avatarSpeed" + ":" + getAvatarSpeed()+ "," +
            "avatarMaxJumpHeight" + ":" + getAvatarMaxJumpHeight()+ "," +
            "avatarLength" + ":" + getAvatarLength()+ "," +
            "avatarWidth" + ":" + getAvatarWidth()+ "," +
            "avatarHeight" + ":" + getAvatarHeight()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "avatarBrainType" + "=" + (getAvatarBrainType() != null ? !getAvatarBrainType().equals(this)  ? getAvatarBrainType().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "avatar = "+(getAvatar()!=null?Integer.toHexString(System.identityHashCode(getAvatar())):"null");
  }
}


