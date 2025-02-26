using UnityEngine;

namespace MarsSimulator.Core
{
    public class Startup : MonoBehaviour
    {
        [SerializeField] private GameObject mainCanvas;
        
        void Start()
        {
            Debug.Log("Mars AI Simulator starting up...");
            
            // Ensure the main canvas is active
            if (mainCanvas != null)
            {
                mainCanvas.SetActive(true);
                Debug.Log("Main UI canvas activated");
            }
            else
            {
                Debug.LogError("Main canvas reference not set!");
            }
        }
    }
}
