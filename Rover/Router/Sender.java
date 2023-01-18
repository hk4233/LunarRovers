package Rover.Router;

import Rover.Router.RIP.RIPPacket;

import java.net.UnknownHostException;
import java.util.TimerTask;
import java.io.IOException;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;


public class Sender extends TimerTask  {
    MulticastSocket current_socket;
    Router current_router;

    public Sender(Router current_router) {
        this.current_router = current_router;
        this.current_socket = current_router.multi_socket;
    }

    @Override
    public void run() {
        DatagramPacket data_packet = this.createNewDataGram();
        try {
            current_socket.send(data_packet);
        } catch (IOException e) {
            System.out.println("Please try later!");
            e.printStackTrace();
        }
    }

    public DatagramPacket createNewDataGram() {
        RIPPacket current_rippacket = new RIPPacket(this.current_router.rover_ID, this.current_router.rout_table);

        byte[] datapacket = current_rippacket.createRIPPacketData();
        DatagramPacket dp = null;
        try {
            dp = new DatagramPacket(datapacket, datapacket.length, InetAddress.getByName(this.current_router.router_config.multi_IP), this.current_router.router_config.getPort());
        } catch (UnknownHostException e) {
            System.out.println("please try later");
            e.printStackTrace();
        }

        return dp;
    }
}
