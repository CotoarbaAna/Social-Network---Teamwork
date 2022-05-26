package lab6.repository.db;

import lab6.domain.Event;
import lab6.domain.User;
import lab6.domain.validator.Validator;
import lab6.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class EventsDbRepository implements Repository<Long, Event> {
    private String url;
    private String username;
    private String password;

    public EventsDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Event findOne(Long aLong) {
        String sql = "select t.* from public.\"Events\" t where eventId = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, aLong);
            ResultSet res = ps.executeQuery();

            if (!res.next())
                return null;

            Long id = res.getLong("eventId");
            String name = res.getString("eName");
            LocalDate date = res.getDate("eDate").toLocalDate();

            Event event = new Event(id, name, date);

            return event;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Event> findAll() {
        Set<Event> events = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select t.* from public.\"Events\" t");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("eventId");
                String name = resultSet.getString("eName");
                LocalDate date = resultSet.getDate("eDate").toLocalDate();

                Event event = new Event(id, name, date);
                events.add(event);
            }
            return events;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    @Override
    public Event save(Event entity) {
        String sql = "insert into public.\"Events\"(eventId, eName, eDate) values (?, ?, ?)";

        if (this.findOne(entity.getId()) != null)
            return this.findOne(entity.getId());

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getId());
            ps.setString(2, entity.getName());
            ps.setDate(3, java.sql.Date.valueOf(entity.getDate()));

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Event delete(Long aLong) {
        String sql = "delete from public.\"Events\" where eventId = ?";

        Event e = this.findOne(aLong);

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, aLong);

            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return e;
    }

    @Override
    public Event update(Event entity) {
        String sql = "update public.\"Events\" set eName = ?, eDate = ? where eventId = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(3, entity.getId());
            ps.setString(1, entity.getName());
            ps.setDate(2, java.sql.Date.valueOf(entity.getDate()));

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
