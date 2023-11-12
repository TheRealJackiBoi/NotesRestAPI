package dat3.dao.impl;

import dat3.dao.CRUDDao;
import dat3.model.Note;
import dat3.model.NoteGroup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class NoteDao extends CRUDDao<Note, Integer> {

    private static NoteDao instance;

    public static NoteDao getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new NoteDao();
            instance.setEntityManagerFactory(emf);
        }
        return instance;
    }

    public List<Note> readAllInNoteGroup(int id) {
        try(EntityManager em = super.getEmf().createEntityManager()){
            return em.createQuery("SELECT n FROM Note n WHERE noteGroup.id = :id", Note.class)
                    .setParameter("id", id)
                    .getResultList();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
