# Minecraft-Spec-Plugin
This Minecraft 1.17.1 plugin allow players on a server to enter spectator mode using a command. If they use the command again they will leave the spectator mode again and will be teleportet back to their original position. Additionaly all effect, remaining air bubbles, gamemode and the time on fire will be restored.

## Commands
- `/c`, `/spec` or `/spectator` to enter spectator mode
- `/c stay`, `/spec stay` oder `/spectator stay` to stay where you are currently in spectator mode (Admin command)

## Permission
- `spec.*` - All Plugin Permissions
- `spec.use` - Use the `/c`, `/spec` and `/spectator` commands.
- `spec.stay` - Get access to the `stay` sub-command
- `spec.bypass` - Bypass requirements to enter spectator mode

