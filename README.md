# DiscordAuth v4.0.0
## What is DiscordAuth?
DiscordAuth is a Minecraft plugin that allow account creation over Discord and server protection against unauthorized users.

## Notice
DiscordAuth can't be used for commercial purposes. You can't sell this plugin or any of its forks.

DiscordAuth only support PaperMC, PurpurMC and Waterfall servers; No support will be given for others forks.

## How to install?
Currently, DiscordAuth doesn't support setup with only one PaperMC server. You need to have at least two servers: one for game and one for Waterfall.

### Waterfall setup
1. Download and install DiscordAuth on both Waterfall and PaperMC Server
2. Start and stop your servers
3. On Waterfall: 
   1. In plugins/DiscordAuth/config.yml, change the `key: unsafe_key` to a most secure key
   2. In plugins/DiscordAuth/configs/core.yml, change:
      1. The `token: token` with your Discord bot token
      2. The `guild_id: 0` with your Discord guild ID
4. On PaperMC servers:
   1. In plugins/DiscordAuth/config.yml, change the `key: unsafe_key` to the same key as Waterfall
5. Start your servers
6. On Discord, execute `/register` command
7. Now anyone that click on the register button will be able to register on your server
8. Now connect to the server and put your password into the anvil GUI
9. 
## Next features
- [ ] Single server support

## Links
- [PaperMC](https://papermc.io/software/paper)
- [Waterfall](https://papermc.io/software/waterfall)
- [DiscordAuth on SpigotMC](https://www.spigotmc.org/resources/discordauth.92286/)
