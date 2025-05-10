
# InventoryDeclutter
_For when there are just way too many buttons in your inventory._

It is common for a mod to implement another inventory screen that has extra slots for
its own things. Then it puts a button in the normal inventory screen to take you there.

While this is not a problem with one mod, in big modpacks this can lead to clutter. It can also lead to inconvenience,
because one screen added by a mod may not account for one added by another, preventing you from accessing other screens
from the one you are currently in.

This mod aims to solve that issue. InventoryDeclutter adds a simple panel that has buttons for screens added by mods.

## Features:
* **Customizable panel**. All aspects of the panel (positioning, sizing and orientation) can be easily
configured from the config screen in the mod list.
* **Easy API.** Adding an entry in the panel is as simple as adding anything using the
NeoForge registry system.
* **Different builds for your purpose.** The mod comes with two different builds: A client-only build
and a full build. 
  * The client-only build is for use with mods that already implement their own screen opening network 
  logic, and is not needed on the server.
  **WARNING:** Trying to add Menu or Screen entries on the client-only build will prevent the mod from loading and crash the game.
  **WARNING:** Trying to use the client-only build on the server will prevent mod from loading and crash the server.
  * The full build is for mods that don't want to bother with the screen opening network logic. All that stuff is 
  already implemented in the full build. All you have to do is use the right class in the API and InventoryDeclutter 
  will take care of it for you. This build needs to be included on the server too.

## Usage:
No maven repository yet :(

Download the mod file and API file of your desired InventoryClutter build.
Put the jars in a folder called "libs" in your project root and add this to your build.gradle file:

```
dependencies {
    compileOnly "blank:[api jar name]"
    localRuntime "blank:[mod jar name]" //for client only build
    runtimeOnly "blank:[mod jar name]" //for full build
}
```

## Contributions:

Always welcome. Open a PR and I'll check it out probably maybe hopefully :)

