package fr.xen0xys.discordauth.common.database;

import fr.xen0xys.discordauth.common.database.models.Account;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Objects;
import java.util.UUID;

public class DatabaseHandler {

    private final SessionFactory sessionFactory;

    public DatabaseHandler(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public boolean isAccountExists(UUID uuid){
        try(Session session = this.sessionFactory.openSession()){
            return Objects.nonNull(session.get(Account.class, uuid));
        }
    }

    public void addAccount(Account account){
        try(Session session = this.sessionFactory.openSession()){
            session.beginTransaction();
            session.persist(account);
            session.getTransaction().commit();
        }
    }

    public void updateAccount(Account account){
        try(Session session = this.sessionFactory.openSession()){
            session.beginTransaction();
            session.merge(account);
            session.getTransaction().commit();
        }
    }

    public void removeAccount(Account account){
        try(Session session = this.sessionFactory.openSession()){
            session.beginTransaction();
            session.remove(account);
            session.getTransaction().commit();
        }
    }

    public Account getAccount(String uuid){
        try(Session session = this.sessionFactory.openSession()){
            return session.get(Account.class, uuid);
        }
    }
}
