package lab6.domain.validator;

public interface Validator<T> {
    /**
     *Valideaza un obiect
     * @param entity: tipul T
     * @throws ValidationException
     */
    void validate(T entity) throws ValidationException;
}
