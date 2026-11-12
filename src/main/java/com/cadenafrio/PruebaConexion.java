package com.cadenafrio;

import com.cadenafrio.model.Cava;
import com.cadenafrio.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class PruebaConexion {
    public static void main(String[] args) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            List<Cava> cavas = s.createQuery("from Cava", Cava.class).list();
            System.out.println("Conexión OK. Cavas: " + cavas.size());
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
