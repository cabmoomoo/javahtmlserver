package model;

/**
 * Very experimental example of how the server could track state. This might not be the best way to go about it,
 * but it is the quickest way I could think of to make a POST request actually do something interesting.
 */
public class State {
    private int stateValue = 0;

    /*
     * Making everything synchronized means I can just pass around a single object and not worry about thread-safety
     * https://stackoverflow.com/a/8982589
     */
    
    public synchronized void add(int amt) {
        this.stateValue += amt;
    }

    public synchronized void sub(int amt) {
        this.stateValue -= amt;
    }

    public synchronized int getStateValue() {
        return this.stateValue;
    }
}
