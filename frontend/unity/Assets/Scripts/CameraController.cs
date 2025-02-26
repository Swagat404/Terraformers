using UnityEngine;

public class CameraController : MonoBehaviour
{
    public float orbitSpeed = 10f;
    public float zoomSpeed = 500f;
    public float minZoom = 100f;
    public float maxZoom = 500f;

    private Transform target;

    void Start()
    {
        target = GameObject.FindGameObjectWithTag("MarsCenter").transform;
    }

    void Update()
    {
        transform.RotateAround(target.position, Vector3.up, orbitSpeed * Time.deltaTime);

        float scroll = Input.GetAxis("Mouse ScrollWheel");
        Vector3 pos = transform.position;
        pos += transform.forward * scroll * zoomSpeed * Time.deltaTime;
        pos = Vector3.ClampMagnitude(pos, maxZoom);
        pos = Vector3.ClampMagnitude(pos, Mathf.Max(minZoom, pos.magnitude));
        transform.position = pos;

        transform.LookAt(target);
    }
}
