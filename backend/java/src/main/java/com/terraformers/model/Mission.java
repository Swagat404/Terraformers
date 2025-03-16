package com.terraformers.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.util.*;


import java.sql.Date;
import java.sql.Time;

// line 93 "model.ump"
// line 217 "model.ump"
public class Mission
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum MissionStatus { Planned, Ongoing, Failed, Completed, Aborted }
  public enum AvatarColor { Red, Blue, Green, Yellow }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Mission Attributes
  private int missionID;
  private String missionName;
  private MissionStatus status;
  private String objective;

  //Mission Associations
  private List<MissionLog> missionLogs;
  private List<Avatar> avatars;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Mission(int aMissionID, String aMissionName, MissionStatus aStatus, String aObjective)
  {
    missionID = aMissionID;
    missionName = aMissionName;
    status = aStatus;
    objective = aObjective;
    missionLogs = new ArrayList<MissionLog>();
    avatars = new ArrayList<Avatar>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setMissionID(int aMissionID)
  {
    boolean wasSet = false;
    missionID = aMissionID;
    wasSet = true;
    return wasSet;
  }

  public boolean setMissionName(String aMissionName)
  {
    boolean wasSet = false;
    missionName = aMissionName;
    wasSet = true;
    return wasSet;
  }

  public boolean setStatus(MissionStatus aStatus)
  {
    boolean wasSet = false;
    status = aStatus;
    wasSet = true;
    return wasSet;
  }

  public boolean setObjective(String aObjective)
  {
    boolean wasSet = false;
    objective = aObjective;
    wasSet = true;
    return wasSet;
  }

  public int getMissionID()
  {
    return missionID;
  }

  public String getMissionName()
  {
    return missionName;
  }

  public MissionStatus getStatus()
  {
    return status;
  }

  public String getObjective()
  {
    return objective;
  }
  /* Code from template association_GetMany */
  public MissionLog getMissionLog(int index)
  {
    MissionLog aMissionLog = missionLogs.get(index);
    return aMissionLog;
  }

  /**
   * Association: One Mission has many MissionLogs.
   */
  public List<MissionLog> getMissionLogs()
  {
    List<MissionLog> newMissionLogs = Collections.unmodifiableList(missionLogs);
    return newMissionLogs;
  }

  public int numberOfMissionLogs()
  {
    int number = missionLogs.size();
    return number;
  }

  public boolean hasMissionLogs()
  {
    boolean has = missionLogs.size() > 0;
    return has;
  }

  public int indexOfMissionLog(MissionLog aMissionLog)
  {
    int index = missionLogs.indexOf(aMissionLog);
    return index;
  }
  /* Code from template association_GetMany */
  public Avatar getAvatar(int index)
  {
    Avatar aAvatar = avatars.get(index);
    return aAvatar;
  }

  public List<Avatar> getAvatars()
  {
    List<Avatar> newAvatars = Collections.unmodifiableList(avatars);
    return newAvatars;
  }

  public int numberOfAvatars()
  {
    int number = avatars.size();
    return number;
  }

  public boolean hasAvatars()
  {
    boolean has = avatars.size() > 0;
    return has;
  }

  public int indexOfAvatar(Avatar aAvatar)
  {
    int index = avatars.indexOf(aAvatar);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfMissionLogs()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public MissionLog addMissionLog(int aLogID, Date aLogDate, Time aLogTime, Log.LogType aLogType, String aLogMessage)
  {
    return new MissionLog(aLogID, aLogDate, aLogTime, aLogType, aLogMessage, this);
  }

  public boolean addMissionLog(MissionLog aMissionLog)
  {
    boolean wasAdded = false;
    if (missionLogs.contains(aMissionLog)) { return false; }
    Mission existingMission = aMissionLog.getMission();
    boolean isNewMission = existingMission != null && !this.equals(existingMission);
    if (isNewMission)
    {
      aMissionLog.setMission(this);
    }
    else
    {
      missionLogs.add(aMissionLog);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeMissionLog(MissionLog aMissionLog)
  {
    boolean wasRemoved = false;
    //Unable to remove aMissionLog, as it must always have a mission
    if (!this.equals(aMissionLog.getMission()))
    {
      missionLogs.remove(aMissionLog);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addMissionLogAt(MissionLog aMissionLog, int index)
  {  
    boolean wasAdded = false;
    if(addMissionLog(aMissionLog))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfMissionLogs()) { index = numberOfMissionLogs() - 1; }
      missionLogs.remove(aMissionLog);
      missionLogs.add(index, aMissionLog);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveMissionLogAt(MissionLog aMissionLog, int index)
  {
    boolean wasAdded = false;
    if(missionLogs.contains(aMissionLog))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfMissionLogs()) { index = numberOfMissionLogs() - 1; }
      missionLogs.remove(aMissionLog);
      missionLogs.add(index, aMissionLog);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addMissionLogAt(aMissionLog, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfAvatars()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Avatar addAvatar(int aAvatarID, String aAvatarName, Avatar.AvatarColor aAvatarColor, Location aLocation, AvatarBrain aAvatarBrain)
  {
    return new Avatar(aAvatarID, aAvatarName, aAvatarColor, aLocation, aAvatarBrain, this);
  }

  public boolean addAvatar(Avatar aAvatar)
  {
    boolean wasAdded = false;
    if (avatars.contains(aAvatar)) { return false; }
    Mission existingMission = aAvatar.getMission();
    boolean isNewMission = existingMission != null && !this.equals(existingMission);
    if (isNewMission)
    {
      aAvatar.setMission(this);
    }
    else
    {
      avatars.add(aAvatar);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeAvatar(Avatar aAvatar)
  {
    boolean wasRemoved = false;
    //Unable to remove aAvatar, as it must always have a mission
    if (!this.equals(aAvatar.getMission()))
    {
      avatars.remove(aAvatar);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addAvatarAt(Avatar aAvatar, int index)
  {  
    boolean wasAdded = false;
    if(addAvatar(aAvatar))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAvatars()) { index = numberOfAvatars() - 1; }
      avatars.remove(aAvatar);
      avatars.add(index, aAvatar);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveAvatarAt(Avatar aAvatar, int index)
  {
    boolean wasAdded = false;
    if(avatars.contains(aAvatar))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAvatars()) { index = numberOfAvatars() - 1; }
      avatars.remove(aAvatar);
      avatars.add(index, aAvatar);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addAvatarAt(aAvatar, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    for(int i=missionLogs.size(); i > 0; i--)
    {
      MissionLog aMissionLog = missionLogs.get(i - 1);
      aMissionLog.delete();
    }
    for(int i=avatars.size(); i > 0; i--)
    {
      Avatar aAvatar = avatars.get(i - 1);
      aAvatar.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "missionID" + ":" + getMissionID()+ "," +
            "missionName" + ":" + getMissionName()+ "," +
            "objective" + ":" + getObjective()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "status" + "=" + (getStatus() != null ? !getStatus().equals(this)  ? getStatus().toString().replaceAll("  ","    ") : "this" : "null");
  }
}


