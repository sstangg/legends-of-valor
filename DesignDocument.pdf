+------------------------------------------------+
|                    Tile (abstract)            |
+------------------------------------------------+
| - row: int                                     |
| - col: int                                     |
+------------------------------------------------+
| + getRow(): int                                |
| + getCol(): int                                |
| + isAccessible(): boolean                      |
+------------------------------------------------+
                     ^
                     |
                     |
+--------------------+---------------------------+
|                    Block                       |
+------------------------------------------------+
| - type: Type                                   |
| - hasHeros: boolean                            |
+------------------------------------------------+
| + Block(row:int, col:int)                      |
| + Block(row:int, col:int, type:Type)          |
| + getType(): Type                              |
| + isAccessible(): boolean                      |
| + isMarket(): boolean                          |
| + isCommon(): boolean                          |
| + moveHerosOn(): void                           |
| + moveHerosOff(): void                          |
| + getSymbol(): String                           |
+------------------------------------------------+
                     |
                     v
                   Type
         -------------------
         | INACCESSIBLE     |
         | MARKET           |
         | COMMON           |
         -------------------

+------------------------------------------------+
|                 Character (abstract)          |
+------------------------------------------------+
| - row: int                                     |
| - col: int                                     |
+------------------------------------------------+
| + getRow(): int                                |
| + getCol(): int                                |
| + setPosition(row:int, col:int): void         |
| + displayStats(): void                         |
+------------------------------------------------+
                     ^
                     |
         ------------------------
         |                      |
+--------------------+     +--------------------+
|       Hero (abstract)|     |      Monster      |
+--------------------+     +--------------------+
| - name: String      |     | - name: String     |
| - type: String      |     | - type: String     |
| - level: int        |     | - level: int       |
| - experience: int   |     | - HP: int          |
| - HP: int           |     | - baseDamageValue: double|
| - baseHP: int       |     | - defenseValue: double  |
| - MP: int           |     | - dodgeAbility: double |
| - strength: int     |     +--------------------+
| - dexterity: int    |     | + getHP(): int     |
| - agility: int      |     | + setHP(hp:int)    |
| - gold: int         |     | + getName(): String|
| - inventory: List<Item>|  | + getDamage(): double |
| - equippedWeapon: Weapon| | + getDefense(): double|
| - equippedArmor: Armor | | + getDodge(): double |
+--------------------+     | + setDamage(val:int)|
| + getHP(): int      |     | + setDefense(val:int)|
| + getMP(): int      |     | + setDodge(val:int) |
| + getLevel(): int   |     | + print(): void    |
| + getGold(): int    |     +--------------------+
| + getWeapon(): Weapon|
| + getArmor(): Armor |
| + equip(w:Weapon)   |
| + equip(a:Armor)    |
| + moveTo(row:int, col:int) |
| + levelUp(monsters:int) |
| + regain()          |
| + buy(item:Item)    |
| + sell(item:Item)   |
| + use(spell:Spell)  |
| + take(potion:Potion) |
| + print(): void     |
| # applyBonus()      |
+--------------------+
         ^
         |
 -----------------------------
 |           |               |
Warrior    Sorcerer        Paladin

         ^
         |
  -------------------
  | Dragon          |
  | Exoskeleton     |
  | Spirit          |
  -------------------
          |
          v
       Monster

+------------------------------------------------+
|                      Item (abstract)          |
+------------------------------------------------+
| - name: String                                |
| - price: int                                  |
| - level: int                                  |
| - type: Type                                  |
+------------------------------------------------+
| + getName(): String                            |
| + getPrice(): int                              |
| + getLevel(): int                              |
| + print(): void                                |
+------------------------------------------------+
         ^
         |
-------------------------------
|          |        |         |
Weapon     Armor    Potion    Spell
| - damage: int
| - hands: int
| + getDamage(): int
| + getHands(): int
| + parse(line, level): Weapon
| + print(): void
------------------------------

+------------------------------------------------+
|                      Game (abstract)          |
+------------------------------------------------+
| - complete: boolean                            |
| - board: Board                                 |
+------------------------------------------------+
| + play(): boolean                              |
| + printBoard(): void                            |
+------------------------------------------------+
                     ^
                     |
+--------------------+---------------------------+
|               MonstersAndHeros                 |
+------------------------------------------------+
| - input: Input                                 |
| - heros: Hero[]                                |
| - monsters: Monster[]                          |
| - marketStock: List<Item>                      |
+------------------------------------------------+
| + MonstersAndHeros(input: Input, heroType: int[])|
| + play(): boolean                              |
| + printBoard(): void                            |
| - handleHerosInput(): void                     |
| - handleCommand(command: char): boolean        |
| - attemptMove(rowChange:int, colChange:int): boolean |
| - startBattle(): void                           |
| - createMonsters(): void                        |
| - herosTurn(): void                             |
| - monstersTurn(): void                           |
| - attackFrom(hero: Hero): Monster               |
| - castSpell(hero: Hero): void                   |
| - usePotion(hero: Hero): void                   |
| - manageInventory(): boolean                    |
| - enterMarket(): void                           |
| - loadMarketStock(level:int): void              |
+------------------------------------------------+

+------------------------------------------------+
|                    GameManager                 |
+------------------------------------------------+
| - input: Input                                 |
+------------------------------------------------+
| + start(): void                                |
| + askReplay(): boolean                          |
+------------------------------------------------+

+------------------------------------------------+
|                       Main                     |
+------------------------------------------------+
| + main(args: String[]): void                    |
+------------------------------------------------+
| Main --> GameManager                             |
| GameManager --> Game                              |
| MonstersAndHeros --|> Game                        |
| Hero --|> Character                                |
| Monster --|> Character                             |
| Warrior, Sorcerer, Paladin --|> Hero             |
| Dragon, Exoskeleton, Spirit --|> Monster         |
| Weapon, Armor, Potion, Spell --|> Item           |
| Block --|> Tile                                   |
| Block --> Type                                   |
+------------------------------------------------+