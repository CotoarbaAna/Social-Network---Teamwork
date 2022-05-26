package lab6.service;

import lab6.domain.*;
import lab6.domain.validator.ValidationException;
import lab6.repository.Repository;
import lab6.repository.db.FriendshipDbRepository;
import lab6.repository.db.UserDbRepository;
import lab6.repository.file.FriendshipFile;
import lab6.repository.file.UserFile;

import java.io.ObjectInputFilter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class Service {
    private Repository<Long, User> userRepo;
    private Repository<Long, Friendship> friendshipRepo;
    private Repository<Long, Message> messageRepo;
    private Repository<Long, FriendRequest> requestRepo;
    private Repository<Long, Username> usernameRepo;
    private Repository<Long, Event> eventRepo;
    private Repository<Long, EventParticipation> epRepo;
    private Network<Long> socialNetwork;

    public Service(Repository<Long, User> userRepo, Repository<Long, Friendship> friendshipRepo, Repository<Long, Message> messageRepo, Repository<Long, FriendRequest> requestRepo,Repository<Long, Username> usernameRepo, Network<Long> socialNetwork) {
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
        this.messageRepo = messageRepo;
        this.requestRepo = requestRepo;
        this.usernameRepo = usernameRepo;
        this.socialNetwork = socialNetwork;
        if (userRepo instanceof UserFile || userRepo instanceof UserDbRepository) {
            for (User us : userRepo.findAll()) {
                socialNetwork.addUser(us.getId());
            }
        }
        if (friendshipRepo instanceof FriendshipFile || friendshipRepo instanceof FriendshipDbRepository) {
            for (Friendship fr : friendshipRepo.findAll()) {
                socialNetwork.addConnection(fr.getIdUser1(), fr.getIdUser2());
            }
        }
    }

    public Service(Repository<Long, User> userRepo, Repository<Long, Friendship> friendshipRepo, Repository<Long, Message> messageRepo, Repository<Long, FriendRequest> requestRepo,Repository<Long, Username> usernameRepo, Repository<Long, Event> eventRepo, Repository<Long, EventParticipation> epRepo, Network<Long> socialNetwork) {
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
        this.messageRepo = messageRepo;
        this.requestRepo = requestRepo;
        this.usernameRepo = usernameRepo;
        this.socialNetwork = socialNetwork;
        this.eventRepo = eventRepo;
        this.epRepo = epRepo;
        if (userRepo instanceof UserFile || userRepo instanceof UserDbRepository) {
            for (User us : userRepo.findAll()) {
                socialNetwork.addUser(us.getId());
            }
        }
        if (friendshipRepo instanceof FriendshipFile || friendshipRepo instanceof FriendshipDbRepository) {
            for (Friendship fr : friendshipRepo.findAll()) {
                socialNetwork.addConnection(fr.getIdUser1(), fr.getIdUser2());
            }
        }
    }

    public void addEvent(Long id, String name, LocalDate date) {
        Event ev = new Event(id, name, date);
        if (eventRepo.save(ev) != null) {
            throw new ValidationException("The event already exists");
        }
    }

    public void removeEvent(Long id) {
        eventRepo.delete(id);
    }

    public Long getAvailableEventId() {
        List<Long> res = StreamSupport.stream(eventRepo.findAll().spliterator(), false)
                .map(x -> x.getId())
                .toList();
        Long i = 1L;
        while (res.contains(i)) {
            i++;
        }

        return i;
    }

    public List<Event> getEvents() {
        return StreamSupport.stream(eventRepo.findAll().spliterator(), false).toList();
    }

    public void addEventParticipation(Long id, Long eId, Long uId) {
        if (!StreamSupport.stream(eventRepo.findAll().spliterator(), false)
                .map(x -> x.getId())
                .toList().contains(eId))
            throw new ValidationException("The event does not exist");
        if (!StreamSupport.stream(userRepo.findAll().spliterator(), false)
                .map(x -> x.getId())
                .toList().contains(eId))
            throw new ValidationException("The user does not exist");
        if (!StreamSupport.stream(epRepo.findAll().spliterator(), false)
                .filter(x -> (x.getEventId() == eId && x.getUserId() == uId))
                .toList().isEmpty())
            throw new ValidationException("User already registered for event");
        EventParticipation ep = new EventParticipation(id, eId, uId);
        if (epRepo.save(ep) != null) {
            throw new ValidationException("Participant already registered to event");
        }
    }

    public void removeEventParticipation(Long eId, Long uId) {
        List<Long> ep = StreamSupport.stream(epRepo.findAll().spliterator(), false)
                .filter(x -> (x.getEventId() == eId && x.getUserId() == uId))
                .map(x -> x.getId())
                .toList();

        for (Long id: ep)
            epRepo.delete(id);
    }

    public List<Event> getEventsForUser(Long uId) {
        List<Event> userEvents = new ArrayList<>();

        List<EventParticipation> eps = StreamSupport.stream(epRepo.findAll().spliterator(), false)
                .filter(x -> (x.getUserId() == uId))
                .toList();

        for (EventParticipation ep: eps) {
            userEvents.add(eventRepo.findOne(ep.getEventId()));
        }
        return userEvents;
    }

    public Long getAvailableEventParticipationId() {
        List<Long> res = StreamSupport.stream(epRepo.findAll().spliterator(), false)
                .map(x -> x.getId())
                .toList();
        Long i = 1L;
        while (res.contains(i)) {
            i++;
        }

        return i;
    }

    /**
     * Adauga un utilizator
     *
     * @param id:tipul        Long
     * @param firstName:tipul String
     * @param lastName:tipul  String
     */
    public void addUser(Long id, String firstName, String lastName) {
        User user = new User(firstName, lastName);
        user.setId(id);
        if (userRepo.save(user) != null)
            throw new ValidationException("The user already exists");
        socialNetwork.addUser(id);
    }

    /**
     * Sterge un utilizator
     *
     * @param id: tipul Long
     */
    public void deleteUser(Long id) {
        Iterable<Friendship> frList = friendshipRepo.findAll();
        Iterator<Friendship> frIter = frList.iterator();
        while (frIter.hasNext()) {
            Friendship fr = frIter.next();
            if (fr.getIdUser1() == id || fr.getIdUser2() == id) {
                frIter.remove();
                friendshipRepo.delete(fr.getId());
            }
        }
        List<Long> frReqList = StreamSupport.stream(requestRepo.findAll().spliterator(), false)
                .filter(x -> x.getId_us1() == id || x.getId_us2() == id)
                .map(x -> x.getId())
                .toList();
        for (Long Id : frReqList) {
            requestRepo.delete(Id);
        }
        if (userRepo.delete(id) == null)
            throw new ValidationException("The user doesn't exists");
        socialNetwork.removeUser(id);
    }

    /**
     * Adauga un prieten
     *
     * @param idFriendship
     * @param id:          tipul Long
     * @param id_2:        tipul Long
     */
    public void addFriend(Long idFriendship, Long id, Long id_2) {
        if (userRepo.findOne(id) == null)
            throw new ValidationException("The first user doesn't exist");
        if (userRepo.findOne(id_2) == null)
            throw new ValidationException("The second user doesn't exist");

        Friendship fr = new Friendship(id, id_2, LocalDate.now());
        fr.setId(idFriendship);
        if (friendshipRepo.save(fr) != null)
            throw new ValidationException("The friendship already exists");
        socialNetwork.addConnection(id, id_2);
    }

    /**
     * Sterge un prieten
     *
     * @param id: tipul Long
     */
    public void deleteFriend(Long id) {
        long id_1 = friendshipRepo.findOne(id).getIdUser1();
        long id_2 = friendshipRepo.findOne(id).getIdUser2();

        Friendship fr = friendshipRepo.findOne(id);

        List<Long> res = StreamSupport.stream(requestRepo.findAll().spliterator(), false)
                .filter(x -> (x.getId_us1() == fr.getIdUser1() && x.getId_us2() == fr.getIdUser2()))
                .map(x -> x.getId())
                .toList();

        for (Long Id : res) {
            requestRepo.delete(Id);
        }

        if (friendshipRepo.delete(id) == null)
            throw new ValidationException("The friendship doesn't exist");
        socialNetwork.removeConnection(id_1, id_2);
    }

    /**
     * Returneaza  o lista cu toti prietenii user-ului dat
     *
     * @param id: tipul Long, id-ul user-ului al carui prieteni ii returnam
     * @return: res- lista de obiecte de tipul FriendshipDto
     */
    public List<FriendshipDto> showUserFriends(Long id) {
        List<FriendshipDto> res = StreamSupport.stream(friendshipRepo.findAll().spliterator(), false)
                .filter(x -> x.getIdUser1() == id || x.getIdUser2() == id)
                .map(x -> {
                    if (x.getIdUser1() == id)
                        return new FriendshipDto(userRepo.findOne(x.getIdUser2()).getLastName(),
                                userRepo.findOne(x.getIdUser2()).getFirstName(),
                                x.getDate());
                    else
                        return new FriendshipDto(userRepo.findOne(x.getIdUser1()).getLastName(),
                                userRepo.findOne(x.getIdUser1()).getFirstName(),
                                x.getDate());
                })
                .toList();
        return res;
    }
    /**
     * Finds the list of friends of a specific user
     * @param id : Long
     * @return List<Username>
     */
    public List<Username> getFirendsFor(Long id) {
        List<Username> res = StreamSupport.stream(friendshipRepo.findAll().spliterator(), false)
                .filter(x -> (x.getIdUser1() == id || x.getIdUser2() == id))
                .map(x -> {
                    if (x.getIdUser1() == id)
                        return usernameRepo.findOne(x.getIdUser2());
                    else
                        return usernameRepo.findOne(x.getIdUser1());
                })
                .toList();
        return res;
    }
    /**
     * Finds the list of friendships made in a specific month for a user
     * @param id : Long
     * @param month : Int
     * @return List<FriendshipDto>
     */
    public List<FriendshipDto> showUserFriendsMonth(Long id, int month) {
        List<FriendshipDto> res = showUserFriends(id);
        return res.stream()
                .filter(x -> x.getDate().getMonthValue() == month)
                .toList();
    }
    /**
     * Find the next available Id for a new FriendRequest
     * @return Integer which is an unused id in FriendRequests table
     */
    public Long getAvailableFriendRequestId() {
        List<Long> res = StreamSupport.stream(requestRepo.findAll().spliterator(), false)
                .map(x -> x.getId())
                .toList();
        Long i = 1L;
        while (res.contains(i)) {
            i++;
        }

        return i;
    }
    /**
     * Finds the list of FriendRequests which have PENDING as status
     * @param id : Long
     * @return List<FriendRequest>
     */
    public List<FriendRequest> getPendingFriendRequests(Long id) {
        List<FriendRequest> res = StreamSupport.stream(requestRepo.findAll().spliterator(), false)
                .filter(x -> x.getId_us2() == id && x.getStatus() == RequestStatus.PENDING)
                .toList();
        return res;
    }
    /**
     * Finds the list of FriendRequests which have APPROVED as status
     * @param id : Long
     * @return List<FriendRequest>
     */
    public List<FriendRequest> getApprovedFriendRequests(Long id) {
        List<FriendRequest> res = StreamSupport.stream(requestRepo.findAll().spliterator(), false)
                .filter(x -> x.getId_us2() == id  && x.getStatus() == RequestStatus.APPROVED)
                .toList();
        return res;
    }

    /**
     * Finds the list of FriendRequests which have REJECTED as status
     * @param id : Long
     * @return List<FriendRequest>
     */
    public List<FriendRequest> getRejectedFriendRequests(Long id) {
        List<FriendRequest> res = StreamSupport.stream(requestRepo.findAll().spliterator(), false)
                .filter(x -> x.getId_us2() == id && x.getStatus() == RequestStatus.REJECTED)
                .toList();
        return res;
    }

    /**
     * Finds the list of FriendRequests which have PENDING as status
     * (The difference from getPendingFriendRequests function is that
     * this one filters 'x' by getId_us1)
     * @param id : Long
     * @return List<FriendRequest>
     */
    public List<FriendRequest> getSentFriendRequests(Long id) {
        List<FriendRequest> res = StreamSupport.stream(requestRepo.findAll().spliterator(), false)
                .filter(x -> x.getId_us1() == id && x.getStatus() == RequestStatus.PENDING)
                .toList();
        return res;
    }

    /**
     * Deletes a friend request
     * @param id : Long
     * @param id_del : Long
     */
    public void deleteSentFriendRequest(Long id, Long id_del)
    {
        List<FriendRequest> list = getSentFriendRequests(id);
        for(FriendRequest fr : list)
            if(fr.getId_us2().equals(id_del))
                requestRepo.delete(fr.getId());
    }
    /**
     * Sends a friend request
     * @param idRequest : Long
     * @param id_us1 : Long
     * @param id_us2 : Long
     */
    public void sendFriendRequest(Long idRequest, Long id_us1, Long id_us2) {
        if (userRepo.findOne(id_us1) == null)
            throw new ValidationException("The first user doesn't exist");
        if (userRepo.findOne(id_us2) == null)
            throw new ValidationException("The second user doesn't exist");

        FriendRequest request = new FriendRequest(id_us1, id_us2, LocalDate.now());
        request.setId(idRequest);

        List<FriendRequest> res = StreamSupport.stream(requestRepo.findAll().spliterator(), false)
                .filter(x -> (x.getId_us1() == id_us1 && x.getId_us2() == id_us2 && x.getStatus() != RequestStatus.REJECTED))
                .toList();

        if (!res.isEmpty())
            throw new ValidationException("The friend request already exists");

        if (requestRepo.save(request) != null)
            throw new ValidationException("The friendship already exists");
    }

    /**
     * Accepts a friend request
     * @param idRequest : Long
     */
    public void acceptFriendRequest(Long idRequest) {
        FriendRequest request = requestRepo.findOne(idRequest);
        if (request == null)
            throw new ValidationException("The request doesn't exist!");

        request.setStatus(RequestStatus.APPROVED);
        this.addFriend(idRequest, request.getId_us1(), request.getId_us2());
        requestRepo.update(request);
    }
    /**
     * Rejects a friend request
     * @param idRequest : Long
     */
    public void rejectFriendRequest(Long idRequest) {
        FriendRequest request = requestRepo.findOne(idRequest);
        if (request == null)
            throw new ValidationException("The request doesn't exist!");

        request.setStatus(RequestStatus.REJECTED);
        requestRepo.update(request);
    }

    /**
     * Afla numarul comunitatilor
     *
     * @return: numarul comunitatilor
     */
    public int nrComunitati() {
        return socialNetwork.numberOfCommunities();
    }

    /**
     * Gets all users in table Users
     * @return Iterable<User>
     */

    public Iterable<User> getUsers() {
        return userRepo.findAll();
    }

    /**
     * Gets all friendships in table Friendships
     * @return Iterable<Friendship>
     */
    public Iterable<Friendship> getFriendships() {
        return friendshipRepo.findAll();
    }

    /**
     * Gets all messages in table Messages
     * @return Iterable<Message>
     */
    public Iterable<Message> getMessages() {
        return messageRepo.findAll();
    }

    /**
     * Gets all usernames in table Login
     * @return Iterable<Username>
     */
    public Iterable<Username> getUsernames() { return usernameRepo.findAll(); }

    /**
     * Finds the id of a user
     * @param username : String
     * @return id as Long
     */
    public Long findUsername(String username) {
        for(Username username1: usernameRepo.findAll())
            if(username1.getUsername().equals(username))
                return username1.getId();
        return null;
    }
    /**
     * Finds a username in table Username
     * @param id : Long
     * @return username as String
     */
    public String findUsernameById(Long id) {
        return usernameRepo.findOne(id).getUsername();
    }
    /**
     * Finds all messages of two specific users
     * @param us1 : Long
     * @param us2 : Long
     * @return List of messages as List<Message>
     */
    public List<Message> findUserMessages(Long us1, Long us2) {
        List<Message> res = StreamSupport.stream(messageRepo.findAll().spliterator(), false)
                .filter(x -> ((x.getTo() == us1 && x.getFrom() == us2) || (x.getTo() == us2 && x.getFrom() == us1)))
                .toList();
        return res;
    }
    /**
     * Finds the username by given id
     * @param id : Long
     * @return Username
     */
    public Username findUsernameFromUser(Long id) {
        for(Username username: usernameRepo.findAll())
            if(username.getId().equals(id))
                return username;
        return null;
    }
    /**
     * Finds the User by given name
     * @param nume : String
     * @param prenume : String
     * @return User
     */
    public User findUserFromNume(String nume, String prenume) {
        for(User us: userRepo.findAll())
            if(us.getLastName().equals(nume)&&us.getFirstName().equals(prenume))
                return us;
        return null;
    }
    /**
     * Finds a friendship by given user's id's
     * @param id1 : Long
     * @param id2 : Long
     * @return Friendship
     */
    public Friendship findFriendship(Long id1, Long id2) {
        for(Friendship fr: friendshipRepo.findAll())
            if(fr.getIdUser1().equals(id1)&&fr.getIdUser2().equals(id2))
                return fr;
            else
            if(fr.getIdUser1().equals(id2)&&fr.getIdUser2().equals(id1))
                return fr;
        return null;
    }
    /**
     * Gets the available id for table Message
     * @return available id as Long
     */
    public Long getAvailableMessageId() {
        List<Long> res = StreamSupport.stream(messageRepo.findAll().spliterator(), false)
                .map(x -> x.getId())
                .toList();
        Long i = 1L;
        while (res.contains(i)) {
            i++;
        }

        return i;
    }
    /**
     * Adds a message in Messages table
     * @param id_m : Long
     * @param to : Long
     * @param from : Long
     * @param message : String
     */
    public void addMessage(Long id_m, Long to, Long from, String message) {
        Long reply = null;
        Integer ok = 0;
        Friendship fr = new Friendship(to, from, LocalDate.now());
        fr.setId(Long.parseLong("1"));
        if (friendshipRepo.save(fr) == null)
            throw new ValidationException("The users aren't friends!");
        else {
            for (Message m : messageRepo.findAll())
                if (m.getFrom().equals(from) && m.getTo().equals(to)) {
                    Message ms1 = new Message(from, to, message, LocalDate.now(), to);
                    ms1.setId(id_m);
                    ok = 1;
                    messageRepo.save(ms1);
                }

            if (ok == 0) {
                Message ms2 = new Message(from, to, message, LocalDate.now(), reply);
                ms2.setId(id_m);
                messageRepo.save(ms2);

            }

        }

    }

}
