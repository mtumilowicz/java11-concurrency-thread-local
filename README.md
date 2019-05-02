[![Build Status](https://travis-ci.com/mtumilowicz/java11-concurrency-thread-local.svg?branch=master)](https://travis-ci.com/mtumilowicz/java11-concurrency-thread-local)

# java11-concurrency-thread-local
The main goal of this project is to show basic features of `ThreadLocal`.

_Reference_: https://docs.oracle.com/javase/7/docs/api/java/lang/ThreadLocal.html  
_Reference_: https://www.amazon.com/Java-Concurrency-Practice-Brian-Goetz/dp/0321349601

# preface
**ThreadLocal** - each thread has its own, independently initialized copy of the 
variable. ThreadLocal instances are typically private static fields in classes 
that wish to associate state with a thread (e.g., a user ID or Transaction ID).  
**Since** Java 1.2.

**Garbage collector**: Each thread holds an implicit reference to its copy of a 
thread-local variable as long as the thread is alive and the `ThreadLocal` 
instance is accessible; after a thread goes away, all of its copies of 
thread-local instances are subject to garbage collection (unless other 
references to these copies exist).

* Conceptually, you can think of a `ThreadLocal<T>` as holding 
a `Map<Thread,T>` that stores the thread-specific values, though this is not how it is actually implemented.
* The thread-specific values are stored in the `Thread` object itself; when the thread terminates, the
thread-specific values can be garbage collected.
* `@Transational` in Spring (or in general - transaction management) is a good example of using `ThreadLocal` as
the entity manager and the connection are both bound to the current thread using `ThreadLocal` variables - 
This is convenient in that it reduces the need to pass execution context information into every method.
* Like global variables, thread-local variables can detract from reusability and introduce hidden couplings 
among classes, and should therefore be used with care.

# project description
* `ThreadId` assigns id to thread.
* `MyCallable` represents unit work that has to be done by a thread.
    ```
    @Override
    public int[] call() {
        return new int[]{ThreadId.get(), ThreadId.get(), ThreadId.get()}; // if ThreadId is OK array should have the same values
    }    
    ```
* `Main`, `ThreadIdTest` - showcase

# tests
* if tasks are done by one thread, ids should be the same (`ids_same_oneThread`)
    ```
    ExecutorService executorService = Executors.newFixedThreadPool(1);
    
    List<Future<int[]>> futures = executorService.invokeAll(Arrays.asList(
            sharedCallableInstance,
            sharedCallableInstance,
            sharedCallableInstance));
            
    int[] ids1 = futures.get(0).get();
    int[] ids2 = futures.get(1).get();
    int[] ids3 = futures.get(2).get();
    
    assertThat(ids1, is(ids2));
    assertThat(ids2, is(ids3));
    assertThat(ids3, is(ids1));
    ```
* if tasks are done by 3 thread, each id should be different (`ids_difference_atLeast3Threads`)
    ```
    ExecutorService executorService = Executors.newFixedThreadPool(3);
    
    List<Future<int[]>> futures = executorService.invokeAll(Arrays.asList(
            sharedCallableInstance,
            sharedCallableInstance,
            sharedCallableInstance));
    
    
    int[] ids1 = futures.get(0).get();
    int[] ids2 = futures.get(1).get();
    int[] ids3 = futures.get(2).get();
    
    assertThat(ids1, not(ids2));
    assertThat(ids2, not(ids3));
    assertThat(ids3, not(ids1));
    ```
* we verify that array returned by `MyCallable` has the same values (`ids_array_has_same_values`)
    ```
    ExecutorService executorService = Executors.newCachedThreadPool();
    
    int[] ids = executorService.submit(sharedCallableInstance).get();
    
    assertThat(ids[0], is(ids[1]));
    assertThat(ids[1], is(ids[2]));
    assertThat(ids[2], is(ids[0]));
    ```
