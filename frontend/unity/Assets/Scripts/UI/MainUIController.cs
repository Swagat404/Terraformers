using UnityEngine;
using UnityEngine.UI;

public class MainUIController : MonoBehaviour
{
    [Tooltip("Reference to the terrain rotator script")]
    public TerrainRotator terrainRotator;
    
    [Tooltip("UI toggle for rotation")]
    public Toggle rotationToggle;
    
    [Tooltip("UI slider for rotation speed")]
    public Slider rotationSpeedSlider;
    
    [Tooltip("Information panel gameobject")]
    public GameObject infoPanel;
    
    void Start()
    {
        // Initialize UI elements
        if (terrainRotator != null && rotationToggle != null)
        {
            rotationToggle.isOn = terrainRotator.autoRotate;
            rotationToggle.onValueChanged.AddListener(OnRotationToggleChanged);
        }
        
        if (terrainRotator != null && rotationSpeedSlider != null)
        {
            rotationSpeedSlider.value = terrainRotator.rotationSpeed;
            rotationSpeedSlider.onValueChanged.AddListener(OnRotationSpeedChanged);
        }
        
        // Hide info panel at start
        if (infoPanel != null)
        {
            infoPanel.SetActive(false);
        }
    }
    
    public void OnRotationToggleChanged(bool value)
    {
        if (terrainRotator != null)
        {
            terrainRotator.autoRotate = value;
        }
    }
    
    public void OnRotationSpeedChanged(float value)
    {
        if (terrainRotator != null)
        {
            terrainRotator.rotationSpeed = value;
        }
    }
    
    public void ToggleInfoPanel()
    {
        if (infoPanel != null)
        {
            infoPanel.SetActive(!infoPanel.activeSelf);
        }
    }
}
