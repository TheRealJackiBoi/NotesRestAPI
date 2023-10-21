package dat3.controller.impl;

import dat3.config.HibernateConfig;
import dat3.dao.impl.NoteDao;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

public class NoteController {

    private final NoteDao noteDao;

    public NoteController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        noteDao = NoteDao.getInstance(emf);
    }

    public void readAll(Context ctx) {

    }

    public void readOne(Context ctx) {

    }

    public void create(Context ctx) {

    }

    public void update(Context ctx) {

    }

    public void delete(Context ctx) {
    }
}
