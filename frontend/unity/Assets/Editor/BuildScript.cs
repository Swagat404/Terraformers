using UnityEditor;
using System;
using System.IO;
using System.Linq;

public class BuildScript
{
    public static void BuildWebGL()
    {
        try
        {
            // Set the build target to WebGL
            EditorUserBuildSettings.SwitchActiveBuildTarget(BuildTargetGroup.WebGL, BuildTarget.WebGL);
            
            // Configure WebGL settings
            PlayerSettings.WebGL.compressionFormat = WebGLCompressionFormat.Gzip;
            
            // Create the build directory if it doesn't exist
            string buildPath = Path.Combine("Builds", "WebGL");
            Directory.CreateDirectory(buildPath);
            
            // Default to the first scene if no scenes are set up
            string[] scenes = EditorBuildSettings.scenes.Length > 0 
                ? EditorBuildSettings.scenes.Select(s => s.path).ToArray()
                : new[] { "Assets/Scenes/MainScene.unity" };
            
            // Build the player
            BuildPipeline.BuildPlayer(
                scenes,
                buildPath,
                BuildTarget.WebGL,
                BuildOptions.None
            );
            
            Console.WriteLine("BUILD SUCCESSFUL");
        }
        catch (Exception e)
        {
            Console.WriteLine($"BUILD FAILED: {e.Message}");
            EditorApplication.Exit(1);
        }
    }
}
