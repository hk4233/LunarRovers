package Rover.Router;

import java.net.MulticastSocket;
import java.util.HashMap;
import java.io.IOException;
import java.net.DatagramPacket;

import java.util.concurrent.ConcurrentHashMap;
import Rover.Router.RIP.*;

public class Receiver extends Thread {
    MulticastSocket current_socket;
    Router current_router;

    ConcurrentHashMap<Byte, Long> next_timestamps = new ConcurrentHashMap<>();
    ConcurrentHashMap<Byte, RoutingTableEntry> removed_entries = new ConcurrentHashMap<>();

    public Receiver(Router newrouter) {
        this.current_router = newrouter;
        this.current_socket = newrouter.multi_socket;
    }

    @Override
    public void run() {
        while (true) {
            byte[] new_buffer = new byte[512];
            DatagramPacket data_packet = new DatagramPacket(new_buffer, new_buffer.length);
            try {
                current_socket.receive(data_packet);
            } catch (IOException e) {
                System.out.println("check again in sometime.");
                e.printStackTrace();
            }
            checkStatus();
            System.out.println(this.next_timestamps);
            packetProcess(data_packet);
        }
    }


    public void checkStatus() {
        boolean has_changed = false;

        for (byte current_key : this.next_timestamps.keySet()) {
            long timeInSeconds = (System.nanoTime() - this.next_timestamps.get(current_key)) / 1000000000;

            if (timeInSeconds > 10) {
                boolean deleted = deleteEntry(current_key);

                if (deleted) {
                    has_changed = true;
                    next_timestamps.remove(current_key);
                    System.out.println(" #" + current_key + " deleted from routing table.");
                }
            }
        }

        if (has_changed) {
            this.current_router.printTable();
            updateTrigger();
        }
    }


    public boolean deleteEntry(byte routerId) {
        try {
            RoutingTableEntry entry = this.current_router.rout_table.current_entries.get(routerId);
            this.removed_entries.put(routerId, entry);
            this.current_router.rout_table.current_entries.remove(routerId);

            for (byte key : this.current_router.rout_table.current_entries.keySet()) {
                RoutingTableEntry rte = this.current_router.rout_table.current_entries.get(key);
                if (rte.next_rover == routerId) {
                    rte.sentCost = (byte) 16;
                    this.current_router.rout_table.current_entries.put(rte.current_rid, rte);
                }
            }
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public void updateTrigger(){
        new Thread(new Sender(this.current_router)).start();
    }

    public void packetProcess(DatagramPacket Data_packet) {
        byte[] data_packet = Data_packet.getData();
        String ipAddress = Data_packet.getAddress().toString();

        RIPPacket new_ripPacket = new RIPPacket(data_packet);
        byte nextId = new_ripPacket.getRId();
        HashMap<Byte, RIPEntry> current_ripEntries = new_ripPacket.getEntries();

        if (nextId == this.current_router.rover_ID)
            return;

        this.next_timestamps.put(new_ripPacket.getRId(), System.nanoTime());

        boolean isChange = false;
        for (byte current_key : current_ripEntries.keySet()) {
            RIPEntry current_entry = current_ripEntries.get(current_key);

            byte cost = current_entry.getCost();
            if (current_entry.getNextRover() == this.current_router.rover_ID) {
                cost = (byte)16;
            }

            if (this.current_router.rout_table.current_entries.containsKey(current_entry.getDestination())) {
                RoutingTableEntry routingTableEntry = this.current_router.rout_table.current_entries.get(current_entry.getDestination());

                if (routingTableEntry.sentCost == 0) {
                    continue;
                }
                else {
                    if (cost == 16) {
                        routingTableEntry.sentCost = 16;
                        this.current_router.rout_table.current_entries.put(routingTableEntry.current_rid, routingTableEntry);
                        isChange = true;
                    }
                    
                    else {
                        if (routingTableEntry.sentCost > cost + 1) {
                            routingTableEntry.setNextRover(current_entry.getNextRover());
                            routingTableEntry.sentCost = (byte) (cost + 1);
                            this.current_router.rout_table.current_entries.put(routingTableEntry.current_rid, routingTableEntry);
                            isChange = true;
                        }
                    }
                }
            }
            else {
                this.current_router.rout_table.current_entries.put(current_entry.getDestination(), 
                new RoutingTableEntry(current_entry.getDestination(), current_entry.getNextRover(), ipAddress, (byte)(cost + 1)));
                isChange = true;
            }
        }

        if (isChange) {
            this.current_router.printTable();
            updateTrigger();
        }
    }

    
}
