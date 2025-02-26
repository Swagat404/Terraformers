using UnityEngine;

public class TerrainRotator : MonoBehaviour
{
    public float rotationSpeed = 5f;

    void Update()
    {
        transform.Rotate(Vector3.up, rotationSpeed * Time.deltaTime);
    }
}
