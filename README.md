# DiscordAuth V2.0.1
DiscordAuth plugin permit Discord server's owners to manage access to their Minecraft server:

- First create a channel and post a message, and a reaction.
- People who react to this message can create an account from their DM (stored in MySQL).
- Player can connect to Minecraft server and do "/login"
- You can also setup a channel for sending informations like player's connections, disconnections and advancements! (configurable)


## Warning!
**This plugin need JDASpigot plugin to work: https://www.spigotmc.org/resources/jdaspigot.41074/**

## Configuration:

```yaml
#
# THANKS FOR USING DISCORDAUTH!
#
# Notes: certain settings need to be changed, there are noted by "#*"

# If MySQL is disabled, plugin will use SQLite
mysql:
  enable: false
  host: 0.0.0.0
  port: 3306
  user: username
  password: password
  database: database_name

discord:
  #* Put here your bot token
  token: token
  # Disable discord part of the plugin, but keep server's safety and account management
  only_safety: false
  # Put here your discord guild id
  guild_id: 0
  # Put here the id of the message to which you want people to react to access the server
  message_id: 0
  # Channel for sending information
  channel_id: 0
  # Reaction for user react; default is :white_check_mark:
  reaction_name: U+2705
  # When a player connect to the server, send embed to channel attribute to "channel_id"
  enable_connection_message: true
  # When a player send message on Minecraft or Discord server, send message on Discord or Minecraft server respectively
  enable_shared_chat: true
  # Send DM to unregister user who has reacted (useful to disable new players joining)
  send_message_to_unregister_users: true
  # Send advancements in main channel
  send_advancements: true
  # Send message in main channel when a player die (with his dye reason)
  send_death_messages: true
  # Add logo to private messages response
  logo:
    enable: false
    url: null
  # Set bot status here
  activity:
    enable: false
    # Can be PLAYING, STREAMING, LISTENING or WATCHING
    type: PLAYING
    text: DiscordAuth V2.0!
    # Only for streaming
    url: null

encryption:
  # If you want to encrypt ip too (ip encryption use same encryption string)
  encrypt_ip: false
  # If you want to add a string to prevent bruteforce hack (if you change it after there is passwords, You will not be able to log in!)
  # This function is also known as "salt"
  additional_encryption_string:
    enable: false
    string: null

# Spawn point settings
spawnpoint:
  # Enable first time spawn point
  first_time_tp: false
  # Tp player at spawn point on login
  tp_on_login: false
  # Respawn settings
  respawn:
    # Tp player to spawn point if his die
    enable: false
    # If enabled, player will be tp at death even if his had a bed respawn
    even_with_bed: false
  # Spawn point location settings
  infos:
    world_name: world
    x: 0
    y: 80
    z: 0
    facing:
      pitch: 0
      yaw: 0

other:
  # Language file that will be used (can be custom file name in DiscordAuth/resources folder)
  # Can be "en_EN" or "fr_FR"
  language: en_EN
  # Determine if server use UUID or Username
  premium: true
  # Session duration in seconds, set 0 to disable
  session_duration: 3600
  # Set if login window is displayed when player connect or logout
  aggressive_login: true
```

Spigot page: https://www.spigotmc.org/resources/discordauth.92286/