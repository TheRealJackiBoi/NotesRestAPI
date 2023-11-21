package dat3.dto;

import dat3.model.Note;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NoteDto {

    private int id;
    private String content;
    private LocalDate dueDate;
    private String status;
    private int noteGroupId;

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
