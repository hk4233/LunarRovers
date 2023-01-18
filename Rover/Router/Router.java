package Rover.Router;

import java.net.*;
import java.io.IOException;


public class Router {
    MulticastSocket multi_socket;
    RoutingTable rout_table;
    byte rover_ID;
    RouterConfig router_config;
    
    public Router(byte r_id) {
        this.router_config = new RouterConfig(r_id);
        this.rover_ID = r_id;
        
        try {
            this.multi_socket = new MulticastSocket(router_config.getPort());
            multi_socket.setOption(StandardSocketOptions.IP_MULTICAST_LOOP, false);
        } catch (IOException e) {
            System.out.println("Please try later");
            e.printStackTrace();
        }

        this.rout_table = new RoutingTable(r_id);
    }

    public void printTable() {
        System.out.println(this.rout_table);
    }
}
