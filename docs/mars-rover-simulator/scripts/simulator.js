// Global variables
let unityInstance = null;
let missionSeconds = 0;
let currentVelocity = 0;
let currentDistance = 0;
let selectedAvatarType = null;

// Initialize loading sequence
document.addEventListener('DOMContentLoaded', function() {
    startLoadingSequence();
    setupAvatarSelection();
    setupKeyboardControls();
});

// Simulate loading progress
function startLoadingSequence() {
    let progress = 0;
    const loadingBar = document.getElementById('loading-bar');
    const loadingText = document.getElementById('loading-text');
    const loadingScreen = document.getElementById('loading-screen');
    const avatarSelection = document.getElementById('avatar-selection');

    const loadingInterval = setInterval(() => {
        progress += Math.random() * 5;
        if (progress >= 100) {
            progress = 100;
            clearInterval(loadingInterval);
            setTimeout(() => {
                loadingScreen.style.display = 'none';
                avatarSelection.style.display = 'flex';
            }, 1000);
        }
        loadingBar.style.width = `${progress}%`;
        loadingText.innerText = `Loading: ${Math.floor(progress)}%`;
    }, 200);
}

// Setup avatar selection screen
function setupAvatarSelection() {
    const avatarOptions = document.querySelectorAll('.avatar-option');
    avatarOptions.forEach(option => {
        option.addEventListener('click', () => {
            selectedAvatarType = option.getAttribute('data-avatar');
            console.log(`Selected avatar: ${selectedAvatarType}`);
            
            // Hide avatar selection and show simulator
            document.getElementById('avatar-selection').style.display = 'none';
            document.getElementById('simulator-container').style.display = 'block';
            
            // Initialize Unity WebGL
            initUnity(selectedAvatarType);
        });
    });
}

// Initialize Unity WebGL
function initUnity(avatarType) {
    console.log(`Initializing Unity WebGL with avatar: ${avatarType}`);
    
    // Unity loader configuration - ensure the Build path matches your deployment
    unityInstance = UnityLoader.instantiate("unity-container", "Build/MarsRoverSimulator.json", {
        onProgress: function (instance, progress) {
            console.log(`Unity loading progress: ${progress * 100}%`);
        },
        Module: {
            onRuntimeInitialized: function() {
                // Unity runtime is initialized
                console.log("Unity runtime initialized");
                
                // Start simulator systems
                updateDashboard();
                startCompassUpdates();
                
                // Add initial log entries
                addLogEntry("Simulation initialized successfully");
                addLogEntry("Rover systems online");
                
                // Schedule some additional logs for immersion
                setTimeout(() => addLogEntry("Terrain mapping in progress..."), 3000);
                setTimeout(() => addLogEntry("Detecting geological formations..."), 7000);
                setTimeout(() => addLogEntry("Warning: Obstacle detected ahead."), 12000);
                
                // Tell Unity which avatar was selected
                try {
                    unityInstance.SendMessage("GameController", "SetAvatarType", avatarType);
                    addLogEntry(`${avatarType.toUpperCase()} rover model activated`);
                } catch (e) {
                    console.error("Error sending message to Unity:", e);
                    addLogEntry("ERROR: Failed to initialize rover model");
                }
            }
        }
    });
    
    // Store the Unity instance for later reference
    window.unityInstance = unityInstance;
}

// Update dashboard values
function updateDashboard() {
    // Get dashboard display elements
    const velocityDisplay = document.getElementById('velocity-display');
    const distanceDisplay = document.getElementById('distance-display');
    const temperatureDisplay = document.getElementById('temperature-display');
    const powerDisplay = document.getElementById('power-display');
    const atmosphereDisplay = document.getElementById('atmosphere-display');
    const timeDisplay = document.getElementById('time-display');
    
    setInterval(() => {
        // Update mission time
        missionSeconds++;
        const hours = Math.floor(missionSeconds / 3600).toString().padStart(2, '0');
        const minutes = Math.floor((missionSeconds % 3600) / 60).toString().padStart(2, '0');
        const seconds = (missionSeconds % 60).toString().padStart(2, '0');
        timeDisplay.textContent = `${hours}:${minutes}:${seconds}`;
        
        // If we have data from Unity, it will overwrite these values
        // Otherwise, use simulated values for testing
        if (!window.unityDataReceived) {
            // Simulate velocity changes
            if (Math.random() > 0.9) {
                currentVelocity = Math.random() * 5;
            }
            
            // Update distance based on velocity
            currentDistance += currentVelocity * 0.1;
            
            // Update displays with simulated values
            velocityDisplay.textContent = `${currentVelocity.toFixed(1)} m/s`;
            distanceDisplay.textContent = `${currentDistance.toFixed(1)} m`;
            
            // Random fluctuations for other readings
            const tempValue = -60 + (Math.random() * 5);
            temperatureDisplay.textContent = `${tempValue.toFixed(1)}°C`;
            
            const powerValue = 85 + (Math.random() * 10);
            powerDisplay.textContent = `${Math.floor(powerValue)}%`;
            
            const atmosphereValue = 6 + (Math.random() * 0.3);
            atmosphereDisplay.textContent = `${atmosphereValue.toFixed(1)} mbar`;
        }
    }, 100);
}

// Add log entry to mission logs
function addLogEntry(message) {
    const logs = document.getElementById('mission-logs');
    const entry = document.createElement('div');
    entry.className = 'log-entry';
    
    // Format mission time
    const hours = Math.floor(missionSeconds / 3600).toString().padStart(2, '0');
    const minutes = Math.floor((missionSeconds % 3600) / 60).toString().padStart(2, '0');
    const seconds = (missionSeconds % 60).toString().padStart(2, '0');
    const timeString = `T+${hours}:${minutes}:${seconds}`;
    
    entry.innerHTML = `
        <span class="log-timestamp">${timeString}</span>
        <span class="log-message">${message}</span>
    `;
    
    logs.appendChild(entry);
    logs.scrollTop = logs.scrollHeight;
}

// Update compass direction
function startCompassUpdates() {
    const directions = ['N', 'NE', 'E', 'SE', 'S', 'SW', 'W', 'NW'];
    let currentDirection = 0;
    const compassDisplay = document.getElementById('compass-direction');
    
    setInterval(() => {
        if (Math.random() > 0.9) {
            // Randomly change direction occasionally
            currentDirection = (currentDirection + Math.floor(Math.random() * 3) - 1 + 8) % 8;
            compassDisplay.textContent = directions[currentDirection];
        }
    }, 500);
}

// Setup keyboard controls
function setupKeyboardControls() {
    document.addEventListener('keydown', function(event) {
        if (!unityInstance) return;
        
        // Map keys to commands
        let command = "";
        switch(event.key.toLowerCase()) {
            case "w": command = "MoveForward"; break;
            case "s": command = "MoveBackward"; break;
            case "a": command = "TurnLeft"; break;
            case "d": command = "TurnRight"; break;
            case "shift": command = "StartRunning"; break;
            case "f": command = "ToggleScanner"; break;
            case " ": command = "EmergencyStop"; break;
        }
        
        // Send command to Unity if valid
        if (command) {
            try {
                unityInstance.SendMessage("GameController", "ReceiveCommand", command);
                console.log(`Sent command to Unity: ${command}`);
            } catch (e) {
                console.error("Error sending command to Unity:", e);
            }
        }
    });
    
    // Handle key up events for run mode
    document.addEventListener('keyup', function(event) {
        if (!unityInstance) return;
        
        if (event.key.toLowerCase() === "shift") {
            try {
                unityInstance.SendMessage("GameController", "ReceiveCommand", "StopRunning");
            } catch (e) {
                console.error("Error sending command to Unity:", e);
            }
        }
    });
}

// Receive data from Unity
function receiveDataFromUnity(data) {
    window.unityDataReceived = true;
    console.log("Received from Unity:", data);
    
    // Parse the data (assuming JSON format)
    try {
        const parsedData = JSON.parse(data);
        
        // Update dashboard based on data from Unity
        if (parsedData.velocity !== undefined) {
            currentVelocity = parsedData.velocity;
            document.getElementById('velocity-display').textContent = `${currentVelocity.toFixed(1)} m/s`;
        }
        
        if (parsedData.distance !== undefined) {
            currentDistance = parsedData.distance;
            document.getElementById('distance-display').textContent = `${currentDistance.toFixed(1)} m`;
        }
        
        // Update compass if rotation data is available
        if (parsedData.rotation !== undefined) {
            updateCompassFromRotation(parsedData.rotation);
        }
        
        // Handle specific data types
        if (parsedData.temperature !== undefined) {
            document.getElementById('temperature-display').textContent = `${parsedData.temperature.toFixed(1)}°C`;
        }
        
        if (parsedData.power !== undefined) {
            document.getElementById('power-display').textContent = `${Math.floor(parsedData.power)}%`;
        }
        
        if (parsedData.atmosphere !== undefined) {
            document.getElementById('atmosphere-display').textContent = `${parsedData.atmosphere.toFixed(1)} mbar`;
        }
        
        // Handle events
        if (parsedData.event) {
            handleEvent(parsedData.event);
        }
        
        // Add log entries if included
        if (parsedData.log) {
            addLogEntry(parsedData.log);
        }
        
        // Handle status updates
        if (parsedData.status) {
            handleStatusUpdate(parsedData.status);
        }
    } catch (e) {
        console.error("Error parsing data from Unity:", e);
    }
}

// Update compass based on rotation from Unity
function updateCompassFromRotation(rotation) {
    // Normalize rotation to 0-360
    rotation = ((rotation % 360) + 360) % 360;
    
    // Map degrees to compass directions
    const directions = ['N', 'NE', 'E', 'SE', 'S', 'SW', 'W', 'NW'];
    const index = Math.round(rotation / 45) % 8;
    
    document.getElementById('compass-direction').textContent = directions[index];
}

// Handle events from Unity
function handleEvent(event) {
    // Add log entry for the event
    addLogEntry(event.message || "Unknown event occurred");
    
    // Handle different event types
    switch(event.type) {
        case "discovery":
            // Flash the mission log panel
            const logPanel = document.getElementById('mission-logs');
            logPanel.style.backgroundColor = "rgba(255, 90, 0, 0.7)";
            setTimeout(() => {
                logPanel.style.backgroundColor = "rgba(25, 25, 25, 0.7)";
            }, 1000);
            break;
            
        case "danger":
            // Flash warning indicators
            const indicators = document.querySelectorAll('.status-indicator');
            indicators.forEach(indicator => {
                indicator.classList.add('indicator-red');
                setTimeout(() => {
                    indicator.classList.remove('indicator-red');
                }, 2000);
            });
            break;
            
        case "system":
            // Handle system events
            if (event.status === "error") {
                // Update status indicators
                const index = event.system === "power" ? 0 : 
                             event.system === "navigation" ? 1 :
                             event.system === "comms" ? 2 : 3;
                             
                if (index >= 0 && index < 4) {
                    const indicator = document.querySelectorAll('.status-indicator')[index];
                    indicator.className = "status-indicator indicator-red";
                }
            }
            break;
    }
}

// Handle status updates from Unity
function handleStatusUpdate(status) {
    console.log("Status update:", status);
    
    // Handle different status types
    switch(status) {
        case "initialized":
            addLogEntry("Rover systems fully initialized");
            break;
            
        case "error":
            // Update UI to show error state
            addLogEntry("ERROR: System malfunction detected");
            break;
    }
}