package com.example.demo;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        Observable
                .just("one", "two", "three")
                .flatMap(val -> Observable.just(val)
                        .subscribeOn(Schedulers.computation())
                        .flatMap(t -> {
                            try {
                                threadPrint("map", t);
                                if ("two".equals(t)) {
                                     throw new IllegalArgumentException("toto");
                                }
                                return Observable.just(t.replace("o", "O"));
                            }
                        catch (Exception e) {
                         return Observable.empty();
                        }
                        })
                )
                .subscribe(t -> {
                            threadPrint("end", t);
                        },
                        e -> {
                            threadPrint("enderror", e.getMessage());
                        }
                );
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {


        }
    }

    static public void  threadPrint(final String step, final String s) {
        System.out.println(Thread.currentThread().getName() + " : " + step + " : " + s);
    }

}

