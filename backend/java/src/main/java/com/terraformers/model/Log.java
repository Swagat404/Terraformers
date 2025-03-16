package com.terraformers.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.sql.Date;
import java.sql.Time;

// line 186 "model.ump"
// line 272 "model.ump"
public class Log
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum LogType { Info, Warning, Error }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Log Attributes
  private int logID;
  private Date logDate;
  private Time logTime;
  private LogType logType;
  private String logMessage;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Log(int aLogID, Date aLogDate, Time aLogTime, LogType aLogType, String aLogMessage)
  {
    logID = aLogID;
    logDate = aLogDate;
    logTime = aLogTime;
    logType = aLogType;
    logMessage = aLogMessage;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setLogID(int aLogID)
  {
    boolean wasSet = false;
    logID = aLogID;
    wasSet = true;
    return wasSet;
  }

  public boolean setLogDate(Date aLogDate)
  {
    boolean wasSet = false;
    logDate = aLogDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setLogTime(Time aLogTime)
  {
    boolean wasSet = false;
    logTime = aLogTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setLogType(LogType aLogType)
  {
    boolean wasSet = false;
    logType = aLogType;
    wasSet = true;
    return wasSet;
  }

  public boolean setLogMessage(String aLogMessage)
  {
    boolean wasSet = false;
    logMessage = aLogMessage;
    wasSet = true;
    return wasSet;
  }

  public int getLogID()
  {
    return logID;
  }

  public Date getLogDate()
  {
    return logDate;
  }

  public Time getLogTime()
  {
    return logTime;
  }

  public LogType getLogType()
  {
    return logType;
  }

  public String getLogMessage()
  {
    return logMessage;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "logID" + ":" + getLogID()+ "," +
            "logMessage" + ":" + getLogMessage()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "logDate" + "=" + (getLogDate() != null ? !getLogDate().equals(this)  ? getLogDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "logTime" + "=" + (getLogTime() != null ? !getLogTime().equals(this)  ? getLogTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "logType" + "=" + (getLogType() != null ? !getLogType().equals(this)  ? getLogType().toString().replaceAll("  ","    ") : "this" : "null");
  }
}


