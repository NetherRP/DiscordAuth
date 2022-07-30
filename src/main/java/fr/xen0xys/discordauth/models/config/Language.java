package fr.xen0xys.discordauth.models.config;

import fr.xen0xys.xen0lib.utils.ConfigurationReader;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

public class Language extends ConfigurationReader {

    // Login:
    public final String loginSuccess;
    public final String loginRequest;
    public final String reloadLoginRequest;
    public final String discordLogin;
    public final String forcedLogin;
    public final String sessionLogin;
    public final String needLogin;
    public final String playerForceLogged;
    public final String connectOnce;

    // Account:
    public final String accountCreatedSuccessful;
    public final String alreadyHasAccount;
    public final String accountNotExist;
    public final String noReaction;

    // Password:
    public final String updatedPassword;
    public final String invalidPassword;

    // Discord or Minecraft chat:
    public final String playerConnect;
    public final String playerDisconnect;
    public final String serverStart;
    public final String serverStop;

    // Errors:
    public final String errorOccurred;
    public final String alreadyConnected;
    public final String sessionEnd;
    public final String playerNotConnected;
    public final String cannotBeExecutedFromCLI;
    public final String unknownSpecifiedPlayer;
    public final String kickMessage;
    public final String passwordDefineError;

    // Information
    public final String acceptMessage;
    public final String actionSuccess;

    // Adds
    public final String accountDeleted;
    public final String noAccountForUsername;
    public final String idForUsername;
    public final String invalidUsernamePassword;
    public final String noIdenticalPasswords;
    public final String accountDeletionConfirm;
    public final String keepAccount;
    public final String deleteAccount;
    public final String noPermission;


    public Language(Plugin plugin, String configName) {
        super(plugin, configName);

        // Login:
        loginSuccess = this.getValue("login_success");
        loginRequest = this.getValue("login_request");
        reloadLoginRequest = this.getValue("reload_login_request");
        discordLogin = this.getValue("discord_login");
        forcedLogin = this.getValue("forced_login");
        sessionLogin = this.getValue("session_login");
        needLogin = this.getValue("need_login");
        playerForceLogged = this.getValue("player_force_logged");
        connectOnce = this.getValue("connect_once");

        // Account:
        accountCreatedSuccessful = this.getValue("account_created_successful");
        alreadyHasAccount = this.getValue("already_has_account");
        accountNotExist = this.getValue("account_not_exist");
        noReaction = this.getValue("no_reaction");

        // Password:
        updatedPassword = this.getValue("updated_password");
        invalidPassword = this.getValue("invalid_password");

        // Discord or Minecraft chat
        playerConnect = this.getValue("player_connect");
        playerDisconnect = this.getValue("player_disconnect");
        serverStart = this.getValue("server_start");
        serverStop = this.getValue("server_stop");

        // Errors:
        errorOccurred = this.getValue("error_occurred");
        alreadyConnected = this.getValue("already_connected");
        sessionEnd = this.getValue("session_end");
        playerNotConnected = this.getValue("player_not_connected");
        cannotBeExecutedFromCLI = this.getValue("cant_be_executed_from_cli");
        unknownSpecifiedPlayer = this.getValue("unknown_specified_player");
        kickMessage = this.getValue("kick_message");
        passwordDefineError = this.getValue("password_define_error");

        // Information
        acceptMessage = this.getValue("accept_message");
        actionSuccess = this.getValue("action_success");

        // Adds
        accountDeleted = this.getValue("account_deleted");
        noAccountForUsername = this.getValue("no_account_for_username");
        idForUsername = this.getValue("id_for_username");
        invalidUsernamePassword = this.getValue("invalid_username_password");
        noIdenticalPasswords = this.getValue("no_identical_passwords");
        accountDeletionConfirm = this.getValue("account_deletion_confirm");
        keepAccount = this.getValue("keep_account");
        deleteAccount = this.getValue("delete_account");
        noPermission = this.getValue("no_permission");
    }

    private String getValue(String path){
        String rawValue = this.getConfiguration().getString(path);
        if(rawValue != null){
            return ChatColor.translateAlternateColorCodes('&', rawValue);
        }
        return "Error when getting string, try to remove language file and restart the server!";
    }



}
