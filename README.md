# Visibility Script

This script is designed for the Old School RuneScape game using the DreamBot API. It checks if your player character is blocked from the camera view and provides visual feedback based on the player's visibility state.

## Overview

The script defines four different visibility states for the player character:
- `FULLY_VISIBLE`: The player is fully visible.
- `LESS_THAN_HALF_OBSTRUCTED`: The player is less than half obstructed.
- `MORE_THAN_HALF_OBSTRUCTED`: The player is more than half obstructed.
- `FULLY_OBSTRUCTED`: The player is fully obstructed.

The script continuously checks the player's visibility and logs the current state. It also provides visual feedback on the screen.

## Usefulness

This script can help in several ways:
- **Resource Gathering Bots**: Maintain visibility while gathering resources like mining, fishing, or woodcutting and avoiding obstructions as a human would.
- **Camera**: Help the bot to adjust and remain visible, mimicking how a human player would react.
- **Camera Adjustments**: Extend the script to automatically adjust the camera angle to maintain player visibility.

## Classes

### Main.java

The `Main` class is the entry point of the script. It initializes the `VisibilityChecker` and updates the player's visibility state in a loop. It also handles the on-screen drawing of the visibility state.

### PlayerVisibilityState.java

The `PlayerVisibilityState` enum defines the different visibility states for the player character.

### VisibilityChecker.java

The `VisibilityChecker` class contains the logic to determine the player's visibility state by checking for obstructions from nearby NPCs and game objects. It calculates the coverage ratio and categorizes it into one of the defined visibility states. It also provides methods to draw the visibility state on the screen.

## Known Issues

- **Z Coordinate Limitations**: Due to the DreamBot API, the script cannot grab objects from other levels (Z coordinates). This means it does not take into consideration objects that are above or below the player's current level when calculating player visibility.

## How to Run

1. Clone the repository to your local machine.
2. Open the project in your preferred Java IDE.
3. Make sure you have the DreamBot client and API properly set up in your development environment.
4. Run the `Main` class as a DreamBot script.
