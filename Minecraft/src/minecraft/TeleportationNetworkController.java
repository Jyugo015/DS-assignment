package minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeleportationNetworkController {
    private static List<Point> nodes = new ArrayList<>();
    private static List<List<Edge>> adjacencyList = new ArrayList<>();
    private static List<Edge> edges = new ArrayList<>();
    
    public boolean addNewNode(Point newNode) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getNameOfTeleportationPoint().equals(newNode.getNameOfTeleportationPoint())) {
                System.out.println("The teleportaton point with same name exist already. Please try a new name");
                return false;
            } else if (nodes.get(i).getX() == newNode.getX() && nodes.get(i).getY() == newNode.getY()) {
                System.out.println("x: " + newNode.getX());
                System.out.println("y: " + newNode.getY());
                System.out.println("The teleportaton point with same location exist already. Please try a new location");
                return false;
            }
        }
        nodes.add(newNode);
        adjacencyList.add(new ArrayList<>());
        System.out.println(adjacencyList.size());
        return true;
    }
    
    public boolean removeNode(Point node) {
        int indexCurrent = getIndex(node);
        if (indexCurrent != -1) {
            long startTime = System.currentTimeMillis();
            // remove neigbour's neigbours(current) from neighbour
            for (Point n : nodes.get(indexCurrent).getNeighbours()) {
                for (int j = 0; j < n.neighbours.size() ; j++) {
                    if (n.neighbours.get(j).nameOfTeleportationPoint.equals(node.nameOfTeleportationPoint)) {
                        n.neighbours.remove(j); // removeffrom neighbour list
                        break;
                    }
                }
                int indexNeighbour= getIndex(n); // find neighbour index
                for (int i = 0; i <  adjacencyList.get(indexNeighbour).size() ; i++) {
                    if (adjacencyList.get(indexNeighbour).get(i).n2.nameOfTeleportationPoint.equals(node.getNameOfTeleportationPoint())) {
                        adjacencyList.get(indexNeighbour).remove(i); // remove from adjacency list
                        i--;
                        break;
                    }
                }
            }
            adjacencyList.remove(indexCurrent); // remove the whole adjacency list
            System.out.println("Time taken to remove multiple edges: " + (System.currentTimeMillis() - startTime));
            nodes.remove(node);
            node.neighbours.clear();
            // remove the single edges
            startTime = System.currentTimeMillis();
            for (int j = 0; j < edges.size(); j++) {
                if (edges.get(j).n1.equals(node) || edges.get(j).n2.equals(node) ) {
                    edges.remove(j);
                    j--;
                }
            }
            System.out.println("Time taken to remove single edge: " + (System.currentTimeMillis() - startTime));
            return true;

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

    public static List<Edge> getEdges() {
        return edges;
    }
    
    public ArrayList<Point> getNeighbours(String nodename) {
        Point node = getNode(nodename);
        if (node != null && ! node.neighbours.isEmpty()) {
            System.out.print("Neighbour of nodes " + node.getNameOfTeleportationPoint() + " are: ");
            int sizeNeighbour = node.getNeighbours().size();
            for (int i = 0; i< sizeNeighbour-1; i++) {
                System.out.print(node.getNeighbours().get(i).getNameOfTeleportationPoint() + ", ");
            } 
            System.out.println(" and " + node.getNeighbours().get(sizeNeighbour-1).getNameOfTeleportationPoint());
            return node.neighbours;
            }
        return null;
    }
    
    public boolean contains(Point nodeName) {
        return getNode(nodeName.getNameOfTeleportationPoint()) != null;
    }
    
    protected static int getIndex(Point node) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).equals(node)) {
//                System.out.println("is equal");
                return i;
            } 
//            else {
//                System.out.println(node.getNameOfTeleportationPoint().equals(nodes.get(i).getNameOfTeleportationPoint()));
//            }
        }
        return -1;
    }
    
    public int totalNodes() {
        return nodes.size();
    }

    public static List<List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    public List<Point> getNodes() {
        return nodes;
    }
    
    
    // bfs to find the number of connected nodes 
    public ArrayList<Point> BFSnodesCanBeReached(Point teleportationPoint) {
        ArrayList<Point> reachableNode = new ArrayList<>();
        reachableNode.add(teleportationPoint);
        reachableNode.addAll(teleportationPoint.neighbours);
        for (int i = 1; i < reachableNode.size(); i++) {
            Point node = reachableNode.get(i);
            for (Point node1 : node.getNeighbours()) {
                if (!reachableNode.contains(node1)) {
                    reachableNode.add(node1);
                }
            }
        }
        System.out.println("Total number of reachable node(including the root node): " + reachableNode.size());
        return reachableNode;
    }
    
    public static Point getNode(String nodeName) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getNameOfTeleportationPoint().equals(nodeName)) {
                return nodes.get(i);
            }
        }
        return null;
    }
    
    public ArrayList<Point> shortestPath(String start, String dest) {
        Point currentPoint = getNode(start);
        Point destinationPoint = getNode(dest);
        if (! nodes.contains(destinationPoint) || ! nodes.contains(currentPoint)) {
            System.out.println("The starting point / destination doesn't exist.");
            return null;
        }
        ArrayList<Point> reachableNode = BFSnodesCanBeReached(currentPoint);
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
        ArrayList<Point> visited = new ArrayList<>();
        
        while (! visited.contains(destinationPoint) || visited.size() < size) {            
            int u = -1; // index of the vertex to be determine
            double currentMinValue = Double.POSITIVE_INFINITY;
            for (int i = 0; i < size; i++) {
                if (!visited.contains(reachableNode.get(i)) && distance[i] < currentMinValue) {
                    currentMinValue = distance[i];
                    u = i;
                }
            }
            if (u != -1) {
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
            } else {
                System.out.println("No way to go");
                return null;
            }
            
        }
        System.out.println("Parent: " + Arrays.toString(parent));
        System.out.println("Distance: " + Arrays.toString(distance));
        ArrayList<Point> shortestPath = new ArrayList<>();
//        shortestPath.add(destination);
        // find the index of the destination
        int indexParent = 0;
        for (; indexParent < size; indexParent++)
            if (reachableNode.get(indexParent).equals(destinationPoint))
                break;
        while (indexParent != -1) {            
            shortestPath.add(reachableNode.get(indexParent));
            indexParent = parent[indexParent];
        }
        System.out.print("Shortest path: ");
        for (Point node : shortestPath) {
            System.out.print(node.getNameOfTeleportationPoint() + " ");
        }
        return shortestPath;
    }
    
    public double shortestDistance(String teleportationPoint, String destination) {
        ArrayList<Point> shortestPath = shortestPath(teleportationPoint, destination);
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
    
    public double getDistance(Point teleportationPoint, Point destination) {
        for (Edge edge : adjacencyList.get(getIndex(teleportationPoint))) {
            if (edge.n2.equals(destination)) {
                System.out.println("Edge: " + edge);
                return edge.distance;
            }
        }
        return -1;
    }
    
    public ArrayList<Point> nodesOfOwner(String owner) {
        ArrayList<Point> belong = new ArrayList<>();
        for (Point node : nodes) {
            if (node.getOwner().equals(owner)) {
                belong.add(node);
            }
        }
        return belong;
    }
    
    public static class Point{
        private String nameOfTeleportationPoint;
        private String owner;
        private ArrayList<Point> neighbours = new ArrayList<>();
        private double x;
        private double y;

        public Point(String nameOfTeleportationPoint, String owner, double x, double y) {
            this.nameOfTeleportationPoint = nameOfTeleportationPoint;
            this.owner = owner;
            this.x = x;
            this.y = y;
        }

        public String getOwner() {
            return owner;
        }

        public ArrayList<Point> getNeighbours() {
            return neighbours;
        }

        public ArrayList<String> getNeighboursInString() {
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < neighbours.size(); i++) {
                list.add(neighbours.get(i).getNameOfTeleportationPoint());
            }
            return list;
        }
        
        public String getNameOfTeleportationPoint() {
            return nameOfTeleportationPoint;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public boolean addNeighbour(String neighbourName) {
            Point neighbour = getNode(neighbourName);
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
            edges.add(new Edge(this, neighbour));
            return true;
        }
        
        public void addNeighbours(String[] neighbourName){
            for (String node : neighbourName) {
                addNeighbour(node);
            }
        }
        
        public boolean removeNeighbour(String removedNeighbourName) {
            Point neighbour = getNode(removedNeighbourName);
            int indexCurrent = getIndex(this);
            if (neighbour != null) {
                int indexNeighbour = getIndex(neighbour);
                // remove neighbour's adjacency list that N2 is this
                for (int i = 0; i < adjacencyList.get(indexNeighbour).size(); i++) {
                    if (adjacencyList.get(indexNeighbour).get(i).getN2().equals(this)) {
                        adjacencyList.get(indexNeighbour).remove(i);
                        nodes.get(indexNeighbour).neighbours.remove(this);
                        break;
                    }
                }
                // remove this's adjacency list that N2 is neighbour
                for (int i = 0; i < adjacencyList.get(indexCurrent).size(); i++) {
                    if (adjacencyList.get(indexCurrent).get(i).getN2().equals(neighbour)) {
                        adjacencyList.get(indexCurrent).remove(i);
                        nodes.get(indexCurrent).neighbours.remove(neighbour);
                        break;
                    }
                }
                // remove edges
                for (int i = 0; i< edges.size(); i++) {
                    Edge e = edges.get(i);
                    if ((e.getN1().equals(this) && e.getN2().equals(neighbour)) || (e.getN1().equals(neighbour) && e.getN2().equals(this)) ) {
                        edges.remove(e);
                        i--;
                    }
                }
                
                return true;
            }
            return false;
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
        private Point n1; // starting point
        private Point n2; // starting point

        public Edge(Point n1, Point n2) {
            this.n1 =n1;
            this.n2 =n2;
            this.distance = Math.pow((n1.x - n2.x)*(n1.x - n2.x) + (n1.y - n2.y)*(n1.y - n2.y), 0.5);
        }

        public double getDistance() {
            return distance;
        }

        public Point getN1() {
            return n1;
        }

        public Point getN2() {
            return n2;
        }

        @Override
        public String toString() {
            return String.format("%s, %s, %.3f%n", n1.nameOfTeleportationPoint, n2.nameOfTeleportationPoint, distance);
        }
        
        
    }
}
