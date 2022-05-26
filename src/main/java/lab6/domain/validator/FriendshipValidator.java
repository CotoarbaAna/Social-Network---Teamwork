package lab6.domain.validator;

import lab6.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship>{
    /**
     * Valideaza obiectul dat
     * @param entity: tipul T
     * @throws ValidationException
     */
    @Override
    public void validate(Friendship entity) throws ValidationException {
        if(entity.getIdUser1() == null)
            throw new ValidationException("The id can't be null");
        if(entity.getIdUser2() == null)
            throw new ValidationException("The id can't be null");
        if(entity.getIdUser1() == entity.getIdUser2())
            throw new ValidationException("The user can't be friend with himself!");

    }
}
