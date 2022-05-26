package lab6.repository.file;

import lab6.domain.User;
import lab6.domain.validator.Validator;


import java.util.List;

public class UserFile extends AbstractFileRepository<Long, User> {

    public UserFile(String fileName, Validator<User> validator) {
        super(fileName, validator);
    }

    /**
     *
     * @param attributes: lista de string
     * @return: utilizatorul
     */
    @Override
    public User extractEntity(List<String> attributes) {
        User us = new User(attributes.get(1), attributes.get(2));
        us.setId(Long.parseLong(attributes.get(0)));
        return us;
    }

    /**
     *
     * @param entity: tipul User
     * @return: o entitate ca string
     */
    @Override
    protected String createEntityAsString(User entity) {
        return entity.getId().toString() + ';' + entity.getLastName() + ';' + entity.getFirstName();
    }
}
