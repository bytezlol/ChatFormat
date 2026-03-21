# ChatFormat

A lightweight Paper plugin that replaces vanilla chat formatting with a fully configurable format, supporting LuckPerms prefixes/suffixes, PlaceholderAPI, and every color format Minecraft has to offer.

## Requirements

- Paper 1.21+
- LuckPerms
- PlaceholderAPI *(optional)*

## Installation

1. Drop `ChatFormat.jar` into your `plugins/` folder.
2. Restart the server.
3. Edit `plugins/ChatFormat/config.yml` to your liking.
4. Run `/chatformat reload`.

## Configuration

```yaml
# plugins/ChatFormat/config.yml

# Available placeholders:
#   {prefix}       - LuckPerms prefix
#   {suffix}       - LuckPerms suffix
#   {name}         - player name
#   {displayname}  - player display name
#   {world}        - world name
#   {message}      - chat message
#   %placeholder%  - any PlaceholderAPI placeholder

format: "{prefix}{name} <dark_gray>▶ <white>{message}"
```

## Color Formats

Every color format is supported and can be freely mixed in both the config format and LuckPerms prefixes/suffixes.

| Format | Example |
|---|---|
| MiniMessage | `<red>`, `<bold>`, `<#FF0000>` |
| MiniMessage gradient | `<gradient:#FF0000:#00FF00>text</gradient>` |
| Legacy `&` codes | `&c`, `&l`, `&r` |
| Legacy `§` codes | `§c`, `§l` |
| Hex `&#` | `&#FF0000` |
| Bukkit packed hex | `&x&F&F&0&0&0&0` |

## Permissions

| Permission              | Description                            | Default |
|-------------------------|----------------------------------------|---------|
| `chatformat.admin`      | Access to `/chatformat reload`         | op      |
| `chatformat.colorcodes` | Allow `&` color codes in chat messages | false   |
| `chatformat.rgbcodes`   | Allow hex color codes in chat messages | false   |
| `chatformat.reload`     | Permission to reload the config file   | op      |


## Commands

| Command | Description | Permission |
|---|---|---|
| `/chatformat reload` | Reloads the config | `chatformat.admin` |

## Example Formats

```yaml
# Clean and simple
format: "{prefix}{name}<dark_gray>: <white>{message}"

# Gradient name
format: "<gradient:#FF0000:#FFFF00>{name}</gradient> <dark_gray>▶ <white>{message}"

# With suffix and world
format: "{prefix}{name}{suffix} <dark_gray>[{world}] ▶ <white>{message}"

# With PlaceholderAPI
format: "{prefix}{name} <dark_gray>[%player_health%❤] ▶ <white>{message}"
```
