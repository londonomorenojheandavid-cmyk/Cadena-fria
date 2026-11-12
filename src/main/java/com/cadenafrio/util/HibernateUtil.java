package com.cadenafrio.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = build();

    private static SessionFactory build() {
        Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
        String envUrl = System.getenv("DB_URL");
        String envUser = System.getenv("DB_USER");
        String envPassword = System.getenv("DB_PASSWORD");
        if (envUrl != null && !envUrl.isBlank()) {
            cfg.setProperty("hibernate.connection.url", envUrl);
        }
        if (envUser != null && !envUser.isBlank()) {
            cfg.setProperty("hibernate.connection.username", envUser);
        }
        if (envPassword != null) {
            cfg.setProperty("hibernate.connection.password", envPassword);
        }
        return cfg.buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
