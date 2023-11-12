package dat3.dto;

import dat3.model.Note;

import java.time.LocalDate;
import java.util.List;

public class NoteDto {

    private int id;
    private String content;
    private LocalDate dueDate;
    private String status;
    private int noteGroupId;

    public NoteDto(int id, String content, LocalDate dueDate, String status, int noteGroupId) {
        this.id = id;
        this.content = content;
        this.dueDate = dueDate;
        this.status = status;
        this.noteGroupId = noteGroupId;
    }

    public NoteDto(Note note) {
        this.id = note.getId();
        this.content = note.getContent();
        this.dueDate = note.getDueDate();
        this.status = note.getStatus().toString();
        this.noteGroupId = note.getNoteGroup().getId();
    }

    public static List<NoteDto> toNoteDtoList(List<Note> notes) {
        return notes.stream().map(NoteDto::new).toList();
    }
}
