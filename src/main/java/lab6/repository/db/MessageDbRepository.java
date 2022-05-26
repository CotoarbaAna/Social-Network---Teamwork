package lab6.repository.db;

import lab6.domain.Message;
import lab6.domain.User;
import lab6.domain.validator.Validator;
import lab6.repository.Repository;
import lab6.repository.db.UserDbRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class MessageDbRepository implements Repository<Long, Message> {
    private String url;
    private String username;
    private String password;

    public MessageDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Message findOne(Long aLong) {
        String sql = "select m.* from public.\"Messages\" m where id_m = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, aLong);
            ResultSet res = ps.executeQuery();

            if (!res.next())
                return null;

            Long id_m = res.getLong("id_m");
            Long to = res.getLong("to_us");
            Long from = res.getLong("from_us");
            String message = res.getString("message_m");
            LocalDate date = res.getDate("data_mesajului").toLocalDate();
            Long reply = res.getLong("reply");


            Message message1 = new Message(from,to,message,date,reply);
            message1.setId(id_m);

            return message1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> messages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select m.* from public.\"Messages\" m order by data_mesajului");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id_m = resultSet.getLong("id_m");
                Long to = resultSet.getLong("to_us");
                Long from = resultSet.getLong("from_us");
                String message = resultSet.getString("message_m");
                LocalDate date = resultSet.getDate("data_mesajului").toLocalDate();
                Long reply = resultSet.getLong("reply");

                Message message1 = new Message(from,to,message,date,reply);
                message1.setId(id_m);
                messages.add(message1);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public Message save(Message entity) {
        String sql = "insert into public.\"Messages\"(id_m, to_us,from_us,message_m,data_mesajului,reply) values (?, ?, ?, ?, ?, ?)";

        if (this.findOne(entity.getId()) != null)
            return this.findOne(entity.getId());

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getId());
            ps.setLong(2, entity.getTo());
            ps.setLong(3, entity.getFrom());
            ps.setString(4, entity.getMessage());
            ps.setDate(5, java.sql.Date.valueOf(entity.getDate()));
            if(entity.getReply() == null)
            {
                ps.setNull(6,0);
            }
            else
                ps.setLong(6, entity.getReply());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message delete(Long aLong) {
        return null;
    }

    @Override
    public Message update(Message entity) {
        return null;
    }
}
