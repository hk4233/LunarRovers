/**
 * @author hima bindu krovvidi
 */

package Rover.Router.RIP;

import java.util.Arrays;


public class RIPEntry {
    byte nextrover;
    byte cost;
    byte[] addr;

    byte dest;
  
    public RIPEntry(byte[] dataPacket) {
        int i = 0;

        this.addr = new byte[] {dataPacket[i++], dataPacket[i++]};

        i += 2;

        this.dest = dataPacket[i++];
        this.nextrover = dataPacket[i++];

        i += 10;

        this.cost = dataPacket[i];
    }

  
    public RIPEntry(byte destination, byte nextrover, byte cost) {
        this.addr = new byte[]{0, 2};
        this.dest = destination;
        this.nextrover = nextrover;
        this.cost = cost;
    }

  
    public byte[] createEntryData() {
        byte[] buffer = new byte[20];

        int i = 0;
        buffer[i++] = this.addr[0];
        buffer[i++] = this.addr[1];

        buffer[i++] = 0;
        buffer[i++] = 0;

        buffer[i++] = this.dest;

        buffer[i++] = this.nextrover;

        i += 2;

        buffer[i++] = (byte)255;
        buffer[i++] = (byte)255;
        i += 6;


        buffer[i++] = this.cost;

        return buffer;
    }

    public byte getCost() {
        return cost;
    }

  

   
    @Override
    public String toString() {
        return "RIPEntry{" +
                "addressFamilyIdentifier=" + Arrays.toString(addr) +
                ", destination=" + dest +
                ", nextHop=" + nextrover +
                ", cost=" + cost +
                '}';
    }

    public byte getDestination() {
        return dest;
    }

    public byte getNextRover() {
        return nextrover;
    }
}
