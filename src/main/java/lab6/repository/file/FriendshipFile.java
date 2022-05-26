package lab6.repository.file;

import lab6.domain.Friendship;
import lab6.domain.User;
import lab6.domain.validator.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FriendshipFile extends AbstractFileRepository<Long, Friendship> {

    public FriendshipFile(String fileName, Validator<Friendship> validator) {
        super(fileName, validator);
    }

    /**
     *
     * @param attributes: lista de string
     * @return: entitatea
     */
    @Override
    public Friendship extractEntity(List<String> attributes) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        Friendship fr = new Friendship(Long.parseLong(attributes.get(1)), Long.parseLong(attributes.get(2)), LocalDate.parse(attributes.get(3), formatter));
        fr.setId(Long.parseLong(attributes.get(0)));
        return fr;
    }

    /**
     *
     * @param entity: tipul Friendship
     * @return: o entitate ca String
     */
    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getId().toString() + ';' + entity.getIdUser1() + ';' + entity.getIdUser2();
    }
}

