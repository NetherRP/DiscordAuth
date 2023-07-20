package fr.xen0xys.discordauth.common.database;

import fr.xen0xys.discordauth.common.database.models.Account;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.community.dialect.SQLiteDialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.PostgreSQLDialect;

public class HibernateConfig {

    private DatabaseType databaseType;
    private String host;
    private String port;
    private String database;
    private String username;
    private String password;

    public SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration();
        switch (databaseType) {
            case SQLITE -> {
                configuration.setProperty(AvailableSettings.DRIVER, "org.sqlite.JDBC");
                configuration.setProperty(AvailableSettings.URL, "jdbc:sqlite:" + database +".db");
                configuration.setProperty(AvailableSettings.DIALECT, SQLiteDialect.class.getName());
            }
            case MYSQL -> {
                configuration.setProperty(AvailableSettings.DRIVER, "com.mysql.cj.jdbc.Driver");
                configuration.setProperty(AvailableSettings.URL, "jdbc:mysql://" + host + ":" + port + "/" + database);
                configuration.setProperty(AvailableSettings.USER, username);
                configuration.setProperty(AvailableSettings.PASS, password);
                configuration.setProperty(AvailableSettings.DIALECT, MySQLDialect.class.getName());
            }
            case POSTGRESQL -> {
                configuration.setProperty(AvailableSettings.DRIVER, "org.postgresql.Driver");
                configuration.setProperty(AvailableSettings.URL, "jdbc:postgresql://" + host + ":" + port + "/" + database);
                configuration.setProperty(AvailableSettings.USER, username);
                configuration.setProperty(AvailableSettings.PASS, password);
                configuration.setProperty(AvailableSettings.DIALECT, PostgreSQLDialect.class.getName());
            }
        }
        configuration.setProperty(AvailableSettings.SHOW_SQL, "false");
        configuration.setProperty(AvailableSettings.HBM2DDL_AUTO, "update");
        configuration.addAnnotatedClass(Account.class);
        return configuration.buildSessionFactory();
    }

    public HibernateConfig setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
        return this;
    }

    public HibernateConfig setHost(String host) {
        this.host = host;
        return this;
    }

    public HibernateConfig setPort(String port) {
        this.port = port;
        return this;
    }

    public HibernateConfig setDatabase(String database) {
        this.database = database;
        return this;
    }

    public HibernateConfig setUsername(String username) {
        this.username = username;
        return this;
    }

    public HibernateConfig setPassword(String password) {
        this.password = password;
        return this;
    }
}
