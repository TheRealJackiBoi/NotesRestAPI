package dat3.routes;

import dat3.controller.impl.UserController;
import dat3.security.RouteRoles;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class UserRoutes {
    private final UserController userController = new UserController();

    private final NoteGroupRoutes noteGroupRoutes = new NoteGroupRoutes();
    protected EndpointGroup getRoutes() {

        return () -> {
            path("/auth", () -> {
                post("/login", userController::login, RouteRoles.ANYONE);
                post("/register", userController::register, RouteRoles.ANYONE);
            });
            path("/users", () -> {
                path("/{user_id}", () -> {
                    path("/note_groups", noteGroupRoutes.getRoutes());
                });
            });
        };
    }
}