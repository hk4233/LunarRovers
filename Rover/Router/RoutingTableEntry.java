package Rover.Router;

public class RoutingTableEntry {
    byte current_rid;
    String current_addr;
    byte next_rover;
    String next_rover_IP;
    byte sentCost;


    public RoutingTableEntry(byte current_rid, byte next_rover, String next_rover_IP, byte sentCost) {
        this.next_rover = next_rover;
        this.sentCost = sentCost;
        this.next_rover_IP = next_rover_IP;
        this.current_rid = current_rid;
        this.current_addr = "10.0." + current_rid + ".0";
        
    }

    public void setNextRover(byte nextHop) {
        this.next_rover = nextHop;
    }

    public byte getRId() {
        return current_rid;
    }


    @Override
    public String toString() {
        return this.current_addr + "\t | " +
                this.next_rover_IP + "(" + this.next_rover + ")\t | " +
                this.sentCost;
    }


    public byte getCost() {
        return sentCost;
    }

    public byte getNextHop() {
        return next_rover;
    }

}
