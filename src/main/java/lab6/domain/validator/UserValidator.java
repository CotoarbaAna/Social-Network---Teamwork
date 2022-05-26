package lab6.domain.validator;
import lab6.domain.User;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator implements Validator<User>{
    /**
     * Valideaza obiectul dat
     * @param entity: tipul T
     * @throws ValidationException
     */
    @Override
    public void validate(User entity) throws ValidationException {
        if (entity.getLastName().trim().isEmpty())
            throw new ValidationException("The last name can't be empty!");
        if(entity.getFirstName().trim().isEmpty())
            throw new ValidationException("The first name can't be empty!");
        if(entity.getLastName().length() == 1)
            throw new ValidationException("The last name can't have only one character!");
        if(entity.getFirstName().length() == 1)
            throw new ValidationException("The first name can't have only one character!");


        Pattern pattern = Pattern.compile("[^a-zA-Z]");
        Matcher matcher = pattern.matcher(entity.getLastName());
        if (matcher.find())
            throw new ValidationException("The last name can't contain special characters or numbers!");

        matcher = pattern.matcher(entity.getFirstName());
        if (matcher.find())
            throw new ValidationException("The first name can't contain special characters or numbers!");

    }
}
