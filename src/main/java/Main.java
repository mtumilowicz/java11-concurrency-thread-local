import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by mtumilowicz on 2018-08-26.
 */
public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MyCallable sharedCallableInstance = new MyCallable();

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        List<Future<int[]>> futures = executorService.invokeAll(Arrays.asList(
                sharedCallableInstance, 
                sharedCallableInstance, 
                sharedCallableInstance));
        
        System.out.println(Arrays.toString(futures.get(0).get()));
        System.out.println(Arrays.toString(futures.get(1).get()));
        System.out.println(Arrays.toString(futures.get(2).get()));
        
        executorService.shutdownNow();
    }
}
