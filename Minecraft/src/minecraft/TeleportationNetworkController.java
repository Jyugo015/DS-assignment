package minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author PC
 */
public class TeleportationNetworkController {
    private static List<Node> nodes = new ArrayList<>();
    private static List<List<Edge>> adjacencyList = new ArrayList<>();
    
    public boolean addNewNode(Node newNode) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getNameOfTeleportationPoint().equals(newNode.getNameOfTeleportationPoint())) {
                System.out.println("The teleportaton point with same name exist already. Please try a new name");
                return false;
            } else if (nodes.get(i).getX() == newNode.getX() && nodes.get(i).getY() == newNode.getY()) {
                System.out.println("The teleportaton point with same location exist already. Please try a new location");
                return false;
            }
        }
        nodes.add(newNode);
        adjacencyList.add(new ArrayList<>());
        System.out.println(adjacencyList.size());
        return true;
    }
    
    public boolean removeNode(Node node) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).equals(node)) {
                for (Node n : nodes.get(i).getNeighbours()) {
                    boolean removeNeighbour= false;
                    boolean removeAdjacencyList = false;
                    for (int j = 0; j < n.neighbours.size() && (! removeAdjacencyList || ! removeNeighbour); j++) {
                        if (n.neighbours.get(j).equals(node)) {
                            n.neighbours.remove(j);
                            removeNeighbour = true;
                        }
                        if (j < adjacencyList.get(i).size() && adjacencyList.get(i).get(j).getN2().equals(node)) {
                           adjacencyList.get(i).remove(j); 
                           removeAdjacencyList = true;
                        }
                    }
                }
                adjacencyList.remove(i);
                nodes.remove(node);
                node.neighbours = null;
                return true;
            }
        }
        return false;
    }
    
//    public boolean addNewNeighbour(Node TeleportationPoint, Node neighbours) {
//        return TeleportationPoint.addNeighbours(neighbours);
//    }
//    
//    public boolean removeNeighbour(Node TeleportationPoint, Node neighbour) {
//        boolean isRemoved = TeleportationPoint.removeNeighbour(neighbour);
//        System.out.println(((isRemoved) ? ("The neighbour is removed. Contains? " + TeleportationPoint.neighbours.contains(neighbour)) : "The neighbour doesn't belongs to the teleportation point"));
//        return isRemoved;
//    }
//    
    public ArrayList<Node> getNeighbours(Node node) {
        System.out.print("Neighbour of nodes " + node.getNameOfTeleportationPoint() + " are: ");
        if (node.neighbours != null) {
            for (Node n : node.getNeighbours()) {
                System.out.print(n.getNameOfTeleportationPoint() + " ");
            } 
        }
        System.out.println("");
        return node.neighbours;
    }
    
    public boolean contains(Node nodeName) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getNameOfTeleportationPoint().equals(nodeName)) {
                return true;
            }
        }
        return false;
    }
    
    protected static int getIndex(Node node) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).equals(node)) {
//                System.out.println("is equal");
                return i;
            } else {
//                System.out.println(node.getNameOfTeleportationPoint().equals(nodes.get(i).getNameOfTeleportationPoint()));
            }
        }
        return -1;
    }
    
    public int totalNodes() {
        return nodes.size();
    }
    
    // bfs to find the number of connected nodes 
    public ArrayList<Node> BFSnodesCanBeReached(Node teleportationPoint) {
        ArrayList<Node> reachableNode = new ArrayList<>();
        reachableNode.add(teleportationPoint);
        reachableNode.addAll(teleportationPoint.neighbours);
        for (int i = 1; i < reachableNode.size(); i++) {
            Node node = reachableNode.get(i);
            for (Node node1 : node.getNeighbours()) {
                if (!reachableNode.contains(node1)) {
                    reachableNode.add(node1);
                }
            }
        }
        System.out.println("Total number of reachable node(including the root node): " + reachableNode.size());
        return reachableNode;
    }
    
    public ArrayList<Node> shortestPath(Node teleportationPoint, Node destination) {
        if (! nodes.contains(destination) || ! nodes.contains(teleportationPoint)) {
            System.out.println("The starting point / destination doesn't exist.");
            return null;
        }
        ArrayList<Node> reachableNode = BFSnodesCanBeReached(teleportationPoint);
        System.out.println(reachableNode);
        int size = reachableNode.size();
        int[] parent = new int[size];
        for (int i = 0; i < parent.length; i++) {
            parent[i] = -1;
        }
        double[] distance = new double[size];
        for (int i = 0; i < size; i++) {
            distance[i] = Double.POSITIVE_INFINITY;
        }
        distance[0] = 0;
        ArrayList<Node> visited = new ArrayList<>();
        
        while (! visited.contains(destination) || visited.size() < size) {            
            int u = -1; // index of the vertex to be determine
            double currentMinValue = Double.POSITIVE_INFINITY;
            for (int i = 0; i < size; i++) {
                if (!visited.contains(reachableNode.get(i)) && distance[i] < currentMinValue) {
                    currentMinValue = distance[i];
                    u = i;
                }
            }
            visited.add(reachableNode.get(u));
           
            for (Edge edge : adjacencyList.get(getIndex(reachableNode.get(u)))) {
                for (int i = 0; i < size; i++) {
                    if (edge.n2.equals(reachableNode.get(i))) {
                       if (! visited.contains(reachableNode.get(i)) && distance[i] > distance[u] + edge.getDistance()) {
                            distance[i] = distance[u] + edge.getDistance();
                            parent[i] = u;
                        }
                    }
                } 
            }
        }
        System.out.println("Parent: " + Arrays.toString(parent));
        System.out.println("Distance: " + Arrays.toString(distance));
        ArrayList<Node> shortestPath = new ArrayList<>();
        // find the index of the destination
        int indexParent = 0;
        for (; indexParent < size; indexParent++)
            if (reachableNode.get(indexParent).equals(destination))
                break;
        while (indexParent != -1) {            
            shortestPath.add(reachableNode.get(indexParent));
            indexParent = parent[indexParent];
        }
        System.out.print("Shortest path: ");
        for (Node node : shortestPath) {
            System.out.print(node.getNameOfTeleportationPoint() + " ");
        }
        return shortestPath;
    }
    
    public double shortestDistance(Node teleportationPoint, Node destination) {
        ArrayList<Node> shortestPath = shortestPath(teleportationPoint, destination);
        if (shortestPath == null) {
            return -1;
        }
        double distance = 0;
        for (int i = 0; i < shortestPath.size()-1; i++) {
            distance += getDistance(shortestPath.get(i), shortestPath.get(i+1));
        }
        System.out.println("Distance: " + distance);
        return distance;
    }
    
    public double getDistance(Node teleportationPoint, Node destination) {
        for (Edge edge : adjacencyList.get(getIndex(teleportationPoint))) {
            if (edge.n2.equals(destination)) {
                System.out.println("Edge: " + edge);
                return edge.distance;
            }
        }
        return -1;
    }
    
    public ArrayList<Node> nodesOfOwner(String owner) {
        ArrayList<Node> belong = new ArrayList<>();
        for (Node node : nodes) {
            if (node.getOwner().equals(owner)) {
                belong.add(node);
            }
        }
        return belong;
    }
    
    public static class Node{
        private String nameOfTeleportationPoint;
        private String owner;
        private ArrayList<Node> neighbours = new ArrayList<>();
        private int x;
        private int y;

        public Node(String nameOfTeleportationPoint, String owner, int x, int y) {
            this.nameOfTeleportationPoint = nameOfTeleportationPoint;
            this.owner = owner;
            this.x = x;
            this.y = y;
        }

        public String getOwner() {
            return owner;
        }

        public ArrayList<Node> getNeighbours() {
            return neighbours;
        }

        public String getNameOfTeleportationPoint() {
            return nameOfTeleportationPoint;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public boolean addNeighbour(Node neighbour) {
            if (neighbour.equals(this)) return false; // the node itself is not its neighbour
            for (int i = 0; i < neighbours.size(); i++) {
                if (neighbours.get(i).equals(neighbour)) {
                    System.out.println("They are neighbour already");
                    return false;
                }
            }
            // both add each other as neighbour
            neighbours.add(neighbour);
            neighbour.neighbours.add(this);
            System.out.println("added");
            // create edge between them
            adjacencyList.get(getIndex(this)).add(new Edge(this, neighbour));
            adjacencyList.get(getIndex(neighbour)).add(new Edge(neighbour, this));
            return true;
        }
        public void  addNeighbours(Node[] neighbour){
            for (Node node : neighbour) {
                 addNeighbour(node);
            }
        }
        
        public boolean removeNeighbour(Node removedNeighbour) {
            return neighbours.remove(removedNeighbour);
        }
        
        public void setNameOfTeleportationPoint(String nameOfTeleportationPoint) {
            this.nameOfTeleportationPoint = nameOfTeleportationPoint;
        }

        @Override
        public String toString() {
            return nameOfTeleportationPoint ;
        }
    }
     
    public static class Edge{
        private double distance;
        private Node n1; // starting point
        private Node n2; // starting point

        public Edge(Node n1, Node n2) {
            this.n1 =n1;
            this.n2 =n2;
            this.distance = Math.pow((n1.x - n2.x)*(n1.x - n2.x) + (n1.y - n2.y)*(n1.y - n2.y), 0.5);
        }

        public double getDistance() {
            return distance;
        }

        public Node getN1() {
            return n1;
        }

        public Node getN2() {
            return n2;
        }

        @Override
        public String toString() {
            return String.format("%s, %s, %.3f%n", n1.nameOfTeleportationPoint, n2.nameOfTeleportationPoint, distance);
        }
    }
    
}

