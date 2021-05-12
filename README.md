# DiscordAuth
DiscordAuth plugin permit Discord server's owners to manage access to their Minecraft server:

First create a channel and post a message, and a reaction.
People who react to this message can create an account from their DM (stored in MySQL).
Player can connect to Minecraft server and do "/login <password>"
You can also setup a channel for sending informations like player's connections, disconnections and advancements! (configurable)

**This plugin need JDASpigot plugin to work: https://www.spigotmc.org/resources/jdaspigot.41074/**

### Configuration:

```yaml
mysql:
  host: 0.0.0.0
  port: 3306
  user: username
  password: password
  database: database_name

discord:
  # Put here your bot token
  token: token
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
  # Send DM to unregister user who has react
  send_message_to_unregister_users: true

encryption:
  # If you want to encrypt ip too (ip encryption use same encryption string)
  encrypt_ip: false
  # If you want to add an additional string to prevent bruteforce hack (if you change it after there is passwords, You will not be able to log in!)
  additional_encryption_string:
    enable: false
    string: null

# Only use for first spawn
spawnpoint:
  world_name: world
  x: 0
  y: 80
  z: 0
  facing:
    pitch: 0
    yaw: 0
```

Spigot page: https://www.spigotmc.org/resources/discordauth.92286/
