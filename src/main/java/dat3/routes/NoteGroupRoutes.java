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
                get("/", noteGroupController::readAllByUserId, RouteRoles.USER, RouteRoles.ADMIN);
                post("/", noteGroupController::create, RouteRoles.USER, RouteRoles.ADMIN);
                path("/{id}", () -> {
                    get("/", noteGroupController::read, RouteRoles.USER, RouteRoles.ADMIN);
                    put("/", noteGroupController::update, RouteRoles.USER, RouteRoles.ADMIN);
                    delete("/", noteGroupController::delete, RouteRoles.USER, RouteRoles.ADMIN);
                    path("/notes", () -> {
                        get("/", noteGroupController::readAllNotesInNoteGroup, RouteRoles.USER, RouteRoles.ADMIN);
                        delete("/", noteGroupController::removeAllNotesFromNoteGroup, RouteRoles.USER, RouteRoles.ADMIN);
                        put("/{note_id}", noteGroupController::updateNoteInNoteGroup, RouteRoles.USER, RouteRoles.ADMIN);
                        delete("/{note_id}", noteGroupController::removeNoteFromNoteGroup, RouteRoles.USER, RouteRoles.ADMIN);
                        post("/", noteGroupController::createNoteInNoteGroup, RouteRoles.USER, RouteRoles.ADMIN);
                    });
                });
            });

        };
    }
}
