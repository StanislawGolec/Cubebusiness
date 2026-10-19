# CubeBusiness

CubeBusiness is a turn-based board game plugin for PaperMC 1.20.6 servers that brings the classic UK-edition Monopoly ruleset into a 3D Minecraft world. Players join a match lobby, choose a custom Minecraft mob as their board token, roll dice using an in-game controller item, and move across a 40-space board complete with floating hologram labels and title deed cards.

---

## Features

### Implemented
* **Procedural 40-Space Board Geometry:** Calculates 3D world coordinates and yaw rotations for all 40 board spaces around a configurable origin point (`42x42` block perimeter with 4-block spacing).
* **Hologram Space Labels:** Spawns invisible, invulnerable `ArmorStand` holograms around the board perimeter for all streets, railroads, utilities, taxes, and corner spaces.
* **Custom Mob Game Pieces:** Allows players to spawn any supported Minecraft entity as their board piece (`/createpiece <entity>`), automatically disabling AI, gravity, explosion yield, and sun damage while protecting pieces from external damage or collisions.
* **Turn-Based Dice & Animated Movement:** Enforces turn order, rolls 2d6 via the handheld controller item, awards `$200` when passing `GO`, and animates piece movement space-by-space using scheduled Bukkit tasks.
* **UK Board Registry & Title Deeds:** Registers all 28 purchasable properties (Brown to Dark Blue color sets, 4 stations, 2 utilities) with accurate purchase prices, rent tiers, house costs, and mortgage values.
* **Title Deed GUI Viewer:** Provides a 45-slot read-only chest inventory (`CardListGUI`) displaying generated `TitleDeed` items and their full rent schedules, protected against item theft by `InventoryLockListener`.
* **Operator Testing Tools:** Includes `/cb force-start` for single-player testing and `/cb as <player> <subcommand>` to simulate multi-player interactions from a single client.

### Work in Progress
* **Property Acquisition & Rent Engine:** Purchasing unowned spaces on landing, collecting tiered rent based on color set monopolies and building upgrades, and handling mortgages.
* **Chat & GUI Auction System:** State-gated bidding flow via `AuctionChatListener` when a player declines to buy a property at list price.
* **Chance & Community Chest Decks:** Shuffled card decks for cash transfers, movement triggers, repairs, and `Get Out of Jail Free` cards.
* **Jail & Bankruptcy Mechanics:** Rolling doubles three times, `Go To Jail` space handling, bail payments, and asset liquidation on bankruptcy.

---

## Tech Stack & Requirements

* **Java:** 21
* **Server Platform:** PaperMC `1.20.6` (`io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT`)
* **Build System:** Apache Maven

---

## Project Structure

```text
Cubebusiness/
├── pom.xml
└── src/main/
    ├── resources/
    │   └── paper-plugin.yml
    └── java/me/scpl/java/games/mcplugins/cubebusiness/
        ├── CubeBusiness.java                  # Plugin entry point, command & listener registration
        ├── UsefulMethods.java                 # Color code translation & ItemStack factory helpers
        ├── commands/
        │   ├── CubeBusinessCommand.java       # Main /cb command (join, leave, start, stop, as, cardlist)
        │   ├── CreatePieceCommand.java        # /createpiece command with entity allowlist filtering
        │   └── ColourCodesCommand.java        # /colourcodes reference command
        ├── events/
        │   ├── ControllerInteractListener.java # Handles dice rolls and turn progression with Panel item
        │   ├── PieceProtectionListener.java   # Cancels damage, explosions, and collisions on game pieces
        │   ├── InventoryLockListener.java     # Prevents taking items from custom plugin GUIs
        │   └── AuctionChatListener.java       # Intercepts numeric chat bids during auctions (WIP)
        ├── gameobjects/
        │   └── CBPlayer.java                  # Player state (UUID, piece UUID, space, cash, deeds, jail state)
        ├── guis/
        │   ├── GUI.java                       # Base InventoryHolder wrapper
        │   └── CardListGUI.java               # 45-slot inventory displaying all 28 TitleDeed cards
        ├── itemstacks/
        │   ├── Controller.java                # Factory for the handheld Panel stick
        │   ├── TitleDeed.java                 # Property model and lore generator
        │   └── SpaceType.java                 # Enum: PROPERTY, RAILROAD, UTILITY
        └── logic/
            ├── GameManager.java               # Lobby state, turn rotation, and win condition checks
            ├── BoardManager.java              # 3D space coordinate math, piece spawning, and holograms
            ├── BoardRegistry.java             # Static definitions for all 40 board spaces and deeds
            ├── PieceManager.java              # Step-by-step piece teleportation and landing callbacks
            └── PlayerManager.java             # Active CBPlayer list container
```

---

## Commands & Controls

### Commands

| Command | Alias | Permission | Description |
| :--- | :--- | :--- | :--- |
| `/cubebusiness join` | `/cb join` | All Players | Join the current game lobby before a match starts. |
| `/cubebusiness leave` | `/cb leave` | All Players | Leave the game lobby prior to match start. |
| `/cubebusiness start` | `/cb start` | Operator | Spawn board holograms, distribute controllers, and start the match (requires 2+ players). |
| `/cubebusiness force-start` | `/cb force-start` | Operator | Start a match with a single player for testing. |
| `/cubebusiness stop` | `/cb stop` | Operator | Stop the active match. |
| `/cubebusiness cardlist` | `/cb cardlist` | Operator | View the title deeds inventory GUI. |
| `/cubebusiness as <player> <args...>` | `/cb as ...` | Operator | Execute a `/cb` subcommand on behalf of another online player. |
| `/createpiece <entity>` | - | All Players | Spawn or replace your board token on your current space. |
| `/colourcodes` | - | All Players | Display standard Minecraft `&` formatting and color codes in chat. |

### Handheld Controller (`Panel` Stick)

When the match starts, each participant receives a `Panel` item (`Material.STICK`):

* **Left-Click (Air / Block):** Roll two 6-sided dice (`2d6`) and move your piece along the board.
* **Shift + Right-Click (Air / Block):** End your current turn and pass control to the next active player.
* **Right-Click (Air / Block):** Open the player management panel (planned).

---

## Building from Source

1. Clone the repository and navigate to the project root containing `pom.xml`:
   ```bash
   cd Cubebusiness
   ```
2. Compile and package the plugin JAR using Maven:
   ```bash
   mvn clean package
   ```
3. Copy the compiled artifact from `target/CubeBusiness-1.0-SNAPSHOT.jar` into the `plugins/` directory of a Paper 1.20.6 server.

---

## Development Roadmap

### Phase 1: Core Fixes & Repository Hygiene
* Add a standard Maven/IDE `.gitignore` and untrack compiled artifacts in `target/`.
* Fix constructor initialization order in `GameManager` so `PieceManager` and `BoardManager` receive a valid reference (or inject `GameManager` directly).
* Switch command registration to `plugin.yml` or Paper's Brigadier `LifecycleEvents.COMMANDS` API so `getCommand()` resolves reliably on Paper 1.20.6.
* Update `PieceProtectionListener` to persist the newly spawned entity `UUID` on `CBPlayer` when a piece is recreated.

### Phase 2: Turn State Machine & Board Instance Isolation
* Introduce an explicit `TurnPhase` enum (`WAITING_FOR_ROLL`, `MOVING`, `AWAITING_BUY_DECISION`, `AUCTION`, `MANAGEMENT`, `TURN_COMPLETE`) to prevent rolling multiple times during movement animation.
* Separate static property metadata in `BoardRegistry` from per-match mutable state (`ownerUUID`, `isMortgaged`, `upgradeLevel`) so multiple matches or restarts start with a clean board.
* Tag the controller stick and deed items using Bukkit's `PersistentDataContainer` (`PDC`) instead of matching raw `Material.STICK`.

### Phase 3: Economy, Property Transactions & Auctions
* Implement property purchase on unowned space landing and automatic rent calculation (base rent, full color set double rent, house/hotel tiers, railroad count, and utility dice multipliers).
* Connect `AuctionChatListener` to the `AUCTION` turn phase with a countdown timer, minimum bid increments, balance validation, and English localization.
* Build the interactive player management GUI for buying/selling houses, mortgaging deeds, and trading with other players.

### Phase 4: Special Spaces, Cards & Paper API Modernization
* Implement `Jail` rules (3 consecutive doubles, `Go To Jail` on Space 31, paying bail, or using `Get Out of Jail Free` cards), `Income Tax`, and `Luxury Tax`.
* Implement `Chance` and `Community Chest` card decks.
* Migrate deprecated `ChatColor` and `AsyncPlayerChatEvent` calls to Paper's Kyori Adventure `Component` and `AsyncChatEvent` APIs.
* Add YAML configuration (`config.yml`) for board origin coordinates, world name, starting cash, and message localization.
