package org.fedran.manager.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.fedran.manager.service.UserService;

@ShellComponent
public class UserCommands {

    private final UserService service;

    public UserCommands(UserService service) {
        this.service = service;
    }

    @ShellMethod("Create user")
    public String createUser(final String name) {
        return service.create(name).getName() + " created";
    }

    @ShellMethod("Find user")
    public String findUser(@ShellOption(defaultValue="") final String name) {
        var users = service.findByNameLike(name);
        var sb = new StringBuilder();
        for (int i = 0; i < users.size(); i++) {
            var user = users.get(i);
            sb.append(i + 1);
            sb.append(". ");
            sb.append(user.getName());
            sb.append("\n");
        }
        return sb.toString().trim();
    }

    @ShellMethod("Delete user")
    public String deleteUser(final String name) {
        service.delete(name);
        return name + " was successfully deleted";
    }
}
