import java.util.concurrent.Callable;

/**
 * Created by mtumilowicz on 2018-08-26.
 */
public class MyCallable implements Callable<int[]> {
    @Override
    public int[] call() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }

        return new int[]{ThreadId.get(), ThreadId.get(), ThreadId.get()}; // if ThreadId is OK arrays should have the same values
    }
}
