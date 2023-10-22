package dat3.controller.impl;

import dat3.config.HibernateConfig;
import dat3.dao.impl.NoteDao;
import dat3.dao.impl.NoteGroupDao;
import dat3.exception.ApiException;
import dat3.model.Note;
import dat3.model.NoteGroup;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class NoteController {

    private final NoteDao noteDao;
    private final NoteGroupDao noteGroupDao;

    public NoteController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        noteDao = NoteDao.getInstance(emf);
        noteGroupDao = NoteGroupDao.getInstance(emf);
    }

    public void readAll(Context ctx) {
        List<Note> notes = noteDao.readAll(Note.class);
        if (notes == null) {
            ctx.status(404);
            return;
        }
        ctx.status(200);
        ctx.json(notes);
    }

    public void read(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Note note = noteDao.read(Note.class, id);
        if (note == null) {
            ctx.status(404);
            return;
        }
        ctx.status(200);
        ctx.json(note);
    }

    public void create(Context ctx) throws ApiException {
        Note note = ctx.bodyAsClass(Note.class);
        note = noteDao.create(note);
        if (note == null) {
            throw new ApiException(500, "Could not create note");
        }
        ctx.status(201);
        ctx.json(note);
    }

    public void update(Context ctx) throws ApiException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Note note = ctx.bodyAsClass(Note.class);
        Note noteToUpdate = noteDao.read(Note.class, note.getId());

        if (noteToUpdate == null) {
            ctx.status(404);
            return;
        }
        note = noteDao.update(note);
        if (note == null) {
            throw new ApiException(500, "Could not update note");
        }
        ctx.status(200);
        ctx.json(note);
    }

    public void delete(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Note note = noteDao.read(Note.class, id);
        if (note == null) {
            ctx.status(404);
            return;
        }
        noteDao.delete(Note.class, id);
        ctx.status(204);
    }

    public void updateNoteGroup(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        int groupId = Integer.parseInt(ctx.pathParam("group-id"));

        Note note = noteDao.read(Note.class, id);
        NoteGroup noteGroup = noteGroupDao.read(NoteGroup.class, groupId);

        if (note == null || noteGroup == null) {
            ctx.status(404);
            return;
        }

        note.setNoteGroup(noteGroup);
        note = noteDao.update(note);

        ctx.status(200);
        ctx.json(note);
    }

    public void readNoteGroup(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));

        Note note = noteDao.read(Note.class, id);

        if (note == null) {
            ctx.status(404);
            return;
        }

        ctx.status(200);
        ctx.json(note.getNoteGroup());
    }
}
