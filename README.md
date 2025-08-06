# Wiki
## Importation
```kts
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.Qg9:StonksMenu:1")
}
```

## Configuration

```yaml
title: "&lMissions"
pattern:
  - "$ $ $ $ $ $ $ $ $"
  - "$ * * * * * * * $"
  - "$ * # @ % ^ & * $"
  - "$ * * * * * * * $"
  - "$ $ $ $ $ $ $ $ $"

items:
  '$':
    type: STAINED_GLASS_PANE
    data: 15
    name: "&7"

  '#':
    type: DIAMOND_PICKAXE
    name: "&eMineur"
    lore:
      - "&7Commence ta vie de mineur !"
    enchanted: true

  '@':
    type: SKULL_ITEM
    data: 2
    name: "&eChasseur"
    lore:
      - "&7Part en chasse !"

  '%':
    type: WHEAT
    name: "&eFermier"
    lore:
      - "&7Travaille la terre !"

  '^':
    type: FISHING_ROD
    name: "&ePêcheur"
    lore:
      - "&7Attrape les poissons."

  '&':
    type: MAP
    name: "&eExplorateur"
    lore:
      - "&7Explore le monde !"

scripts:
  '#':
    requirement:
      - "[perm] missions.mineur"
      - "![eval] level < 10"
    needed: 2
    valid:
      actions:
        - "[message] &aMission Mineur acceptée !"
        - "[sound] ENTITY_EXPERIENCE_ORB_PICKUP"
    invalid:
      actions:
        - "[message] &cTu n'as pas le niveau ou la permission !"

  '@':
    actions:
      - "[message] &aTu es maintenant un chasseur !"
      - "[player] kit chasseur"
      - "[close]"

  '%':
    actions:
      - "[console] broadcast %player% a choisi la mission Fermier"
      - "[message] &aBonne culture !"

  '^':
    actions:
      - "[message] &aTu prends ta canne à pêche..."
      - "[sound] ENTITY_FISHING_BOBBER_SPLASH"

  '&':
    requirement:
      - "[perm] missions.explore"
    needed: 1
    valid:
      actions:
        - "[message] &aExplore !"
        - "[update]"
    invalid:
      actions:
        - "[message] &cTu ne peux pas explorer."

```

### Requirement

For now, there are two types of requirements:
- permissions : use [perm] prefix and after that the permission
- evalex : use [eval] prefix and then write something using evalex language, you can use basic function for maths and operator, you can also parse placeholders inside.
  You can add "!" as first character for the negation requirement (example : require player doesn't have this permission)

### Actions

Basic actions is a string list of pseudo-commands : you can use [player] prefix for a player command, [console] for a console one, [message] for sending a message to the player, [sound] for sending a sound, [close] for closing the gui and [update] for updating it.
You have the %player% placeholder.
There is also a more complex click action :
```yaml
requirement:
  - "[perm] missions.mineur"
  - "![eval] level < 10"
needed: 2
valid:
  actions:
    - "[message] &aMission Mineur acceptée !"
    - "[sound] ENTITY_EXPERIENCE_ORB_PICKUP"
invalid:
  actions:
    - "[message] &cTu n'as pas le niveau ou la permission !"
```
Requirement's section are for the click requirement, check previous section. needed is the number of requirement needed for valid action, -1 for all of them, -1 is the default value. valid and unvalid are the actions executed depending on the requirements.

## Basic Usage
```kotlin
MenuAPI.register(yourPlugin) //register api listeners
val menu = MenuAPI.getMenu(configurationSection) //creating a menu from config, you can also create manually a QGMenu
menu.open(player) //open the menu to the player
```
## Going further

You can handle your plugin interaction by managing the ui drawing :

```kotlin
val heads: MutableList<ItemStack> //A pack of itemstack, as instance for an ah
val char = '$' //char where you put then
val nullItem = ItemStack(Material.BARRIER) //default item
val menu = MenuAPI.getMenu(configurationSection)

menu.pattern.withIndex().filter { (i, c) -> c == char }.map { (i, _) -> i }.forEach {
    inv.setItem(it, heads.removeLastOrNull() ?: nullItem)
}

menu.open(player) //open the menu to the player
```

You can also handle the click :

```kotlin
val click = ClickScript { menu, player, slot ->
    player.sendMessage("§cInteraction from another plugin")
}
menu.actions['$'] = click
menu.open(player)
```
