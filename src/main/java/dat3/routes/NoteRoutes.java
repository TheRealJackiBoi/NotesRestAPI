package dat3.routes;

import dat3.controller.impl.NoteController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class NoteRoutes {

    private final NoteController noteController = new NoteController();

    protected EndpointGroup getRoutes() {

        return () -> {
            path("/", () -> {
                get("/", noteController::readAll);
                post("/", noteController::create);
                path("/:id", () -> {
                    get("/", noteController::read);
                    put("/", noteController::update);
                    delete("/", noteController::delete);
                    path("/notegroup", () -> {
                        get("/", noteController::readNoteGroup);
                    });
                });
            });

        };
    }
}
