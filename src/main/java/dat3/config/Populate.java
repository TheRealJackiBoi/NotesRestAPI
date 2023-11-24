package dat3.config;

import dat3.dao.impl.UserDao;
import dat3.model.Role;
import dat3.model.User;
import dat3.security.RouteRoles;

public class Populate {

    public static void main(String[] args) {
        UserDao userDao = UserDao.getInstance(HibernateConfig.getEntityManagerFactory());

        User user = new User("email@goo.com", "pw");


        Role userRole = userDao.createRole("user");
        Role adminRole = userDao.createRole("admin");
        Role managerRole = userDao.createRole("manager");

        user.addRole(new Role("user"));
        user.addRole(new Role("admin"));

        userDao.create(user);
    }
}
