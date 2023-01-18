package Rover.Router;

import java.util.HashMap;


public class RoutingTable {


    HashMap<Byte, RoutingTableEntry> current_entries;


    public RoutingTable(byte router_ID) {
        this.current_entries = new HashMap<>();
        this.current_entries.put(router_ID, new RoutingTableEntry(router_ID, router_ID, "localhost", (byte)0));
    }

  

    @Override
    public String toString() {
        StringBuilder newStringBuilder = new StringBuilder();
        newStringBuilder.append("Address\t | Next Hop\t | Cost\n");
        newStringBuilder.append("********************************\n");
        for(byte current_key : current_entries.keySet()) {
            newStringBuilder.append(current_entries.get(current_key) + "\n");
        }

        return newStringBuilder.toString();
    }

    public int getEntriesSize() {
        return this.current_entries.size();
    }

    public HashMap<Byte, RoutingTableEntry> getEntries() {
        return this.current_entries;
    }
}
