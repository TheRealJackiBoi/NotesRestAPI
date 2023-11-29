package dat3.controller.impl;

import dat3.config.HibernateConfig;
import dat3.dao.impl.NoteDao;
import dat3.dao.impl.NoteGroupDao;
import dat3.dao.impl.UserDao;
import dat3.dto.NoteDto;
import dat3.dto.NoteGroupDto;
import dat3.exception.ApiException;
import dat3.exception.Message;
import dat3.model.Note;
import dat3.model.NoteGroup;
import dat3.model.User;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class NoteGroupController {

    private final NoteGroupDao noteGroupDao;
    private final NoteDao noteDao;
    private final UserDao userDao;

    public NoteGroupController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        noteGroupDao = NoteGroupDao.getInstance(emf);
        noteDao = NoteDao.getInstance(emf);
        userDao = UserDao.getInstance(emf);
    }

    public void readAll(Context ctx) throws ApiException {
        List<NoteGroup> noteGroups = noteGroupDao.readAll(NoteGroup.class);
        if (noteGroups == null) {
            ctx.status(404);
            throw new ApiException(404, "Could not find any note groups");
        }
        ctx.status(200);
        ctx.json(noteGroups);
    }
    public void readAllByUserId(Context ctx) throws ApiException {
        List<NoteGroup> noteGroups = noteGroupDao.readAll(NoteGroup.class).stream().filter(ng -> ng.getUser().getUserEmail().equals(ctx.pathParam("user_id"))).toList();
        if (noteGroups == null) {
            ctx.status(404);
            throw new ApiException(404, "Could not find any note groups");
        }
        ctx.status(200);
        ctx.json(NoteGroupDto.toNoteGroupDtoList(noteGroups));
    }
    public void read(Context ctx) throws ApiException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        NoteGroup noteGroup = noteGroupDao.read(NoteGroup.class, id);
        if (noteGroup == null) {
            ctx.status(404);
            throw new ApiException(404, "Could not find note group with id " + id);
        }
        ctx.status(200);
        ctx.json(new NoteGroupDto(noteGroup));
    }

    public void create(Context ctx) throws ApiException {
        String userEmail = ctx.pathParam("user_id");
        NoteGroupDto noteGroupDto = ctx.bodyAsClass(NoteGroupDto.class);

        User user = userDao.read(User.class, userEmail);
        if (user == null) {
            ctx.status(404);
            throw new ApiException(404, "Could not find user with id "
                    + userEmail
                    + " when trying to create note group");
        }
        NoteGroup noteGroup = new NoteGroup();
        noteGroup.setName(noteGroupDto.getName());
        noteGroup.setUser(user);
        noteGroup = noteGroupDao.create(noteGroup);
        if (noteGroup == null) {
            ctx.status(500);
            throw new ApiException(500, "Could not create note group");
        }
        ctx.status(201);
        ctx.json(new NoteGroupDto(noteGroup));
    }

    public void update(Context ctx) throws ApiException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        NoteGroup noteGroup = ctx.bodyAsClass(NoteGroup.class);
        if (noteGroupDao.read(NoteGroup.class, id) == null) {
            ctx.status(404);
            throw new ApiException(404, "Could not find note group with id " + id);
        }
        noteGroup = noteGroupDao.update(noteGroup);
        if (noteGroup == null) {
            ctx.status(500);
            throw new ApiException(500, "Could not update note group");
        }
        ctx.status(200);
        ctx.json(new NoteGroupDto(noteGroup));
    }

    public void delete(Context ctx) throws ApiException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        NoteGroup noteGroup = noteGroupDao.read(NoteGroup.class, id);
        if (noteGroup == null) {
            ctx.status(404);
            throw new ApiException(404, "Could not find note group with id " + id);
        }
        noteGroup.removeuser(noteGroup.getUser());
        noteGroup.getNotes().forEach(note -> {
                note.removeNoteGroup(noteGroup);
                noteDao.delete(Note.class, note.getId());
        });
        noteGroupDao.update(noteGroup);
        noteGroupDao.delete(NoteGroup.class, id);
        ctx.status(200);
        ctx.json(new Message(200, "Note group with id " + id + " was deleted"));
    }

    public void addNoteToNoteGroup(Context ctx) throws ApiException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        int noteId = Integer.parseInt(ctx.pathParam("note_id"));
        NoteGroup noteGroup = noteGroupDao.read(NoteGroup.class, id);
        Note note = noteDao.read(Note.class, noteId);
        if (noteGroup == null) {
            ctx.status(404);
            throw new ApiException(404, "Could not find note group with id " + id);
        }
        else if (note == null) {
            ctx.status(404);
            throw new ApiException(404, "Could not find note with id " + noteId);
        }
        note.setNoteGroup(noteGroup);
        noteGroupDao.update(noteGroup);
        ctx.status(200);
        ctx.json(new NoteGroupDto(noteGroup));
    }

    public void removeNoteFromNoteGroup(Context ctx) throws ApiException {
        String userEmail = ctx.pathParam("user_id");
        int id = Integer.parseInt(ctx.pathParam("id"));
        int noteId = Integer.parseInt(ctx.pathParam("note_id"));
        NoteGroup noteGroup = noteGroupDao.read(NoteGroup.class, id);
        Note note = noteDao.read(Note.class, noteId);
        if (noteGroup == null || !noteGroup.getUser().getUserEmail().equals(userEmail)) {
            ctx.status(404);
            throw new ApiException(404, "Could not find note group with id " + id + " or matching user " + userEmail);
        }
        else if (note == null) {
            ctx.status(404);
            throw new ApiException(404, "Could not find note with id " + noteId);
        }
        note.removeNoteGroup(noteGroup);
        noteDao.update(note);
        noteGroupDao.update(noteGroup);
        noteDao.delete(Note.class, noteId);
        ctx.status(200);
        ctx.json(new NoteGroupDto(noteGroup));
    }

    public void readAllNotesInNoteGroup(Context ctx) throws ApiException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        List<Note> notes = noteDao.readAllInNoteGroup(id);
        if (notes == null) {
            ctx.status(404);
            throw new ApiException(404, "Could not find note group with id " + id);
        }
        ctx.status(200);
        ctx.json(NoteDto.toNoteDtoList(notes));
    }

    public void createNoteInNoteGroup(Context ctx) throws ApiException {
        int noteGroupId = Integer.parseInt(ctx.pathParam("id"));
        String userEmail = ctx.pathParam("user_id");
        NoteDto noteDto = ctx.bodyAsClass(NoteDto.class);
        NoteGroup noteGroup = noteGroupDao.read(NoteGroup.class, noteGroupId);
        if (noteGroup == null || !noteGroup.getUser().getUserEmail().equals(userEmail)) {
            ctx.status(404);
            throw new ApiException(404, "Could not find note group with id " + noteGroupId + " or a matching user");
        }
        Note note = new Note();
        note.setContent(noteDto.getContent());
        note.setStatus(Note.Status.TODO);
        note.setNoteGroup(noteGroup);
        note = noteDao.create(note);
        if (note == null) {
            ctx.status(500);
            throw new ApiException(500, "Could not create note");
        }
        ctx.status(201);
        ctx.json(new NoteDto(note));
    }

    public void updateNoteInNoteGroup(Context ctx) throws ApiException {
        int noteGroupId = Integer.parseInt(ctx.pathParam("id"));
        String userEmail = ctx.pathParam("user_id");
        int noteId = Integer.parseInt(ctx.pathParam("note_id"));
        NoteDto note = ctx.bodyAsClass(NoteDto.class);
        Note noteToUpdate = noteDao.read(Note.class, noteId);
        if (noteToUpdate == null) {
            ctx.status(404);
            throw new ApiException(404, "Could not find note with id " + noteId);
        }
        noteToUpdate.setContent(note.getContent());
        noteToUpdate.setDueDate(note.getDueDate());
        noteToUpdate.setStatus(Note.Status.valueOf(note.getStatus()));
        noteToUpdate = noteDao.update(noteToUpdate);
        if (noteToUpdate == null) {
            throw new ApiException(500, "Could not update note");
        }
        ctx.status(200);
        ctx.json(new NoteDto(noteToUpdate));
    }
}
