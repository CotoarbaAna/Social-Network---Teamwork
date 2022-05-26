package lab6.repository.db;

import lab6.domain.Friendship;
import lab6.domain.User;
import lab6.domain.validator.Validator;

import lab6.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FriendshipDbRepository implements Repository<Long, Friendship> {
    private String url;
    private String username;
    private String password;
    private Validator<Friendship> validator;

    public FriendshipDbRepository(String url, String username, String password, Validator<Friendship> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
    @Override
    public Friendship findOne(Long aLong) {
        String sql = "select r.* from public.\"Friendships\" r where idfr = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, aLong);
            ResultSet res = ps.executeQuery();

            if (!res.next())
                return null;

            Long idfr = res.getLong("idfr");
            Long idUser1 = res.getLong("id_us1");
            Long idUser2 = res.getLong("id_us2");
            LocalDate date = res.getDate("data_prieteniei").toLocalDate();

            Friendship friendship = new Friendship(idUser1, idUser2,date);
            friendship.setId(idfr);

            return friendship;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select r.* from public.\"Friendships\" r");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long idfr = resultSet.getLong("idfr");
                Long id_User1 = resultSet.getLong("id_us1");
                Long id_User2 = resultSet.getLong("id_us2");
                LocalDate date = resultSet.getDate("data_prieteniei").toLocalDate();

                Friendship friendship = new Friendship(id_User1, id_User2, date);
                friendship.setId(idfr);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Friendship save(Friendship entity) {
        validator.validate(entity);

        String sql = "insert into public.\"Friendships\"(idfr, id_us1, id_us2,data_prieteniei) values (?, ?, ?, ?)";

        if (this.findOne(entity.getId()) != null)
            return this.findOne(entity.getId());

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getId());
            ps.setLong(2, entity.getIdUser1());
            ps.setLong(3, entity.getIdUser2());
            ps.setDate(4, java.sql.Date.valueOf(entity.getDate()));

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Friendship delete(Long aLong) {
        String sql = "delete from public.\"Friendships\" where idfr = ?";

        Friendship fr = this.findOne(aLong);

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, aLong);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fr;
    }

    @Override
    public Friendship update(Friendship entity) {
        validator.validate(entity);
        String sql = "update public.\"Friendships\" set id_us1 = ?, id_us2 = ? where idfr = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(3, entity.getId());
            ps.setLong(1, entity.getIdUser1());
            ps.setLong(2, entity.getIdUser2());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

