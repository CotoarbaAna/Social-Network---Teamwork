package lab6.ui;

import lab6.domain.*;
import lab6.domain.validator.ValidationException;
import lab6.service.Service;

import java.util.List;
import java.util.Scanner;

public class Meniu {
    private Service serv;

    public String meniu ="1.Add user\n" +
            "2.Remove user\n" +
            "3.Add friend\n" +
            "4.Remove friend\n" +
            "5.Display the number of communities\n" +
            "6.Show users\n"+
            "7.Show friendships\n" +
            "8.Show user's friends\n" +
            "9.Show user's friends made in this month\n"+
            "10.Send message to one\n" +
            "11.Send message to group\n"+
            "12.Show messages\n" +
            "13.Send a friend request\n"+
            "14.Manage friend requests\n" +
            "0.Exit\n" +
            ">> ";

    public Meniu(Service serv) {
        this.serv = serv;
    }

    public void addUserUi(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the user id: ");
        String id = scanner.nextLine();
        System.out.print("Enter the user first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter the user last name: ");
        String lastName = scanner.nextLine();
        serv.addUser(Long.parseLong(id),firstName,lastName);
    }

    public void deleteUserUi(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the user id: ");
        Long id = scanner.nextLong();
        serv.deleteUser(id);
    }

    public void addFriendUi(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the friendship id: ");
        Long idf = scanner.nextLong();
        System.out.print("Enter the first user id: ");
        Long id = scanner.nextLong();
        System.out.print("Enter the second user id: ");
        Long id_2 = scanner.nextLong();
        serv.addFriend(idf,id, id_2);
    }

    public void deleteFriendUi(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the friendship id: ");
        String idf = scanner.nextLine();
        serv.deleteFriend(Long.parseLong(idf));
    }

    public void showUsersUi(){
        for(User us:serv.getUsers())
            System.out.println(us);
    }

    public void showFriendshipsUi(){
        for(Friendship fr:serv.getFriendships())
            System.out.println(fr);
    }

    public void showMessagesUI() {
        for(Message ms:serv.getMessages())
            System.out.println(ms);
    }

    public void showUserFriendsUI(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the user id: ");
        String id_us = scanner.nextLine();
        List<FriendshipDto> list = serv.showUserFriends(Long.parseLong(id_us));
        System.out.println(list.toString());
    }

    public void showUserFriendsMonthUI(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the user id: ");
        String id_us = scanner.nextLine();
        System.out.println("Enter the month: ");
        String month = scanner.nextLine();
        List<FriendshipDto> list = serv.showUserFriendsMonth(Long.parseLong(id_us), Integer.parseInt(month));
        System.out.println(list.toString());

    }

    public void AddMessageToOne() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter message id: ");
        String id_m = scanner.nextLine();
        System.out.println("Message to(ID): ");
        String to = scanner.nextLine();
        System.out.println("Message from(ID): ");
        String from = scanner.nextLine();
        System.out.println("Message: ");
        String message = scanner.nextLine();
        serv.addMessage(Long.parseLong(id_m),Long.parseLong(to),Long.parseLong(from),message);
    }

    public void AddMessageToGroup() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter message id: ");
        String id_m = scanner.nextLine();
        System.out.println("Message from(ID): ");
        String from = scanner.nextLine();
        System.out.println("Message: ");
        String message = scanner.nextLine();
        System.out.println("How many people should receive the message? > ");
        String nr = scanner.nextLine();
        Long numar = Long.parseLong(nr);
        Long id = Long.parseLong(id_m);
        while(numar!=0) {
            System.out.println("Message to(ID): ");
            String to = scanner.nextLine();
            serv.addMessage(id,Long.parseLong(to),Long.parseLong(from),message);
            numar=numar-1;
            id=id+1;
        }

    }

    public void sendFriendRequestUI(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter request id: ");
        String id = scanner.nextLine();
        System.out.println("From: ");
        String id_us1 = scanner.nextLine();
        System.out.println("To: ");
        String id_us2 = scanner.nextLine();
        Long Id = Long.parseLong(id);
        Long id_1 = Long.parseLong(id_us1);
        Long id_2 = Long.parseLong(id_us2);
        serv.sendFriendRequest(Id, id_1, id_2);
    }

    public void acceptFriendRequestUI(List<FriendRequest> req) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the request id: ");
        Long rId = Long.parseLong(scanner.nextLine());
        if (req.stream().filter(x -> x.getId() == rId).toList().isEmpty()) {
            System.out.println("Invalid request id");
            return;
        }
        serv.acceptFriendRequest(rId);
    }

    public void rejectFriendRequestUI(List<FriendRequest> req) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the request id: ");
        Long rId = Long.parseLong(scanner.nextLine());
        if (req.stream().filter(x -> x.getId() == rId).toList().isEmpty()) {
            System.out.println("Invalid request id");
            return;
        }
        serv.rejectFriendRequest(rId);
    }

    public void manageFriendRequestsUI() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter user id: ");
        String id_us = scanner.nextLine();
        Long id = Long.parseLong(id_us);

        boolean is_managing = true;
        while (is_managing) {
            List<FriendRequest> req = serv.getPendingFriendRequests(id);
            if (req.isEmpty()) {
                System.out.println("No more friend requests");
                is_managing = false;
                break;
            }
            req.forEach(x -> System.out.println(x.toString()));
            System.out.println("1.Accept a request");
            System.out.println("2.Reject a request");
            System.out.println("0.Back");
            int cmd = scanner.nextInt();
            if (cmd == 1) {
                acceptFriendRequestUI(req);
            }
            else if (cmd == 2) {
                rejectFriendRequestUI(req);
            }
            else if (cmd == 0) {
                is_managing = false;
            }
            else {
                System.out.println("Invalid command!");
            }
        }
    }

    public void start(){
        int cmd;
        boolean isRunning = true;
        Scanner scanner = new Scanner(System.in);

        while(isRunning)
        {
            System.out.print(meniu);
            cmd = scanner.nextInt();
            try {
                if(cmd == 1)
                {
                    addUserUi();
                }
                if(cmd == 2)
                {
                    deleteUserUi();
                }
                if(cmd == 3)
                {
                    addFriendUi();
                }
                if(cmd == 4)
                {
                    deleteFriendUi();
                }
                if(cmd == 5)
                {
                    System.out.println("There are " + serv.nrComunitati() + " communities");
                }
                if(cmd == 6)
                {
                    showUsersUi();
                }
                if(cmd == 7)
                {
                    showFriendshipsUi();
                }
                if(cmd == 8)
                {
                    showUserFriendsUI();
                }
                if(cmd == 9)
                {
                    showUserFriendsMonthUI();
                }
                if(cmd == 10)
                {
                    AddMessageToOne();
                }
                if(cmd == 11)
                {
                    AddMessageToGroup();
                }
                if(cmd == 12)
                {
                    showMessagesUI();
                }
                if(cmd == 13)
                {
                    sendFriendRequestUI();
                }
                if (cmd == 14)
                {
                    manageFriendRequestsUI();
                }
                if(cmd == 0)
                    isRunning = false;
            }
            catch (ValidationException e) {
                System.out.println(e.getMessage());
            }

    }

    }
}
