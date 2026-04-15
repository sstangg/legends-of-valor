Required sections: Header, Files, Notes, How to compile and run, I/O Example


# CS611-Assignment 4
## Legends of Valor
---------------------------------------------------------------------------
- Name: Nikki Rejai, Sophia Tang
- Email: nikkirj@bu.edu, sstang@bu.edu

## Files
---------------------------------------------------------------------------

src
- Abstract Classes
    - Board - Abstract class Defines the common structure and behavior of the game board.
    - Game - Abstract class that defines the general game.
    - Character - Abstract class for characters playing the game, moving around board, and engaging in battles.
    - Tile - Abstract class that defines the components that populate the game board.
  
- Core Classes
  - Character – Base class for all game characters (heroes and monsters), holding position info (row, col).
  - Monster – Abstract class for all monsters, encapsulating stats like HP, damage, defense, and dodge.
  - Hero – Abstract class for heroes, tracking attributes, inventory, equipped items, and level progression.
  - Item – Abstract class for all items (Weapons, Armor, Potions, Spells), storing name, price, and level.
  - LegendsOfValor - contains the logic for the game - moving on the board, entering market, and engaging in battle.

- Item classes
  - Weapon / Armor / Potion / Spell – Specialized item types with effects relevant for combat or stats.

- Hero subclasses
  - Warrior – hero which favors strength and agility when leveling up.
  - Paladin – hero which favors strength and dexterity.
  - Sorcerer – hero which favors dexterity and agility.

- Monster subclasses
  - Dragon – High-damage monster type.
  - Spirit – Agile monster type with boosted dodge ability.
  - Exoskeleton – Durable monster type with high defense.

- Game and Board classes
  - GameManager – Handles game start, player setup, and overall game loop.
  - Grid / Block / Tile – Represents the game board and individual tiles with accessibility and type info.

- IO & Utility
  - Input – Handles user input safely with validation.
  - FileLoader – Loads game data (heroes, monsters, items) from text files using a generic parser.
  - LineParser – Functional interface to parse each line from data files into objects.

The architecture supports scalability and extendability because:
- New hero or monster types can be added simply by subclassing Hero or Monster.
- Items can be extended with new effects or types without changing existing code and only updating the txt files and loading the new data.
- As heroes progress to higher levels, the inventory unlocks new items, which supports extendibility.


## Notes
---------------------------------------------------------------------------
Design Decisions:
1. Chose to add GameSession class which manages entire session, which includes getting users at the beginning and then they can choose who plays either game and maintains score.
2. Block, Box extend Tile class - Both represent a single unit on a board, allowing boards to handle them polymorphically while encapsulating game-specific state.
3. DotsAndBoxesGame and SlidingPuzzleGame extend Game - They share a common lifecycle and allow GameSession to run any game through the same interface.
4. DotsAndBoxesBoard and SlidingPuzzleBoard implement Board - Each board provides a specific implementation for storing and managing tiles.
5. Added input class to abstract console interaction from game logic.
6. Added Direction enum to simplify logic in Board classes and GameType enum to and GameSession class respectively.

Sliding puzzle fixes: 
9.  Ensured the puzzle is solvable by working backwards from solved board,
10. Added validation for user input and setting capacity for board size
11. Added explanation for how to play game
12. Made use of inheritance in Block class, Player class, Game class, and SlidingPuzzle class


## How to compile and run
---------------------------------------------------------------------------

1. Navigate to the directory "611-A4/src" after unzipping the files
2. Run the following instructions:

javac --release 8 -d bin core/\*.java game/\*.java grid/\*.java hero/\*.java iohandler/\*.java main/\*.java monster/\*.java

java -cp bin main/Main


## Input/Output Example
---------------------------------------------------------------------------
Example of execution for Legends of Valor game:


```
Enter your name: Nikki

Select difficulty:
1. Easy (monsters spawn every 5 rounds)
2. Medium (monsters spawn every 4 rounds)
3. Hard (monsters spawn every 3 round)
Choice (1-3): 1
Select type for Hero 1 (1: Warrior, 2: Sorcerer, 3: Paladin): 1
Select lane for Hero 1 (1: Left, 2: Middle, 3: Right): 1
Select type for Hero 2 (1: Warrior, 2: Sorcerer, 3: Paladin): 2
Select lane for Hero 2 (1: Left, 2: Middle, 3: Right): 2
Select type for Hero 3 (1: Warrior, 2: Sorcerer, 3: Paladin): 3
Select lane for Hero 3 (1: Left, 2: Middle, 3: Right): 3

Welcome to Legends of Valor Nikki!
Prepare yourself for new adventures and challenges ahead!

The game will start shortly with the following heroes:

==== Hero 1: Gaerdal_Ironhand I ====
==== Hero 2: Reign_Havoc II ====
==== Hero 3: Caliber_Heist III ====

MONSTERS SPAWNING...

----- FallenAngel (M1) spawned at (0,1)! -----
----- Aasterinian (M2) spawned at (0,4)! -----
----- Chiang-shih (M3) spawned at (0,6)! -----

Let's begin!

[ROUND 1]
============HEROS' MOVE===============
+=======+=======+=======+=======+=======+=======+=======+=======+
|   N   |   M1  |███████|   N   |   M2  |███████|   M3  |   N   |
+=======+=======+=======+=======+=======+=======+=======+=======+
|   K   |       |███████|   C   |       |███████|   K   |   K   |
+=======+=======+=======+=======+=======+=======+=======+=======+
|   K   |   C   |███████|   B   |   C   |███████|   X   |   K   |
+=======+=======+=======+=======+=======+=======+=======+=======+
|   X   |   K   |███████|   B   |   C   |███████|   X   |   C   |
+=======+=======+=======+=======+=======+=======+=======+=======+
|   K   |   K   |███████|   K   |   C   |███████|   K   |   K   |
+=======+=======+=======+=======+=======+=======+=======+=======+
|   X   |   C   |███████|   B   |       |███████|       |       |
+=======+=======+=======+=======+=======+=======+=======+=======+
|   X   |   X   |███████|   X   |   X   |███████|   K   |   C   |
+=======+=======+=======+=======+=======+=======+=======+=======+
|   N   |   H1  |███████|   H2  |   N   |███████|   H3  |   N   |
+=======+=======+=======+=======+=======+=======+=======+=======+

===== Gaerdal_Ironhand I's Turn =====
Position: Row 1, Col 1 | Lane: 1
HP: 450 | MP: 100 | Gold: 1354 | Level: 1

Controls:
W/A/S/D - Move (up/left/down/right)
I - Hero Info/Inventory
E - Equip/Unequip
P - Pass turn
F - Attack
C - Cast Spell
U - Use Potion
T - Teleport
R - Recall
M - Market (only at Nexus)
H - Help/Information
Q - Quit game
Your move:

No monsters! You are safe this time.

+---+---+---+---+---+---+---+---+
|   |   |   |   |   | M |   |   |
+---+---+---+---+---+---+---+---+
| P | M | M |   | X | M | M | M |
+---+---+---+---+---+---+---+---+
|   |   |   |   |   | M | M |   |
+---+---+---+---+---+---+---+---+
| X |   | M | X | M |   |   | M |
+---+---+---+---+---+---+---+---+
| X | X | M | X |   | M | M | M |
+---+---+---+---+---+---+---+---+
| M | X |   |   |   | X | X |   |
+---+---+---+---+---+---+---+---+
| X |   | M | M | X |   | X |   |
+---+---+---+---+---+---+---+---+
|   |   |   | M |   |   |   |   |
+---+---+---+---+---+---+---+---+
Controls:
W/A/S/D - move
I/C - manage inventory (view info, equip/use items)
M - enter market (if on market tile)
Q - quit game
H - Help/Information
Your move: d
+---+---+---+---+---+---+---+---+
|   |   |   |   |   | M |   |   |
+---+---+---+---+---+---+---+---+
|   | P | M |   | X | M | M | M |
+---+---+---+---+---+---+---+---+
|   |   |   |   |   | M | M |   |
+---+---+---+---+---+---+---+---+
| X |   | M | X | M |   |   | M |
+---+---+---+---+---+---+---+---+
| X | X | M | X |   | M | M | M |
+---+---+---+---+---+---+---+---+
| M | X |   |   |   | X | X |   |
+---+---+---+---+---+---+---+---+
| X |   | M | M | X |   | X |   |
+---+---+---+---+---+---+---+---+
|   |   |   | M |   |   |   |   |
+---+---+---+---+---+---+---+---+
Controls:
W/A/S/D - move
I/C - manage inventory (view info, equip/use items)
M - enter market (if on market tile)
Q - quit game
H - Help/Information
Your move: M
=============================================================================
Welcome to the Market! Here you can buy and sell items to enhance your heros.
=============================================================================

-- Primary Market Stock --
[1] [Armor] Platinum_Shield (10/10) (Price: 150, Level: 1, DamageReduction: 200)
[2] [Weapon] Sword (10/10) (Price: 500, Level: 1, Damage: 800, Hands:1)
[3] [Weapon] Dagger (10/10) (Price: 200, Level: 1, Damage: 250, Hands:1)
[4] [Potion] Healing_Potion (10/10) (Price: 250 Level: 1, Effect Type: Health, Effect Amount: 100)
[5] [Potion] Strength_Potion (10/10) (Price: 200 Level: 1, Effect Type: Strength, Effect Amount: 75)
[6] [Spell] Breath_of_Fire (10/10) (Price: 350, Level: 1, Spell Type: Fire, Damage: 450, Mana Cost: 100)
[7] [Spell] Ice_Blade (10/10) (Price: 250, Level: 1, Spell Type: Ice, Damage: 450, Mana Cost: 100)
[8] [Spell] Lightning_Dagger (10/10) (Price: 400, Level: 1, Spell Type: Lightning, Damage: 500, Mana Cost: 150)

Market controls:
I - Show Hero Info
B - Buy item
S - Sell item
R - Repair broken equipment
E - Exit market
Q - Quit game
Enter your choice: 
B 
Select a hero to buy for:
(1) Eunoia_Cyn I [Level: 0, Gold: 2500]
(2) Segojan_Earthcaller II [Level: 0, Gold: 2500]
(3) Parzival III [Level: 1, Gold: 2500]
Hero number: 3
Hero Gold Budget: 2500
Enter item to buy: 6
Purchased: Breath_of_Fire
Purchase successful.
+---+---+---+---+---+---+---+---+
|   |   |   |   |   | M |   |   |
+---+---+---+---+---+---+---+---+
|   | P | M |   | X | M | M | M |
+---+---+---+---+---+---+---+---+
|   |   |   |   |   | M | M |   |
+---+---+---+---+---+---+---+---+
| X |   | M | X | M |   |   | M |
+---+---+---+---+---+---+---+---+
| X | X | M | X |   | M | M | M |
+---+---+---+---+---+---+---+---+
| M | X |   |   |   | X | X |   |
+---+---+---+---+---+---+---+---+
| X |   | M | M | X |   | X |   |
+---+---+---+---+---+---+---+---+
|   |   |   | M |   |   |   |   |
+---+---+---+---+---+---+---+---+
Controls:
W/A/S/D - move
I/C - manage inventory (view info, equip/use items)
M - enter market (if on market tile)
Q - quit game
H - Help/Information
Your move: w
No monsters! You are safe this time.

+---+---+---+---+---+---+---+---+
|   | P |   |   |   | M |   |   |
+---+---+---+---+---+---+---+---+
|   | M | M |   | X | M | M | M |
+---+---+---+---+---+---+---+---+
|   |   |   |   |   | M | M |   |
+---+---+---+---+---+---+---+---+
| X |   | M | X | M |   |   | M |
+---+---+---+---+---+---+---+---+
| X | X | M | X |   | M | M | M |
+---+---+---+---+---+---+---+---+
| M | X |   |   |   | X | X |   |
+---+---+---+---+---+---+---+---+
| X |   | M | M | X |   | X |   |
+---+---+---+---+---+---+---+---+
|   |   |   | M |   |   |   |   |
+---+---+---+---+---+---+---+---+
Controls:
W/A/S/D - move
I/C - manage inventory (view info, equip/use items)
M - enter market (if on market tile)
Q - quit game
H - Help/Information
Your move: a
No monsters! You are safe this time.

+---+---+---+---+---+---+---+---+
| P |   |   |   |   | M |   |   |
+---+---+---+---+---+---+---+---+
|   | M | M |   | X | M | M | M |
+---+---+---+---+---+---+---+---+
|   |   |   |   |   | M | M |   |
+---+---+---+---+---+---+---+---+
| X |   | M | X | M |   |   | M |
+---+---+---+---+---+---+---+---+
| X | X | M | X |   | M | M | M |
+---+---+---+---+---+---+---+---+
| M | X |   |   |   | X | X |   |
+---+---+---+---+---+---+---+---+
| X |   | M | M | X |   | X |   |
+---+---+---+---+---+---+---+---+
|   |   |   | M |   |   |   |   |
+---+---+---+---+---+---+---+---+
Controls:
W/A/S/D - move
I/C - manage inventory (view info, equip/use items)
M - enter market (if on market tile)
Q - quit game
H - Help/Information
Your move: s
Oh no! Monsters on the loose! Entering battle now!!

----- ROUND 1 -----
=============================================
HEROS:
Eunoia_Cyn I (HP: 450, MP: 400)
Segojan_Earthcaller II (HP: 500, MP: 900)
Parzival III (HP: 100, MP: 300)

MONSTERS:
Exodia (HP: 1000)
Chrysophylax (HP: 200)
Exodia (HP: 1000)
=============================================
--- Heros' Turn ---
Actions for Eunoia_Cyn I (A=Attack, S=Spell, P=Potion, E=Equip, I=Info, Q=Quit):
A
Select target monster:
(1) Exodia (HP: 1000)
(2) Chrysophylax (HP: 200)
(3) Melchiresas (HP: 700)
2

Chrysophylax dodged the attack!

Actions for Segojan_Earthcaller II (A=Attack, S=Spell, P=Potion, E=Equip, I=Info, Q=Quit):
1
Invalid input. Try again.
A
Select target monster:
(1) Exodia (HP: 1000)
(2) Chrysophylax (HP: 200)
(3) Melchiresas (HP: 700)
1

Segojan_Earthcaller II attacked Exodia Chrysophylax with 76 damage

Actions for Parzival III (A=Attack, S=Spell, P=Potion, E=Equip, I=Info, Q=Quit):
S
Select a spell to cast:
(1) Breath_of_Fire - [Level: 1, Damage: 450, Mana Cost:100, Type: Fire]
1
Select target monster:
(1) Exodia (HP: 924)
(2) Chrysophylax (HP: 200)
(3) Melchiresas (HP: 700)
3

Parzival III cast Breath_of_Fire on Melchiresas


Melchiresas dodged the spell!

--- Monsters' Turn ---

Exodia attacking Eunoia_Cyn I...


Exodia hit Eunoia_Cyn I with 50 damage



Chrysophylax attacking Parzival III...


Parzival III dodged the attack!


Exodia attacking Segojan_Earthcaller II...


Segojan_Earthcaller II dodged the attack!


----- ROUND 2 -----
=============================================
HEROS:
Eunoia_Cyn I (HP: 440, MP: 440)
Segojan_Earthcaller II (HP: 550, MP: 990)
Parzival III (HP: 110, MP: 220)

MONSTERS:
Exodia (HP: 924)
Chrysophylax (HP: 200)
Melchiresas (HP: 700)
=============================================
--- Heros' Turn ---
Actions for Eunoia_Cyn I (A=Attack, S=Spell, P=Potion, E=Equip, I=Info, Q=Quit):
Q
Thank you for playing! Exiting the game...
```