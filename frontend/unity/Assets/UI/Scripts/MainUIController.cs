using UnityEngine;
using UnityEngine.UI;

namespace MarsSimulator.UI
{
    public class MainUIController : MonoBehaviour
    {
        [SerializeField] private Text titleText;
        [SerializeField] private Button connectButton;
        
        void Start()
        {
            if (connectButton != null)
            {
                connectButton.onClick.AddListener(OnConnectClicked);
            }
            
            if (titleText != null)
            {
                titleText.text = "Mars AI Simulator";
            }
        }
        
        private void OnConnectClicked()
        {
            Debug.Log("Connect button clicked - would connect to backend");
            // In a real implementation, this would connect to your backend
        }
    }
}
