package bgu.atd.a1;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Actor{

    private String id;
    private ConcurrentLinkedQueue<Action<?>> pendingActions;
    private Lock lock;

    public Actor(String id){

        this.id = id;
        this.pendingActions = new ConcurrentLinkedQueue<Action<?>>();
        this.lock = new ReentrantLock();

    }

    public Lock getLock(){

        return lock;

    }

    public String getId(){

        return id;

    }


    public ConcurrentLinkedQueue<Action<?>> getPendingActions(){

        return pendingActions;

    }

    public void addAction(Action<?> action){

        pendingActions.add(action);

    }

}