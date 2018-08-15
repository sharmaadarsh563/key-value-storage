package storage;

import java.util.concurrent.TimeUnit;

/**
 * A daemon thread running in the background to clean-up the
 * files in the speficified directory and make a key-value pair
 * as most recently used after exctrating it from the queue
*/

class DaemonThread implements Runnable {
    private String DIRECTORY_PATH = "./db/";
    private Integer WAITING_PERIOD = 10000;
    private Integer NUMBER_OF_NODES_TO_PROCESS = 100;
    private volatile boolean STOP_DAEMEN_THREAD = false;

    private FileHandler<Integer> f;
    private KeyValueStore<Integer,String> store;
    private String name;
    private Thread t;
 
    public DaemonThread(String name, FileHandler<Integer> f, KeyValueStore<Integer,String> store) {
        this.f = f;
        this.name = name;
        this.store = store;
    }

    public void run() {
        while(!STOP_DAEMEN_THREAD) {
            try {

                f.clean(DIRECTORY_PATH);

                int count = NUMBER_OF_NODES_TO_PROCESS;
                while((count > 0) && (KeyValueStoreConcrete.getQueueSize() > 0)) {
                    Node<Integer,String> node = KeyValueStoreConcrete.removeQueueElement();
                    store.put(node.key, node.value);
                    count--;
                }

                TimeUnit.MILLISECONDS.sleep(WAITING_PERIOD);
            }
            catch(InterruptedException e) {
                System.out.println("Thread " +  name + " interrupted.");
                break;
            }
        }
    }

    /*
    * Start a new thread
    */
    public void start() {
        if(t == null) {
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
