using UnityEngine;

public class TerrainLoader : MonoBehaviour
{
    [Tooltip("Reference to the Mars terrain object")]
    public GameObject marsTerrainObject;
    
    [Tooltip("Scale factor for the terrain")]
    public Vector3 terrainScale = new Vector3(1f, 0.5f, 1f); // Exaggerate height by default
    
    void Start()
    {
        // Apply scaling to the terrain
        if (marsTerrainObject != null)
        {
            marsTerrainObject.transform.localScale = terrainScale;
        }
        else
        {
            Debug.LogError("Mars terrain object not assigned!");
        }
        
        // Additional initialization for the terrain
        InitializeTerrain();
    }
    
    private void InitializeTerrain()
    {
        // This could include additional setup like:
        // - Setting initial rotation
        // - Applying materials
        // - Optimizing mesh if needed
        
        Debug.Log("Mars terrain initialized successfully");
    }
}
