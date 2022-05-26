package lab6.repository.db;

import lab6.domain.FriendRequest;
import lab6.domain.Friendship;
import lab6.domain.RequestStatus;
import lab6.domain.validator.Validator;
import lab6.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FriendRequestDbRepository implements Repository<Long, FriendRequest> {
    private String url;
    private String username;
    private String password;
    private Map<String, RequestStatus> statusMap;

    public FriendRequestDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.statusMap = new HashMap<>();
        statusMap.put("PENDING", RequestStatus.PENDING);
        statusMap.put("APPROVED", RequestStatus.APPROVED);
        statusMap.put("REJECTED", RequestStatus.REJECTED);
    }

    @Override
    public FriendRequest findOne(Long aLong) {
        String sql = "select s.* from public.\"FriendRequests\" s where idr = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, aLong);
            ResultSet res = ps.executeQuery();

            if (!res.next())
                return null;

            Long idfr = res.getLong("idr");
            Long idUser1 = res.getLong("id_us1");
            Long idUser2 = res.getLong("id_us2");
            String status = res.getString("status");
            LocalDate data = res.getDate("data_request").toLocalDate();
            RequestStatus stat = statusMap.get(status);

            FriendRequest friendship = new FriendRequest(idUser1, idUser2, data, stat);
            friendship.setId(idfr);

            return friendship;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<FriendRequest> findAll() {
        Set<FriendRequest> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select s.* from public.\"FriendRequests\" s");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long idfr = resultSet.getLong("idr");
                Long id_User1 = resultSet.getLong("id_us1");
                Long id_User2 = resultSet.getLong("id_us2");
                String status = resultSet.getString("status");
                LocalDate data = resultSet.getDate("data_request").toLocalDate();
                RequestStatus stat = statusMap.get(status);

                FriendRequest friendship = new FriendRequest(id_User1, id_User2, data, stat);
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
    public FriendRequest save(FriendRequest entity) {
        String sql = "insert into public.\"FriendRequests\"(idr, id_us1, id_us2,status,data_request) values (?, ?, ?, ?, ?)";

        if (this.findOne(entity.getId()) != null)
            return this.findOne(entity.getId());

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getId());
            ps.setLong(2, entity.getId_us1());
            ps.setLong(3, entity.getId_us2());
            ps.setString(4, entity.getStatus().toString());
            ps.setDate(5, java.sql.Date.valueOf(entity.getData()));

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public FriendRequest delete(Long aLong) {
        String sql = "delete from public.\"FriendRequests\" where idr = ?";

        FriendRequest fr = this.findOne(aLong);

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
    public FriendRequest update(FriendRequest entity) {
        String sql = "update public.\"FriendRequests\" set id_us1 = ?, id_us2 = ?, status = ?, data_request = ? where idr = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(5, entity.getId());
            ps.setLong(1, entity.getId_us1());
            ps.setLong(2, entity.getId_us2());
            ps.setString(3, entity.getStatus().toString());
            ps.setDate(4, java.sql.Date.valueOf(entity.getData()));

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
