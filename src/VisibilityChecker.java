import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Model;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.interactive.FloorDecoration;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VisibilityChecker {
    private static final Set<String> FLOOR_DECORATION_NAMES = new HashSet<>();

    public PlayerVisibilityState getPlayerVisibilityState() {
        Player player = Players.getLocal();
        Model playerModel = player.getModel();
        if (playerModel != null) {
            Shape playerHull = playerModel.getHullBounds();
            if (playerHull != null) {
                List<NPC> nearbyNpcs = NPCs.all(npc -> npc.distance(player) < 8);
                List<GameObject> nearbyObjects = GameObjects.all(obj -> obj.distance(player) < 8 && !isFloorDecoration(obj));

                return checkVisibilityPriority(playerHull, nearbyNpcs, nearbyObjects);
            }
        }
        return PlayerVisibilityState.FULLY_VISIBLE; // Default to fully visible if no player model or hull
    }

    public void drawVisibilityState(Graphics2D graphics) {
        Player player = Players.getLocal();
        Model playerModel = player.getModel();
        if (playerModel != null) {
            Shape playerHull = playerModel.getHullBounds();
            if (playerHull != null) {
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setColor(Color.CYAN);
                graphics.setStroke(new BasicStroke(1.f));
                graphics.draw(playerHull);

                List<NPC> nearbyNpcs = NPCs.all(npc -> npc.distance(player) < 10);
                List<GameObject> nearbyObjects = GameObjects.all(obj -> obj.distance(player) < 10 && !isFloorDecoration(obj));

                checkVisibilityPriorityAndDraw(graphics, playerHull, nearbyNpcs, nearbyObjects);
            }
        }
    }

    private PlayerVisibilityState checkVisibilityPriority(Shape playerHull, List<NPC> npcs, List<GameObject> gameObjects) {
        double totalPlayerArea = getArea(playerHull);
        Area combinedObstructionArea = new Area();

        // Check NPCs
        for (NPC npc : npcs) {
            Model npcModel = npc.getModel();
            if (npcModel != null) {
                Shape npcHull = npcModel.getHullBounds();
                if (npcHull != null && playerHull.intersects(npcHull.getBounds2D()) && getMaxY(npcHull) > getMaxY(playerHull)) {
                    combinedObstructionArea.add(new Area(npcHull));
                }
            }
        }

        // Check GameObjects
        for (GameObject gameObject : gameObjects) {
            if (isFloorDecoration(gameObject)) {
                continue; // Skip floor decorations
            }

            Model objectModel = gameObject.getModel();
            if (objectModel != null) {
                Shape objectHull = objectModel.getHullBounds();
                if (objectHull != null && playerHull.intersects(objectHull.getBounds2D()) && getMaxY(objectHull) > getMaxY(playerHull)) {
                    combinedObstructionArea.add(new Area(objectHull));
                }
            }
        }

        Area playerArea = new Area(playerHull);
        playerArea.intersect(combinedObstructionArea);
        double coveredArea = getArea(playerArea);

        double coverageRatio = coveredArea / totalPlayerArea;

        if (coverageRatio == 1.0) {
            return PlayerVisibilityState.FULLY_OBSTRUCTED;
        } else if (coverageRatio > 0.5) {
            return PlayerVisibilityState.MORE_THAN_HALF_OBSTRUCTED;
        } else if (coverageRatio > 0) {
            return PlayerVisibilityState.LESS_THAN_HALF_OBSTRUCTED;
        } else {
            return PlayerVisibilityState.FULLY_VISIBLE;
        }
    }

    private void checkVisibilityPriorityAndDraw(Graphics2D graphics, Shape playerHull, List<NPC> npcs, List<GameObject> gameObjects) {
        Area combinedObstructionArea = new Area();

        // Check NPCs
        for (NPC npc : npcs) {
            Model npcModel = npc.getModel();
            if (npcModel != null) {
                Shape npcHull = npcModel.getHullBounds();
                if (npcHull != null && playerHull.intersects(npcHull.getBounds2D()) && getMaxY(npcHull) > getMaxY(playerHull)) {
                    graphics.setColor(Color.RED);
                    graphics.draw(npcHull);
                    combinedObstructionArea.add(new Area(npcHull));
                }
            }
        }

        // Check GameObjects
        for (GameObject gameObject : gameObjects) {
            if (isFloorDecoration(gameObject)) {
                continue; // Skip floor decorations
            }

            Model objectModel = gameObject.getModel();
            if (objectModel != null) {
                Shape objectHull = objectModel.getHullBounds();
                if (objectHull != null && playerHull.intersects(objectHull.getBounds2D()) && getMaxY(objectHull) > getMaxY(playerHull)) {
                    graphics.setColor(Color.RED);
                    graphics.draw(objectHull);
                    combinedObstructionArea.add(new Area(objectHull));
                }
            }
        }

        Area playerArea = new Area(playerHull);
        playerArea.intersect(combinedObstructionArea);
        graphics.setColor(new Color(255, 0, 0, 100));
        graphics.fill(playerArea);
    }

    private double getMaxY(Shape shape) {
        Rectangle2D bounds = shape.getBounds2D();
        return bounds.getMaxY();
    }

    private double getArea(Shape shape) {
        Rectangle2D bounds = shape.getBounds2D();
        return bounds.getWidth() * bounds.getHeight();
    }

    static {
        FLOOR_DECORATION_NAMES.add("Daisies");
        FLOOR_DECORATION_NAMES.add("Flowers");
        // Add more names to filter as needed
    }

    private boolean isFloorDecoration(GameObject gameObject) {
        return gameObject instanceof FloorDecoration || FLOOR_DECORATION_NAMES.contains(gameObject.getName());
    }
}