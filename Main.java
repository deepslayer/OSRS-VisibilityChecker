import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;

import java.awt.*;

@ScriptManifest(category = Category.MISC, name = "Visibility Script", author = "Deep Slayer", version = 1.0)
public class Main extends AbstractScript {

    private VisibilityChecker visibilityChecker;
    private PlayerVisibilityState visibilityState = PlayerVisibilityState.FULLY_VISIBLE;

    @Override
    public void onStart() {
        visibilityChecker = new VisibilityChecker();
    }

    @Override
    public int onLoop() {
        visibilityState = visibilityChecker.getPlayerVisibilityState();
        switch (visibilityState) {
            case FULLY_VISIBLE:
                log("Player is fully visible.");
                break;
            case LESS_THAN_HALF_OBSTRUCTED:
                log("Player is less than half obstructed.");
                break;
            case MORE_THAN_HALF_OBSTRUCTED:
                log("Player is more than half obstructed.");
                break;
            case FULLY_OBSTRUCTED:
                log("Player is fully obstructed.");
                break;
        }
        return 500;
    }

    @Override
    public void onPaint(Graphics2D graphics) {
        visibilityChecker.drawVisibilityState(graphics);
        switch (visibilityState) {
            case FULLY_VISIBLE:
                graphics.setColor(Color.GREEN);
                graphics.drawString("Fully Visible", 10, 50);
                break;
            case LESS_THAN_HALF_OBSTRUCTED:
                graphics.setColor(Color.YELLOW);
                graphics.drawString("Less Than Half Obstructed", 10, 50);
                break;
            case MORE_THAN_HALF_OBSTRUCTED:
                graphics.setColor(Color.ORANGE);
                graphics.drawString("More Than Half Obstructed", 10, 50);
                break;
            case FULLY_OBSTRUCTED:
                graphics.setColor(Color.RED);
                graphics.drawString("Fully Obstructed", 10, 50);
                break;
        }
    }
}
