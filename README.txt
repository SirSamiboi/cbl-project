CBL Project for 2IP90 Programming course

AUTHORS:
Sam Maurer (2268000)
Stas Luzgin (2277921)


Welcome to Magic Defender!

The story:

In this game, you act as the commander of a castle. A horde of monsters, led by a man known as
"The Fallen", are laying an attack on the castle, and your King has tasked you with defeating
these monsters to protect his empire. To do this, you must construct different types of magic
towers which will hold back and defeat the enemies in various ways. If you manage to survive the
attacks, The Fallen has vowed to tear the castle apart by himself. Will you manage to defeat him?


How to run:

- Open the MagicDefender folder with a program that can run Java files, such as Visual Studio Code
- Run Main.java
- Once the game is open, select "Start Game" to start a round, and enjoy :)


How to play:

- Enemies arrive in waves; survive the 15th wave and defeat The Fallen to win
- Select one of the 8 empty plots to build a tower
    - Basic towers shoot energy beams which target and damage a single enemy
    - Fireball towers shoot slow but powerful blasts that can damage multiple enemies at once
    - Chill towers cause groups of enemies to freeze and slow down
- Once a tower has been placed, it can be upgraded to improve a tower's attack damage, range, effect and/or speed
- Towers are built and upgraded with gold
- You start a round with 50 gold, you earn more by defeating enemies
- To upgrade a tower, the tower must first be clicked to be selected. This will also let you see its range of attack
- Enemies will appear from the top-left corner of the map, and will travel along the visible path
- If an enemy reaches the bottom-right corner without being defeated, you will lose lives as the castle is damaged
- If you lose all 10 lives before defeating the 15th wave of enemies, the game is lost
- Try experimenting with different tower placements to find the most efficient setup
- Once a tower has been placed, it cannot be removed, so don't place basic towers everywhere, and never begin a round with a chill tower!


Testing:

All of the game's functionality is accessed with a game round. The game can be tested by simply
starting the program and pressing "Start Game" to begin a round. Depending on the player's actions,
the game will result in either victory or defeat.


Sources:

Our main inspirations for the game were the "Bloons Tower Defense" and "Kingdom Rush" game series.
We both have varying amounts of experience with both game series, so since early in development,
we had a clear vision of how the game should play out.

For game design, we implemented features similar to the above games that are common in the tower
defense game genre, such as health bars, enemy waves, tower placement spots and the enemy path.
We performed extensive testing to improve game balance, i.e. adjusting the game's difficulty to be
a challenge but also forgiving and flexible, so the player can explore a variety of tower setups.
We also balanced enemy waves and tower upgrades to give the player a sense of progression as they
survive through waves and improve their defenses, aiming to make the game as fun as possible.

For the use of a timer for game state updates, we researched the Java.util.Timer class on
geeksforgeeks.org. By using a global timer variable that increments on every "tick" (the game's
time unit, ~33ms), we have been able to control all events and animations in the game.

For more specific questions, such as how to flip images, we also used geeksforgeeks.org to do
research and devise a solution, or we searched Stack Exchange to find answers, and then adapted
the relevant answers to fit the program.


Assets:

craftpix.net via itch.io - 2D Tileset Pixel Art for TD Free Pack
https://free-game-assets.itch.io/free-fields-tileset-pixel-art-for-tower-defense

Foozle via itch.io - Spire Tower Pack 1, 3, 4
https://foozlecc.itch.io/spire-tower-pack-1
https://foozlecc.itch.io/spire-tower-pack-3
https://foozlecc.itch.io/spire-tower-pack-4

craftpix.net - Chibi 2D Game Sprites
https://craftpix.net/freebies/free-orc-ogre-and-goblin-chibi-2d-game-sprites/
https://craftpix.net/freebies/free-chibi-skeleton-crusader-character-sprites/
https://craftpix.net/freebies/free-golems-chibi-2d-game-sprites/
https://craftpix.net/freebies/free-fallen-angel-chibi-2d-game-sprites/
https://craftpix.net/freebies/free-chibi-necromancer-of-the-shadow-character-sprites/

craftpix.net - Free Monster Enemy Game Sprites
https://craftpix.net/freebies/free-monster-enemy-game-sprites/

Many of these assets have been combined or modified in order to create the final assets used in the program.


Sound effects:

jsxfr
basicAttack: https://sfxr.me/#34T6PkkDngdb9rNsAUrzatDpFuy4QfMJoZASmmfbtprhJP8zzVTK9nQGyWcruaaXnKjV7vPTWJRtzfT88iqi8gFPtpbBFs3pDGWFE4sWWnfAJawzJKe1EV1od
fireballAttack: https://sfxr.me/#7BMHBGSTwiL6a6a5Xg9MJEfuE2iBJdC1oEsezx5PRhdJNeo5CqAG16Eb2tjhi5Qn1VJkBFe1Cn7yudwK2KEK8pThEPtGEoBgXJ5c5vVzQbV7gDfkqaeH4ySSb
chillAttack: https://sfxr.me/#34T6PkmsRpTyJb3fRJjKQ5Cvaq9jzfNoQWYAn3zbrNJvuPzZ9kMs4dn3ZGHDDTcmTknMAyiK2SRTuvYeRmDepwGzgy8NqtT6kGDkf5CGrwFax1T6viNB18uMH
fallenTransformation: https://sfxr.me/#1111191C7ofVgHtkHb577aHvfuV7fiQ1xyFG8Q6ZbNctGSa4p4tMif6GF8byAWESgn5oH6WD6A5Sg2ZjWZWUKkpMnK8knMCVUPovN1cnqiyMJynFGkTMpUTy

Clash Royale via 101soundboards.com - Goblin Arena Jingle 01, Scroll Win 02, Scroll Lose 01
waveStart: https://www.101soundboards.com/sounds/730952-goblin-arena-jingle-01
victory: https://www.101soundboards.com/sounds/730936-scroll-win-02
defeat: https://www.101soundboards.com/sounds/730944-scroll-lose-01