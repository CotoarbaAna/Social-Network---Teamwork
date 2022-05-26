package lab6.repository.db;

import lab6.domain.Event;
import lab6.domain.EventParticipation;
import lab6.domain.User;
import lab6.domain.validator.Validator;
import lab6.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class EventParticipationDbRepository implements Repository<Long, EventParticipation> {
    private String url;
    private String username;
    private String password;

    public EventParticipationDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public EventParticipation findOne(Long aLong) {
        String sql = "select t.* from public.\"EventParticipants\" t where epId = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, aLong);
            ResultSet res = ps.executeQuery();

            if (!res.next())
                return null;

            Long id = res.getLong("epId");
            Long event = res.getLong("eventId");
            Long user = res.getLong("userId");

            EventParticipation ep = new EventParticipation(id, event, user);

            return ep;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<EventParticipation> findAll() {
        Set<EventParticipation> eps = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select t.* from public.\"EventParticipants\" t");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("epId");
                Long event = resultSet.getLong("eventId");
                Long user = resultSet.getLong("userId");

                EventParticipation ep = new EventParticipation(id, event, user);
                eps.add(ep);
            }
            return eps;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eps;
    }

    @Override
    public EventParticipation save(EventParticipation entity) {
        String sql = "insert into public.\"EventParticipants\"(epId, eventId, userId) values (?, ?, ?)";

        if (this.findOne(entity.getId()) != null)
            return this.findOne(entity.getId());

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getId());
            ps.setLong(2, entity.getEventId());
            ps.setLong(3, entity.getUserId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public EventParticipation delete(Long aLong) {
        String sql = "delete from public.\"EventParticipants\" where epId = ?";

        EventParticipation ep = this.findOne(aLong);

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, aLong);

            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ep;
    }

    @Override
    public EventParticipation update(EventParticipation entity) {
        String sql = "update public.\"EventParticipants\" set eventId = ?, userId = ? where epId = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(3, entity.getId());
            ps.setLong(1, entity.getEventId());
            ps.setLong(2, entity.getUserId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
