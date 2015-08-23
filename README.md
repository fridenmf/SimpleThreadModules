# SimpleThreadModules
===================

Simple Java framework for easy managing of threads with capabilities of messaging between them.

Look for examples in /src/com/fridenmf/simplethreadmodules/examples

I've excluded module.stop() from all examples in this readme just to make the code cleaner, but of course you can stop the modules to let your program terminate. Also, all examples will use 'true' as a parameter to the module constructor, which automatically starts the module after creation. By using 'false' instead, you can start it later with module.start().

## Installation (Instructions for Eclipse only at the moment)

Clone this repo, then open Eclipse.

File -> Import -> General -> Existing Projects into Workspace

Choose the repo and click finish.

Now create your new project using:
File -> New -> Java Project

Name it and click finish.

Then select this framework as a library by:
Right click on your new project -> Properties -> Java Build Path -> Projects -> Add

Select this framework and then click OK, followed by OK.

Now you can start using this framework.

## TLDR Tutorial

Color coding: All red boxes are Consumers or has a Consumer as superclass, Blue boxes are Producers or have a Producer as superclass, and purple boxesare ConsumerProducers, or have them as superclass.

Consumer: Consumes data and performs action on each receive<br/>
Producer: Produces data and gives the result to a consumer<br/>
ConsumerProducer: Combined Consumer and Producer<br/>

Stash: A consumer that stores the incoming data, instead of performing an action<br/>
StashConsumer: A Consumer that takes data from a Stash<br/>
StashConsumerProducer: Combined StashConsumer and Producer<br/>

![TLDR Tutorial image 0](/images/tldr.png)

## Tutorial

A _module_ is basically a thing that runs on its own thread, and that can handle data of some type. All modules in this framework are generic.

To make it easy to reason within my framework, there are only two kinds of modules:

Consumer - A module that waits for data of some kind, and does some calculation based on this value. <br/>
Producer - A module that produces data of some kind, and gives the result to a Consumer.

This can be used to make a lot of programs, for example a program that has a producer that creates 0's and a consumer that prints them!

    import com.friden.simplethreadmodules.core.Consumer;
    import com.friden.simplethreadmodules.core.Producer;

    public class ZerosPrinter {

      public static void main(String[] arg){
        Consumer c = new IntPrinter(true);
        Producer p = new ZeroProducer(c, true);
      }

      private static class IntPrinter extends Consumer<Integer> {
        public IntPrinter(boolean autostart) {
          super(autostart);
        }

        @Override
        protected void onData(Integer data) {
          System.out.println(data.toString());
        }
      }

      private static class ZeroProducer extends Producer<Integer> {
        public ZeroProducer(Consumer<Integer> nextModule, boolean autostart) {
          super(nextModule, autostart);
        }

        @Override
        protected Integer produce() {
          return 0;
        }
      }
    }

Well now this may seem like a bad example, why just not make a while loop?

    while(true){
      System.out.println(0);
    }

The answer, it's not threaded. Take a look at this image:

![Pretty image 0](/images/image0.jpg)

This illustrates two 15 second runs of the program where the first bump is a program running the while loop, the second one is the one running with this framework. Even a simple program like this run with close to 100% processor utilization while a "normal" program only utilizes around 50%.

Now let's make something more fun. What if we wanted to add another module between these two? Say a module that simply increments an integer before printing it. Of course, this is not normally something you want to thread, you would just add 1 before printing it in the printer, but for the sake of this tutorial we do this anyway.

So, we want to have something in the middle that is both a consumer and a producer. We can't extend two things, so how do we make such a thing? That's where the class ConsumerProducer comes in! (I know, its a bad name, tell me if you have a better name for it, and I'll change it.)

A ConsumerProducer is something that waits for data, processes it, and then gives it to a consumer. Let's see some code:

    import com.friden.simplethreadmodules.core.Consumer;
    import com.friden.simplethreadmodules.core.ConsumerProducer;
    import com.friden.simplethreadmodules.core.Producer;

    public class ZerosIncPrinter {

      public static void main(String[] arg){
        Consumer c          = new IntPrinter(true);
        ConsumerProducer cp = new Incrementer(c, true);
        Producer p          = new ZeroProducer(cp, true);
      }

      private static class IntPrinter extends Consumer<Integer> {
        public IntPrinter(boolean autostart) {
          super(autostart);
        }

        @Override
        protected void onData(Integer data) {
          System.out.println(data.toString());
        }
      }

      private static class Incrementer extends ConsumerProducer<Integer, Integer> {
        public Incrementer(Consumer<Integer> nextModule, boolean autostart) {
          super(nextModule, autostart);
        }

        @Override
        public Integer process(Integer in) {
          return in + 1;
        }
      }

      private static class ZeroProducer extends Producer<Integer> {
        public ZeroProducer(Consumer<Integer> nextModule, boolean autostart) {
          super(nextModule, autostart);
        }

        @Override
        protected Integer produce() {
          return 0;
        }
      }
    }

And once again, a pretty image:

![Pretty image 1](/images/image1.jpg)

The first bump is from:

    while(true){
      Integer i = 0;
      i++;
      System.out.println(i.toString());
    }

The second bump is from this framework. So, enough of pretty images. What if we want to make something that consists of a lot of modules, but behaves like one big machine, where you feed it with things, and it spits out the result at the other end? To do that, we need a new concept since we can't just let the result float in the air until we pick it up. Also, if a machine processes ten things and you only pick up one, you don't want the other nine to be wasted right? We need a stash!

A Stash is simply a consumer that stores stuff from producers. Simple, right? When we try to take something from a stash that is empty, the process that tries to get from the stash will wait until there is something there. Now we can just remove our IntPrinter and replace it with a stash, and also remove our producer, to end up with a kind of machine that accepts input and produces output. I also added another incrementer to make things more interesting.

    import com.friden.simplethreadmodules.core.Consumer;
    import com.friden.simplethreadmodules.core.ConsumerProducer;
    import com.friden.simplethreadmodules.core.Stash;

    public class IncIncStash {

      public static void main(String[] arg){
        Stash<Integer> result = new Stash<>(true);
        Incrementer middle    = new Incrementer(result, true);
        Incrementer start     = new Incrementer(middle, true);

        start.add(0);
        System.out.println(result.get());
      }

      private static class Incrementer extends ConsumerProducer<Integer, Integer> {
        public Incrementer(Consumer<Integer> nextModule, boolean autostart) {
          super(nextModule, autostart);
        }

        @Override
        public Integer process(Integer in) {
          return in + 1;
        }
      }
    }

That looks neat. Now, let's take the last step of making this into a machine!

    import com.friden.simplethreadmodules.core.Consumer;
    import com.friden.simplethreadmodules.core.ConsumerProducer;
    import com.friden.simplethreadmodules.core.Machineable;
    import com.friden.simplethreadmodules.core.Stash;

    public class CustomIncIncMachine {

      public static void main(String[] arg){

        IncIncMachine machine = new IncIncMachine();

        /* Put one thing into the machine, wait for out put, repeat */
        for (int i = 0; i < 10; i++) {
          System.out.println(machine.make(i));
        }

        /* Put ten things into the machine */
        for (int i = 0; i < 10; i++) {
          machine.add(i);
        }
        /* Collect all results */
        for (int i = 0; i < 10; i++) {
          System.out.println(machine.get());
        }

      }

      private static class IncIncMachine implements Machineable<Integer, Integer>{

        /* Our machine consists of 2 workers */
        private Incrementer i0 = null;
        private Incrementer i1 = null;

        /* This is our storage, its thread safe, the program takes results from here */
        private Stash<Integer> results = null;

        public IncIncMachine(){
          results = new Stash<>(true);
          i1 = new Incrementer(results, true);
          i0 = new Incrementer(i1, true);
        }

        @Override
        public Integer make(Integer in){
          i0.add(in);
          return results.get();
        }

        @Override
        public void add(Integer in) {
          i0.add(in);
        }

        @Override
        public Integer get() {
          return results.get();
        }
      }

      private static class Incrementer extends ConsumerProducer<Integer, Integer> {
        public Incrementer(Consumer<Integer> nextModule, boolean autostart) {
          super(nextModule, autostart);
        }

        @Override
        public Integer process(Integer in) {
          return in + 1;
        }
      }
    }

Neat! As it happens to be, the framework contains a class MachineFactory, which can do all of this for you. So let's try that instead:

    import com.friden.simplethreadmodules.core.Consumer;
    import com.friden.simplethreadmodules.core.ConsumerProducer;
    import com.friden.simplethreadmodules.core.Machine;
    import com.friden.simplethreadmodules.core.MachineFactory;

    public class IncIncMachine {

      public static void main(String[] arg){
        ConsumerProducer[] workers = new ConsumerProducer[]{
          new Incrementer(null, false),
          new Incrementer(null, false),
          new Incrementer(null, false),
          new Incrementer(null, false),
        };

        Machine<Integer, Integer> machine = MachineFactory.assemble(workers);

        for (int i = 0; i < workers.length; i++) {
          System.out.println(machine.make(i));
        }

        for (int i = 0; i < 100; i++) {
          machine.add(i);
        }
        for (int i = 0; i < 100; i++) {
          System.out.println(machine.get());
        }
      }

      private static class Incrementer extends ConsumerProducer<Integer, Integer> {
        public Incrementer(Consumer<Integer> nextModule, boolean autostart) {
          super(nextModule, autostart);
        }

        @Override
        public Integer process(Integer in) {
          return in + 1;
        }
      }
    }

Ok, that was easy, and short! Good, now we can make machines. Let's talk about the performance of _make_ vs _add/get_ for a while. Let's say that we had 10 workers in our machine, and each worker took one second to process something. Then it would take the machine 10 seconds to produce something from the moment it gets the input. Using the make function 10 times would take 100 seconds, since we wait until the whole machine is done before feeding it with the next input.

Now consider the add/get approach. The first product will indeed take 10 seconds, but the second product would arrive the second after the first one since all workers do stuff in parallel. We could then make 2 products in 11 seconds. If we feed it with 3 products it would be done in 12 seconds. 4 in 13, 5 in 14, 6 in 15. 10 items will take 19 seconds, compared to the make method's 100 seconds. That is five times as fast, as long as you have threads for it. 20 products with make: 200 seconds. 20 products with add/get: 29 seconds, which is about 6.6 times faster. So, if you have a program where it's possible to use add/get instead of make, then do it!

Now, more interesting stuff! We have seen Producers, Consumers, Stashes, Machines, what more could there possible be to add? Pools?

A Pool is kind of a resource with workers, ready to do your bidding. You could add 10 incrementers to a pool, and then just feed the pool with values. The incoming values will be put in a stash from which the workers in the pool will grab values, work on them, and then put them in another stash of results. To do this we need to introduce a new class, namely the StashConsumer.

A StashConsumer is a consumer that can only consume stuff from stashes. It's also special in one more way: it can clone itself, in order to make the implementation of the pool easier. Let's try it first:

    import com.friden.simplethreadmodules.core.Consumer;
    import com.friden.simplethreadmodules.core.Stash;
    import com.friden.simplethreadmodules.core.StashConsumer;

    public class StashConsumerExample {

      public static void main(String[] arg){
        Stash<Integer> in  = new Stash<>(true);
        Stash<Integer> out = new Stash<>(true);

        StashIncrementer si0 = new StashIncrementer(in, out, true);
        StashIncrementer si1 = new StashIncrementer(in, out, true);
        StashIncrementer si2 = new StashIncrementer(in, out, true);

        for (int i = 0; i < 20; i++) {
          in.add(i);
        }
        for (int i = 0; i < 20; i++) {
          System.out.println(out.get());
        }
      }

      private static class StashIncrementer extends StashConsumer<StashIncrementer, Integer, Integer> {

        public StashIncrementer(Stash<Integer> stash, Consumer<Integer> nextModule, boolean autostart) {
          super(stash, nextModule);
          if(autostart){
            start();
          }
        }

        @Override
        protected Integer produce(Integer data) {
          return data + 1;
        }

        @Override
        public StashIncrementer copy() {
          return new StashIncrementer(stash, nextModule, false);
        }
      }
    }

So, that's basically our pool. It has three StashIncrementers taking from a common stash, putting the results in another common stash. Neat! Now we do the same thing, but with 10 workers and with the provided Pool class:

    import com.friden.simplethreadmodules.core.Consumer;
    import com.friden.simplethreadmodules.core.Stash;
    import com.friden.simplethreadmodules.core.StashConsumer;
    import com.friden.simplethreadmodules.core.Pool;

    public class IncPoolExample {

      public static void main(String[] arg){

        StashIncrementer stashIncer = new StashIncrementer(null, null, false);
        Pool<StashIncrementer, Integer, Integer> pool = new Pool<>(stashIncer, 10, true);

        for (int i = 0; i < 20; i++) {
          pool.add(1);
        }
        for (int i = 0; i < 20; i++) {
          System.out.println(pool.get());
        }

      }

      private static class StashIncrementer extends StashConsumer<StashIncrementer, Integer, Integer> {

        public StashIncrementer(Stash<Integer> stash, Consumer<Integer> nextModule, boolean autostart) {
          super(stash, nextModule);
          if(autostart){
            start();
          }
        }

        @Override
        protected Integer produce(Integer data) {
          return data + 1;
        }

        @Override
        public StashIncrementer copy() {
          return new StashIncrementer(stash, nextModule, false);
        }
      }
    }

Ok, this is what this framework has at the moment, but it will probably be extended with lots of more stuff. I'm gladly accepting feedback, especially if you have a proposal for a new class that would add some awesome functionality. I will post contact information here shortly. Now go play with the framework. ^^
