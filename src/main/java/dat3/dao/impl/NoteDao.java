package dat3.dao.impl;

import dat3.dao.CRUDDao;
import dat3.model.Note;
import jakarta.persistence.EntityManagerFactory;

public class NoteDao extends CRUDDao<Note, Integer> {

    private static NoteDao instance;

    public static NoteDao getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new NoteDao();
            instance.setEntityManagerFactory(emf);
        }
        return instance;
    }
}
