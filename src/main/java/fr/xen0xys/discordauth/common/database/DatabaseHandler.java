package fr.xen0xys.discordauth.common.database;

import fr.xen0xys.discordauth.common.database.models.Account;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Objects;
import java.util.UUID;

public class DatabaseHandler {

    private final SessionFactory sessionFactory;

    public DatabaseHandler(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public boolean isAccountExists(UUID uuid){
        try (Session session = this.sessionFactory.openSession()) {
            Query<Account> query = session.createQuery("SELECT a FROM Account a WHERE a.uuid = :uuid", Account.class);
            query.setParameter("uuid", uuid);
            return Objects.nonNull(query.uniqueResult());
        }
    }

    public boolean isAccountExists(String username) {
        try (Session session = this.sessionFactory.openSession()) {
            Query<Account> query = session.createQuery("SELECT a FROM Account a WHERE a.username = :username", Account.class);
            query.setParameter("username", username);
            return Objects.nonNull(query.uniqueResult());
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
