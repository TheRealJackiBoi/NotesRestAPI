package dat3.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "note")
@Getter
@NoArgsConstructor
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "duedate")
    @Temporal(TemporalType.DATE)
    private LocalDate dueDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    private NoteGroup noteGroup;

    public void setNoteGroup(NoteGroup noteGroup) {
        this.noteGroup = noteGroup;
        noteGroup.addNote(this);
    }

    public void removeNoteGroup(NoteGroup noteGroup) {
        this.noteGroup = null;
        noteGroup.removeNote(this);
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public enum Status {
        TODO("todo"),
        DOING("doing"),
        COMPLETED("completed");

        private final String status;

        Status(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return status;
        }
    }

}