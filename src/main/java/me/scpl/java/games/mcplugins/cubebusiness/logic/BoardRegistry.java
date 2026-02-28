package me.scpl.java.games.mcplugins.cubebusiness.logic;

import me.scpl.java.games.mcplugins.cubebusiness.itemstacks.SpaceType;
import me.scpl.java.games.mcplugins.cubebusiness.itemstacks.TitleDeed;

public class BoardRegistry {

        // Array holding all 40 spaces (Index 0 = Space 1, Index 39 = Space 40)
        private static final TitleDeed[] deeds = new TitleDeed[40];
        private static final String[] spaceNames = new String[40];

        // This block runs once when the server starts and sets up the whole board
        static {
                // --- 1. SETUP SPACE NAMES (For Holograms/Armor Stands) ---
                spaceNames[0] = "&aGO";
                spaceNames[2] = "&3Community Chest";
                spaceNames[4] = "&cIncome Tax";
                spaceNames[7] = "&3Chance";
                spaceNames[10] = "&6Jail";
                spaceNames[17] = "&3Community Chest";
                spaceNames[20] = "&fFree Parking";
                spaceNames[22] = "&3Chance";
                spaceNames[30] = "&9Go To Jail";
                spaceNames[33] = "&3Community Chest";
                spaceNames[36] = "&3Chance";
                spaceNames[38] = "&cLuxury Tax";

                // --- 2. SETUP PROPERTIES (ID, Type, Name, Color, Price, Rents[Base, 1H, 2H,
                // 3H, 4H, Hotel], House Price) ---

                // Brown
                registerProperty(2, SpaceType.PROPERTY, "&5Old Kent Road", "&5Brown", 60,
                                new int[] { 2, 10, 30, 90, 160, 250 }, 50);
                registerProperty(4, SpaceType.PROPERTY, "&5Whitechapel Road", "&5Brown", 60,
                                new int[] { 4, 20, 60, 180, 320, 450 }, 50);

                // Light Blue
                registerProperty(7, SpaceType.PROPERTY, "&bThe Angel, Islington", "&bLight Blue", 100,
                                new int[] { 6, 30, 90, 270, 400, 550 }, 50);
                registerProperty(9, SpaceType.PROPERTY, "&bEuston Road", "&bLight Blue", 100,
                                new int[] { 6, 30, 90, 270, 400, 550 }, 50);
                registerProperty(10, SpaceType.PROPERTY, "&bPentonville Road", "&bLight Blue", 120,
                                new int[] { 8, 40, 100, 300, 450, 600 }, 50);

                // Pink
                registerProperty(12, SpaceType.PROPERTY, "&dPall Mall", "&dPink", 140,
                                new int[] { 10, 50, 150, 450, 625, 750 }, 100);
                registerProperty(14, SpaceType.PROPERTY, "&dWhitehall", "&dPink", 140,
                                new int[] { 10, 50, 150, 450, 625, 750 }, 100);
                registerProperty(15, SpaceType.PROPERTY, "&dNorthumrl'd Avenue", "&dPink", 160,
                                new int[] { 12, 60, 180, 500, 700, 900 }, 100);

                // Orange
                registerProperty(17, SpaceType.PROPERTY, "&6Bow Street", "&6Orange", 180,
                                new int[] { 14, 70, 200, 550, 750, 950 }, 100);
                registerProperty(19, SpaceType.PROPERTY, "&6Marlborough Street", "&6Orange", 180,
                                new int[] { 14, 70, 200, 550, 750, 950 }, 100);
                registerProperty(20, SpaceType.PROPERTY, "&6Vine Street", "&6Orange", 200,
                                new int[] { 16, 80, 220, 600, 800, 1000 }, 100);

                // Red
                registerProperty(22, SpaceType.PROPERTY, "&cStrand", "&cRed", 220,
                                new int[] { 18, 90, 250, 700, 875, 1050 }, 150);
                registerProperty(24, SpaceType.PROPERTY, "&cFleet Street", "&cRed", 220,
                                new int[] { 18, 90, 250, 700, 875, 1050 }, 150);
                registerProperty(25, SpaceType.PROPERTY, "&cTrafalgar Square", "&cRed", 240,
                                new int[] { 20, 100, 300, 750, 925, 1100 }, 150);

                // Yellow
                registerProperty(27, SpaceType.PROPERTY, "&eLeicester Square", "&eYellow", 260,
                                new int[] { 22, 110, 330, 800, 975, 1150 }, 150);
                registerProperty(28, SpaceType.PROPERTY, "&eCoventry Street", "&eYellow", 260,
                                new int[] { 22, 110, 330, 800, 975, 1150 }, 150);
                registerProperty(30, SpaceType.PROPERTY, "&ePiccadilly", "&eYellow", 280,
                                new int[] { 24, 120, 360, 850, 1025, 1200 }, 150);

                // Green
                registerProperty(32, SpaceType.PROPERTY, "&2Regent Street", "&2Green", 300,
                                new int[] { 26, 130, 390, 900, 1100, 1275 }, 200);
                registerProperty(33, SpaceType.PROPERTY, "&2Oxford Street", "&2Green", 300,
                                new int[] { 26, 130, 390, 900, 1100, 1275 }, 200);
                registerProperty(35, SpaceType.PROPERTY, "&2Bond Street", "&2Green", 320,
                                new int[] { 28, 150, 450, 1000, 1200, 1400 }, 200);

                // Dark Blue
                registerProperty(38, SpaceType.PROPERTY, "&1Park Lane", "&1Dark Blue", 350,
                                new int[] { 35, 175, 500, 1100, 1300, 1500 }, 200);
                registerProperty(40, SpaceType.PROPERTY, "&1Mayfair", "&1Dark Blue", 400,
                                new int[] { 50, 200, 600, 1400, 1700, 2000 }, 200);

                // --- 3. SETUP RAILROADS & UTILITIES ---

                // Railroads (Price: 200, Rent: 25, 50, 75, 100)
                int[] railroadRents = new int[] { 25, 50, 75, 100 };
                registerProperty(6, SpaceType.RAILROAD, "&8Kings Cross Station", "&8Railroad", 200, railroadRents, 0);
                registerProperty(16, SpaceType.RAILROAD, "&8Marylebone Station", "&8Railroad", 200, railroadRents, 0);
                registerProperty(26, SpaceType.RAILROAD, "&8Fenchurch St. Station", "&8Railroad", 200, railroadRents,
                                0);
                registerProperty(36, SpaceType.RAILROAD, "&8Liverpool St. Station", "&8Railroad", 200, railroadRents,
                                0);

                // Utilities (Price: 150, Rent multipliers: 4x, 10x roll)
                int[] utilityRents = new int[] { 4, 10 };
                registerProperty(13, SpaceType.UTILITY, "&7Electric Company", "&7Utility", 150, utilityRents, 0);
                registerProperty(29, SpaceType.UTILITY, "&7Water Works", "&7Utility", 150, utilityRents, 0);
        }

        /**
         * Helper method to cleanly add a property to the arrays.
         */
        private static void registerProperty(int id, SpaceType type, String name, String colorSet, int price,
                        int[] rents, int housePrice) {
                TitleDeed deed = new TitleDeed(id, type, name, colorSet, price, rents, housePrice);
                deeds[id - 1] = deed; // Store the deed
                spaceNames[id - 1] = name; // Also store the name for armor stands
        }

        /**
         * Gets the TitleDeed for a specific space (1-40). Returns null if it is not a
         * property (e.g., Go, Jail).
         */
        public static TitleDeed getDeed(int spaceId) {
                if (spaceId < 1 || spaceId > 40)
                        return null;
                return deeds[spaceId - 1];
        }

        /**
         * Gets the display name of any space on the board (1-40).
         */
        public static String getSpaceName(int spaceId) {
                if (spaceId < 1 || spaceId > 40)
                        return "&cUnknown Space";
                return spaceNames[spaceId - 1];
        }
}