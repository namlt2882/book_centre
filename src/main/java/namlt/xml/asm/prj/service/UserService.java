package namlt.xml.asm.prj.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import namlt.xml.asm.prj.common.UserCommon;
import namlt.xml.asm.prj.model.User;
import namlt.xml.asm.prj.repository.UserRepository;

public class UserService implements UserCommon {

    public User register(User user) {
        User rs = null;
        user.setRole(ROLE_USER);
        user.setStatus(STATUS_ACTIVE);
        try {
            rs = new UserRepository().insert(user);
        } catch (Exception ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public User login(String username, String password) {
        User user = null;
        try {
            user = new UserRepository().getByUsernameAndPassword(username, password);
        } catch (Exception ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }
}
