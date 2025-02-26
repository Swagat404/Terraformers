using UnityEngine;

public class TerrainRotator : MonoBehaviour
{
    [Tooltip("Rotation speed in degrees per second")]
    public float rotationSpeed = 5.0f;
    
    [Tooltip("Rotation axis (0,1,0 for Y-axis rotation)")]
    public Vector3 rotationAxis = Vector3.up;
    
    [Tooltip("Enable/disable automatic rotation")]
    public bool autoRotate = true;
    
    void Update()
    {
        if (autoRotate)
        {
            // Rotate the terrain around the specified axis
            transform.Rotate(rotationAxis, rotationSpeed * Time.deltaTime);
        }
    }
    
    // Public method to toggle rotation on/off
    public void ToggleRotation()
    {
        autoRotate = !autoRotate;
    }
    
    // Public method to set rotation speed
    public void SetRotationSpeed(float speed)
    {
        rotationSpeed = speed;
    }
}
