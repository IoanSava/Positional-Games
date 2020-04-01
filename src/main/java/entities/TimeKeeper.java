package entities;

import exceptions.InvalidTimeException;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

/**
 * Timekeeper thread that runs concurrently
 * with the player threads, as a daemon.
 * This thread will display the running time
 * of the game and it will stop the game
 * if it exceeds a certain time limit.
 *
 * @author Ioan Sava
 */
@Getter
public class TimeKeeper extends Thread {
    private int timeInMinutes;

    public TimeKeeper(int time) throws InvalidTimeException {
        setTimeInMinutes(time);
    }

    public void setTimeInMinutes(int time) throws InvalidTimeException {
        if (time < 1) {
            throw new InvalidTimeException("A time keeper should run for at least 1 minute");
        }
        this.timeInMinutes = time;
    }

    @Override
    public void run() {
        for (int i = 0; i < timeInMinutes; ++i) {
            System.out.println(i + " minutes passed. " + (timeInMinutes - i) + " minutes remaining.");
            try {
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Limit time exceeded. This game will be over soon");
    }
}
