package lab6.repository.db;

import lab6.domain.User;
import lab6.domain.Username;
import lab6.repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UsernameDbRepository implements Repository<Long, Username> {

    private String url;
    private String username;
    private String password;

    public UsernameDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Username findOne(Long aLong) {
        String sql = "select z.* from public.\"Login\" z where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, aLong);
            ResultSet res = ps.executeQuery();

            if (!res.next())
                return null;

            Long id = res.getLong("id");
            String username = res.getString("username");

            Username username1= new Username(username);
            username1.setId(id);

            return username1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Username> findAll() {
        Set<Username> usernames = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select z.* from public.\"Login\" z");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String username = resultSet.getString("username");

                Username username1 = new Username(username);
                username1.setId(id);
                usernames.add(username1);
            }
            return usernames;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usernames;
    }

    @Override
    public Username save(Username entity) {

        String sql = "insert into public.\"Login\"(id, username) values (?, ?)";

        if (this.findOne(entity.getId()) != null)
            return this.findOne(entity.getId());

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getId());
            ps.setString(2, entity.getUsername());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Username delete(Long aLong) {
        String sql = "delete from public.\"Login\" where id = ?";

        Username us = this.findOne(aLong);

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, aLong);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return us;
    }

    @Override
    public Username update(Username entity) {
        String sql = "update public.\"Login\" set username= ? where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(3, entity.getId());
            ps.setString(1, entity.getUsername());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
