

import model.src.AvatarBrain;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AvatarBrainTest {

    @Test
    public void testBasicFunctionality() {
        // Create a simple AvatarBrain instance
        AvatarBrain brain = new AvatarBrain("humanoid", 10, 5, "{\"height\":180,\"width\":50}");
        
        // Test basic getters
        assertEquals("humanoid", brain.getAvatarBrainType());
        assertEquals(10, brain.getMaxSpeed());
        assertEquals(5, brain.getMaxJumpHeight());
        assertEquals("{\"height\":180,\"width\":50}", brain.getDimensions());
        
        // Test setters
        brain.setAvatarBrainID(1);
        brain.setAvatarBrainType("robot");
        
        // Verify updates worked
        assertEquals(1, brain.getAvatarBrainID());
        assertEquals("robot", brain.getAvatarBrainType());
    }
}
