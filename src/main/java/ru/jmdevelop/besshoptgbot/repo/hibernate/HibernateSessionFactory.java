package ru.jmdevelop.besshoptgbot.repo.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import ru.jmdevelop.besshoptgbot.models.entity.*;

public final class HibernateSessionFactory {

    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private HibernateSessionFactory() {
    }

    private static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.configure();

        new EnvironmentPropertiesPopulator().populate(configuration.getProperties());
        addAnnotatedClasses(configuration);

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(builder.build());
    }

    private static void addAnnotatedClasses(Configuration configuration) {
        configuration.addAnnotatedClass(Category.class);
        configuration.addAnnotatedClass(Client.class);
        configuration.addAnnotatedClass(Order.class);
        configuration.addAnnotatedClass(OrderItem.class);
        configuration.addAnnotatedClass(OrderStatus.class);
        configuration.addAnnotatedClass(Product.class);
        configuration.addAnnotatedClass(Message.class);
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

}