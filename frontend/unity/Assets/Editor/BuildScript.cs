using UnityEditor;
using UnityEngine;
using System.IO;

public class BuildScript
{
    [MenuItem("Build/WebGL")]
    public static void BuildWebGL()
    {
        // Set the scenes to include in the build
        string[] scenes = { "Assets/Scenes/MainScene.unity" };
        
        // Configure build settings
        PlayerSettings.WebGL.template = "MarsExplorer";
        PlayerSettings.SetScriptingBackend(BuildTargetGroup.WebGL, ScriptingImplementation.IL2CPP);
        PlayerSettings.WebGL.compressionFormat = WebGLCompressionFormat.Brotli;
        
        // Build the project
        BuildPipeline.BuildPlayer(
            scenes,
            "WebGLBuild",
            BuildTarget.WebGL,
            BuildOptions.None
        );
        
        Debug.Log("WebGL build completed successfully");
    }
}
