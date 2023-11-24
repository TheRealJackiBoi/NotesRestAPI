package dat3.routes;

import dat3.controller.impl.NoteController;
import dat3.controller.impl.NoteGroupController;
import dat3.security.RouteRoles;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class NoteGroupRoutes {

    private final NoteGroupController noteGroupController = new NoteGroupController();

    protected EndpointGroup getRoutes () {

        return () -> {
            path("/", () -> {
                get("/", noteGroupController::readAllByUserId, RouteRoles.ANYONE);
                post("/", noteGroupController::create, RouteRoles.ANYONE);
                path("/{id}", () -> {
                    get("/", noteGroupController::read);
                    put("/", noteGroupController::update);
                    delete("/", noteGroupController::delete);
                    path("/notes", () -> {
                        get("/", noteGroupController::readAllNotesInNoteGroup);
                        put("/{note_id}", noteGroupController::addNoteToNoteGroup);
                        delete("/{note_id}", noteGroupController::removeNoteFromNoteGroup);
                        post("/", noteGroupController::createNoteInNoteGroup);
                    });
                });
            });

        };
    }
}
