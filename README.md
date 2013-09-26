WarpSigns2
----------

WarpSigns2 is a plugin for Points that allows for warping via signs in-game.

Permissions
-----------

WarpSigns2 uses the built-in Bukkit permissions API (Superperms).
It exports the following permissions nodes:

warpsigns2.create -- Allows the creation of warp signs
warpsigns2.follow -- Allows the usage of existing warp signs
warpsigns2.*      -- Allows all WarpSigns2 permissions

Dependencies
------------

WarpSigns2 requires the general-purpose warping plugin Points to run.
It can be obtained here:
    http://dev.bukkit.org/server-mods/points/
Place both jars in your plugins/ directory.

WarpSigns2 also requires the flat-file database FlatDB to be present in
your server's lib/ folder. Points is built to automatically download
FlatDB if it isn't present, so nothing has to be done on this front.