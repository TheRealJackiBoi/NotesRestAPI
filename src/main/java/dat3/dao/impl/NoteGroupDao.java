package dat3.dao.impl;

import dat3.dao.CRUDDao;
import dat3.model.Note;
import dat3.model.NoteGroup;
import jakarta.persistence.EntityManagerFactory;

public class NoteGroupDao extends CRUDDao<NoteGroup, Integer> {

    private static NoteGroupDao instance;

    public static NoteGroupDao getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new NoteGroupDao();
            instance.setEntityManagerFactory(emf);
        }
        return instance;
    }
}
