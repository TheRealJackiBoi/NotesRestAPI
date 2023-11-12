package dat3.dto;

import dat3.model.NoteGroup;
import dat3.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@ToString
@NoArgsConstructor
public class UserDTO {

    private String username;
    private Set<String> roles;
    private List<NoteGroupDto> noteGroups;

    public UserDTO(String username, String[] roles, List<NoteGroupDto> noteGroups) {
        this.username = username;
        this.roles = Set.of(roles);
        this.noteGroups = noteGroups;
    }

    public UserDTO(User user) {
        this.username = user.getUserEmail();
        this.roles = user.getRolesAsStrings();
        this.noteGroups = NoteGroupDto.toNoteGroupDtoList(user.getNoteGroups().stream().toList());
    }

    public static List<UserDTO> toUserDTOList(List<User> users) {
        List<UserDTO> userDTOList =  new ArrayList<>();
        for (User user : users) {
            userDTOList.add(new UserDTO(user.getUserEmail(),
                    user.getRolesAsStrings().toArray(new String[0]),
                    NoteGroupDto.toNoteGroupDtoList(user.getNoteGroups().stream().toList())));
        }
        return userDTOList;

    }
}