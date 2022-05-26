package lab6.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Network<ID> {
    private int numberOfUsers;
    private Map<ID, ArrayList<ID>> connections;

    public Network() {
        this.numberOfUsers = 0;
        this.connections = new HashMap<ID, ArrayList<ID>>();
    }

    /**
     * Adauga user in retea
     * @param id: tipul ID
     */
    public void addUser(ID id){
        connections.put(id,new ArrayList<ID>());
        this.numberOfUsers++;
    }

    /**
     * Sterge user din retea
     * @param id: tipul ID
     */
    public void removeUser(ID id) {
        for(ID frId : connections.get(id)) {
            connections.get(frId).remove(id);
        }
        connections.remove(id);
        this.numberOfUsers--;
    }

    /**
     * Adauga o legatura
     * @param id_1:tipul ID
     * @param id_2:tipul ID
     */
    public void addConnection(ID id_1, ID id_2){
        connections.get(id_1).add(id_2);
        connections.get(id_2).add(id_1);
    }

    /**
     * Sterge o legatura
     * @param id_1: tipul ID
     * @param id_2:tipul ID
     */
    public void removeConnection(ID id_1, ID id_2) {
        connections.get(id_1).remove(id_2);
        connections.get(id_2).remove(id_1);
    }

    /**
     * Parcurge graful in adancime
     * @param id: tipul ID
     * @param visited: tipul Boolean
     */
    private void DFSUtil(ID id, Map<ID, Boolean> visited) {
        visited.replace(id, true);
        for (ID friendId : connections.get(id)) {
            if (!visited.get(friendId))
                DFSUtil(friendId, visited);
        }
    }

    /**
     * Afla numarul comunitatilor
     * @return: numarul comunitatilor
     */
    public int numberOfCommunities(){
        Map<ID, Boolean> visited = new HashMap<ID, Boolean>();
        for(ID id : connections.keySet()) {
            visited.put(id, false);
        }
        int communities = 0;
        for(ID id : connections.keySet()) {
            if (!visited.get(id)) {
                DFSUtil(id, visited);
                communities++;
            }
        }
        return communities;
    }
}
