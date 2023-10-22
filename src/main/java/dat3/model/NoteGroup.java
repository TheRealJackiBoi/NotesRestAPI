package dat3.model;

import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GeneratedColumn;

import java.util.Set;

@Entity
@Table(name = "note_group")
@Getter
@NoArgsConstructor
public class NoteGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "noteGroup")
    private Set<Note> notes;

    public void setUser(User user) {
        this.user = user;
        user.addNoteGroup(this);
    }

    public void removeuser(User user) {
        this.user = null;
        user.removeNoteGroup(this);
    }

    public void addNote (Note note) {
        notes.add(note);
    }

    public void removeNote (Note note) {
        notes.remove(note);
    }
}