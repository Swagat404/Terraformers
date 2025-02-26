using UnityEngine;

public class CameraController : MonoBehaviour
{
    [Tooltip("Target to orbit around")]
    public Transform target;
    
    [Tooltip("Orbit speed when dragging")]
    public float orbitSpeed = 10.0f;
    
    [Tooltip("Zoom speed when scrolling")]
    public float zoomSpeed = 2.0f;
    
    [Tooltip("Minimum zoom distance")]
    public float minZoomDistance = 2.0f;
    
    [Tooltip("Maximum zoom distance")]
    public float maxZoomDistance = 20.0f;
    
    private float currentDistance;
    private Vector3 previousPosition;
    
    void Start()
    {
        if (target == null)
        {
            Debug.LogError("Please assign a target for the camera to orbit around.");
            return;
        }
        
        // Initialize distance
        currentDistance = Vector3.Distance(transform.position, target.position);
    }
    
    void Update()
    {
        // Handle zooming with scroll wheel
        float scrollInput = Input.GetAxis("Mouse ScrollWheel");
        if (scrollInput != 0)
        {
            currentDistance -= scrollInput * zoomSpeed;
            currentDistance = Mathf.Clamp(currentDistance, minZoomDistance, maxZoomDistance);
        }
        
        // Handle rotation with mouse drag
        if (Input.GetMouseButtonDown(0))
        {
            previousPosition = Input.mousePosition;
        }
        
        if (Input.GetMouseButton(0))
        {
            Vector3 delta = Input.mousePosition - previousPosition;
            
            // Orbit around the target
            transform.RotateAround(target.position, Vector3.up, delta.x * orbitSpeed * Time.deltaTime);
            transform.RotateAround(target.position, transform.right, -delta.y * orbitSpeed * Time.deltaTime);
            
            // Update camera distance to maintain zoom
            Vector3 direction = (transform.position - target.position).normalized;
            transform.position = target.position + direction * currentDistance;
            
            previousPosition = Input.mousePosition;
        }
    }
}

