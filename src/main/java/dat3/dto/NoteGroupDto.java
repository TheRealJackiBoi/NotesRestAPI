package dat3.dto;

import dat3.model.NoteGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class NoteGroupDto {

    private int id;
    private List<NoteDto> notes;
    private String name;
    private String userEmail;

    public NoteGroupDto(int id, List<NoteDto> notes, String name, String userEmail) {
        this.id = id;
        this.notes = notes;
        this.name = name;
        this.userEmail = userEmail;
    }

    public NoteGroupDto(NoteGroup noteGroup) {
        this.id = noteGroup.getId();
        this.notes = NoteDto.toNoteDtoList(noteGroup.getNotes().stream().toList());
        this.name = noteGroup.getName();
        this.userEmail = noteGroup.getUser().getUserEmail();
    }

    public static List<NoteGroupDto> toNoteGroupDtoList(List<NoteGroup> noteGroups) {
        return noteGroups.stream().map(NoteGroupDto::new).toList();
    }
}
