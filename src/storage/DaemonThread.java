package storage;

import java.util.concurrent.TimeUnit;

/**
 * A daemon thread running in the background to clean-up the
 * files in the speficified directory.
*/

class DaemonThread implements Runnable {
    private String DIRECTORY_PATH = "./db/";
    private Integer WAITING_PERIOD = 10000;
    private volatile boolean STOP_DAEMEN_THREAD = false;

    private FileHandler<Integer> f;
    private String name;
    private Thread t;
 
    public DaemonThread(String name, FileHandler<Integer> f) {
        this.f = f;
        this.name = name;
    }

    public void run() {
        while(!STOP_DAEMEN_THREAD) {
            try {

                f.clean(DIRECTORY_PATH);

                TimeUnit.MILLISECONDS.sleep(WAITING_PERIOD);
            }
            catch(InterruptedException e) {
                System.out.println("Thread " +  name + " interrupted.");
                break;
            }
        }
    }

    public void start() {
        if (t == null) {
            t = new Thread(this, name);
            t.start();
        }
    }

    /*
    * Stop the thread using the volatile variable
    */
    public void stop() {
        STOP_DAEMEN_THREAD = true;
    }

}
