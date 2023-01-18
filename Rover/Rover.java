package Rover;

import java.util.Timer;
import Rover.Router.*;


public class Rover extends Thread{
    Router new_router;
    int r_id;
    

    public Rover(byte R_id) {
        this.r_id = R_id;
        this.new_router = new Router(R_id);
    }

    @Override
    public void run() {
        this.new_router.printTable();

        Timer new_timer = new Timer();
        new_timer.schedule(new Sender(this.new_router), 1000, 5000);

        Receiver new_receiver = new Receiver(this.new_router);
        new_receiver.start();
    }
}
