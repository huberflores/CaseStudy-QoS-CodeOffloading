package com.codeoff.casestudy;
//https://jimmaru.wordpress.com/2011/12/13/simple-andengine-game-v2-0-more-awesomeness/
import java.util.Timer;
import java.util.TimerTask;

public class CoolDown {
    private boolean valid;
    private Timer timer;
    private long delay = 500; //0.5s
    private static CoolDown instance = null;
 
    public static CoolDown sharedCoolDown() {
        if (instance == null) {
            instance = new CoolDown();
        }
        return instance;
    }
 
    private CoolDown() {
        timer = new Timer();
        valid = true;
    }
 
    public boolean checkValidity() {
        if (valid) {
            valid = false;
            timer.schedule(new Task(), delay);
            return true;
        }
        return false;
    }
 
    class Task extends TimerTask {
        public void run() {
            valid = true;
        }
    }
}