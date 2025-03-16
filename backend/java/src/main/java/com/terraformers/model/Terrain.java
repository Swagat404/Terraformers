package com.terraformers.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/


import java.util.*;

/**
 * Classes
 */
// line 73 "model.ump"
// line 206 "model.ump"
public class Terrain
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum TerrainType { Hilly, Flat, Rocky }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Terrain Attributes
  private int terrainID;
  private TerrainType terrainType;
  private float surfaceHardness;
  private String description;

  //Terrain Associations
  private List<Location> locations;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Terrain(int aTerrainID, TerrainType aTerrainType, float aSurfaceHardness, String aDescription)
  {
    terrainID = aTerrainID;
    terrainType = aTerrainType;
    surfaceHardness = aSurfaceHardness;
    description = aDescription;
    locations = new ArrayList<Location>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setTerrainID(int aTerrainID)
  {
    boolean wasSet = false;
    terrainID = aTerrainID;
    wasSet = true;
    return wasSet;
  }

  public boolean setTerrainType(TerrainType aTerrainType)
  {
    boolean wasSet = false;
    terrainType = aTerrainType;
    wasSet = true;
    return wasSet;
  }

  public boolean setSurfaceHardness(float aSurfaceHardness)
  {
    boolean wasSet = false;
    surfaceHardness = aSurfaceHardness;
    wasSet = true;
    return wasSet;
  }

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public int getTerrainID()
  {
    return terrainID;
  }

  public TerrainType getTerrainType()
  {
    return terrainType;
  }

  public float getSurfaceHardness()
  {
    return surfaceHardness;
  }

  public String getDescription()
  {
    return description;
  }
  /* Code from template association_GetMany */
  public Location getLocation(int index)
  {
    Location aLocation = locations.get(index);
    return aLocation;
  }

  /**
   * Association: One Terrain is linked with many Locations.
   */
  public List<Location> getLocations()
  {
    List<Location> newLocations = Collections.unmodifiableList(locations);
    return newLocations;
  }

  public int numberOfLocations()
  {
    int number = locations.size();
    return number;
  }

  public boolean hasLocations()
  {
    boolean has = locations.size() > 0;
    return has;
  }

  public int indexOfLocation(Location aLocation)
  {
    int index = locations.indexOf(aLocation);
    return index;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfLocationsValid()
  {
    boolean isValid = numberOfLocations() >= minimumNumberOfLocations();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfLocations()
  {
    return 1;
  }
  /* Code from template association_AddMandatoryManyToOne */
  public Location addLocation(int aLocationID, float aLongitude, float aLatitude, float aAltitude, float aSlope)
  {
    Location aNewLocation = new Location(aLocationID, aLongitude, aLatitude, aAltitude, aSlope, this);
    return aNewLocation;
  }

  public boolean addLocation(Location aLocation)
  {
    boolean wasAdded = false;
    if (locations.contains(aLocation)) { return false; }
    Terrain existingTerrain = aLocation.getTerrain();
    boolean isNewTerrain = existingTerrain != null && !this.equals(existingTerrain);

    if (isNewTerrain && existingTerrain.numberOfLocations() <= minimumNumberOfLocations())
    {
      return wasAdded;
    }
    if (isNewTerrain)
    {
      aLocation.setTerrain(this);
    }
    else
    {
      locations.add(aLocation);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeLocation(Location aLocation)
  {
    boolean wasRemoved = false;
    //Unable to remove aLocation, as it must always have a terrain
    if (this.equals(aLocation.getTerrain()))
    {
      return wasRemoved;
    }

    //terrain already at minimum (1)
    if (numberOfLocations() <= minimumNumberOfLocations())
    {
      return wasRemoved;
    }

    locations.remove(aLocation);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addLocationAt(Location aLocation, int index)
  {  
    boolean wasAdded = false;
    if(addLocation(aLocation))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLocations()) { index = numberOfLocations() - 1; }
      locations.remove(aLocation);
      locations.add(index, aLocation);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveLocationAt(Location aLocation, int index)
  {
    boolean wasAdded = false;
    if(locations.contains(aLocation))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLocations()) { index = numberOfLocations() - 1; }
      locations.remove(aLocation);
      locations.add(index, aLocation);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addLocationAt(aLocation, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    for(int i=locations.size(); i > 0; i--)
    {
      Location aLocation = locations.get(i - 1);
      aLocation.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "terrainID" + ":" + getTerrainID()+ "," +
            "surfaceHardness" + ":" + getSurfaceHardness()+ "," +
            "description" + ":" + getDescription()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "terrainType" + "=" + (getTerrainType() != null ? !getTerrainType().equals(this)  ? getTerrainType().toString().replaceAll("  ","    ") : "this" : "null");
  }
}


