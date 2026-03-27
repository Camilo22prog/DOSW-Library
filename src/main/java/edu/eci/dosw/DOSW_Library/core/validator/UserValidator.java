package edu.eci.dosw.DOSW_Library.core.validator;

import edu.eci.dosw.DOSW_Library.core.model.User;
import edu.eci.dosw.DOSW_Library.core.util.ValidationUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserValidator {

    public void validate(User user) {
        ValidationUtil.requireNonNull(user, "user");
        ValidationUtil.requireNonBlank(user.getId(), "id");
        ValidationUtil.requireNonBlank(user.getName(), "name");
        ValidationUtil.requireNonBlank(user.getUsername(), "username");
        ValidationUtil.requireNonBlank(user.getPassword(), "password");
    }
}