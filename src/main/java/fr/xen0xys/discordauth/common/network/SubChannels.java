package fr.xen0xys.discordauth.common.network;

public enum SubChannels {
    SESSION_ASK("SessionAsk"),
    SESSION_RESPONSE("SessionResponse"),
    CONNECTION_ASK("ConnectionAsk"),
    CONNECTION_RESPONSE("ConnectionResponse");

    private final String name;

    SubChannels(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static SubChannels from(String value){
        for(SubChannels subChannel : SubChannels.values())
            if(subChannel.getName().equals(value))
                return subChannel;
        return null;
    }
}
