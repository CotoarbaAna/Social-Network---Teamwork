package lab6.repository.file;

import lab6.domain.Entity;
import lab6.domain.validator.Validator;

import lab6.repository.memory.InMemoryRepository;


import java.io.*;

import java.util.Arrays;
import java.util.List;



public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {
    String fileName;
    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName = fileName;
        this.loadData();
    }

    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String linie;
            while ((linie = br.readLine()) != null && !linie.equals("")) {
                List<String> atributes = Arrays.asList(linie.split(";"));
                E e = extractEntity(atributes);
                super.save(e);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  extract entity  - template method design pattern
     *  creates an entity of type E having a specified list of @code attributes
     * @param attributes
     * @return an entity of type E
     */
    protected abstract E extractEntity(List<String> attributes);
    ///Observatie-Sugestie: in locul metodei template extractEntity, puteti avea un factory pr crearea instantelor entity

    protected abstract String createEntityAsString(E entity);

    @Override
    public E save(E entity){
        if(super.save(entity) != null)
            return entity;
        writeToFile(entity);
        return null;
    }

    @Override
    public E delete(ID id) {
        E entity = super.delete(id);
        writeEntities();
        return entity;
    }

    @Override
    public E update(E entity) {
        if (super.update(entity) != null)
            return entity;
        writeEntities();
        return null;
    }

    private void writeEntities(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileName,false))){
            for (E c: findAll()){
                bw.write(createEntityAsString(c));
                bw.newLine();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    protected void writeToFile(E entity){
        try(BufferedWriter bw= new BufferedWriter(new FileWriter(fileName, true))){
            bw.write(createEntityAsString(entity));
            bw.newLine();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}




