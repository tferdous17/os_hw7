import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        Lock fork0 = new ReentrantLock();
        Lock fork1 = new ReentrantLock();

        Thread phil0 = new Thread(new Philosopher(0, fork0, fork1));
        Thread phil1 = new Thread(new Philosopher(1, fork1, fork0));

        phil0.start();
        phil1.start();
    }

    static class Philosopher implements Runnable {
        private final int id;
        private final Lock firstFork;
        private final Lock secondFork;

        public Philosopher(int id, Lock firstFork, Lock secondFork) {
            this.id = id;
            this.firstFork = firstFork;
            this.secondFork = secondFork;
        }

        @Override
        public void run() {
            try {
                // Each philosopher thinks and eats for 3 cycles
                for (int i = 0; i < 3; i++) {
                    think();
                    eat();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void think() throws InterruptedException {
            System.out.println("Philosopher " + id + " is currently thinking");
            // think for few seconds
            Thread.sleep((long) (Math.random() * 2000 + 1000));
        }

        private void eat() throws InterruptedException {
            // get first fork
            System.out.println("Philosopher " + id + " is trying to pick up first fork right now");
            firstFork.lock();
            System.out.println("Philosopher " + id + " has picked up first fork");

            // get second fork
            System.out.println("Philosopher " + id + " is trying to pick up second fork right now");
            secondFork.lock();
            System.out.println("Philosopher " + id + " has picked up second fork");

            try {
                System.out.println("Philosopher " + id + " is currently eating");
                // put thread to sleep for a bit to simulate eating
                Thread.sleep((long) (Math.random() * 2000 + 1000));
                System.out.println("Philosopher " + id + " finished eating");
            } finally {
                // Release forks
                secondFork.unlock();
                System.out.println("Philosopher " + id + " has put down the second fork");
                firstFork.unlock();
                System.out.println("Philosopher " + id + " has put down the first fork");
            }
        }
    }
}
