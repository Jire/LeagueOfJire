# LeagueOfJire

_Free & Open-Source External Scripting Platform_

---

### Why is this free & open-source?

Basically this project was rushed proof-of-concept with little regard for code quality, and still uses the
soon-to-be-replaced `KNA 0.4.x`. Feel free to do basically whatever you want with it though, if you're making a LoL
cheat this is a great public source despite all the crap-code in the core.

**Despite this, performance is excellent.**

On my i7-6700K, it's capable of 700+ full game state captures per second, which I believe makes this the fastest
external. The way this was achieved was by simplifying the game object reading process (
see: [IGameUnitManager.kt](https://github.com/Jire/leagueofjire/blob/main/core/src/main/kotlin/com/leagueofjire/core/game/IGameUnitManager.kt#L61))
.

The overlay uses OpenGL for rendering (LibGDX back-end) and it's efficient and light enough that with the default
plugins you get practically no FPS drop in-game.

### Is there a plan for something better?

Yep! I'm working on a huge overhaul for `KNA 1.0.x` which will serve as the core for all my future game cheating
platforms. The new `KNA` will be completely free and open-source, of course.

**However, the new cheat platform core (back-end) will be proprietary (closed-source).** The reasoning is that these
projects are of substantial effort, and in the past with [Charlatano](https://github.com/Jire/Charlatano) for example,
people were making a whole lot of money off my work -- not fair for me and the other contributors who have spent
hundreds if not thousands of hours on the code.

The scripting API however, will be completely open-source, and many free and open-source scripts will be available!