package Rover.Router.RIP;

import java.util.HashMap;
import Rover.Router.RoutingTableEntry;
import Rover.Router.RoutingTable;



public class RIPPacket {
    byte ver;
    byte count;
    byte com;
    
    byte rid;

    HashMap<Byte, RIPEntry> entries;

  
    public RIPPacket(byte[] data_packet) {
        this.com = data_packet[0];
        this.ver = data_packet[1];
        this.rid = data_packet[2];
        this.count = data_packet[3];

        this.entries = new HashMap<>();


        for (int i = 0; i < this.count; i++) {
            int j = i * 20 + 4;
            byte[] dataEntry = new byte[20];
            System.arraycopy(data_packet, j, dataEntry, 0, 20);
            this.entries.put(dataEntry[4], new RIPEntry(dataEntry));
        }
    }


    public RIPPacket(byte rid, RoutingTable routingTableList) {
        HashMap<Byte, RoutingTableEntry> tableEntries = routingTableList.getEntries();

        this.ver = 2;
        this.rid = rid;
        this.com = 2;
        
        this.count = (byte)routingTableList.getEntriesSize();

        this.entries = new HashMap<>();
        for (byte current_key : tableEntries.keySet()) {
            RoutingTableEntry routingTableEntry = tableEntries.get(current_key);
            entries.put(current_key, new RIPEntry(routingTableEntry.getRId(), routingTableEntry.getNextHop(), routingTableEntry.getCost()));
        }
    }


    public byte getRId() {
        return rid;
    }

    public byte[] createRIPPacketData() {
        byte[] data = new byte[512];

        data[0] = this.com;
        data[1] = this.ver;
        data[2] = this.rid;
        data[3] = this.count;

        int i = 0;
        for (byte current_key : this.entries.keySet()) {
            byte[] dataEntry = this.entries.get(current_key).createEntryData();
            System.arraycopy(dataEntry, 0, data, i * dataEntry.length + 4, dataEntry.length);
            i++;
        }

        return data;
    }


    @Override
    public String toString() {
        StringBuilder entriesString = new StringBuilder();

        for (byte current_key : this.entries.keySet()) {
            entriesString.append(entries.get(current_key));
        }
        return "RIPPacket{" +
                "command=" + com +
                ", version=" + ver +
                ", ripEntriesCount=" + count +
                ", routerId=" + rid +
                ", ripEntries=" + entriesString.toString() +
                '}';
    }

    public HashMap<Byte, RIPEntry> getEntries() {
        return entries;
    }
}
