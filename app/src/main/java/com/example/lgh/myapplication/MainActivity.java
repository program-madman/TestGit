package com.example.lgh.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tv = (TextView) findViewById(R.id.ua_text);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //WebView web = new WebView(MainActivity.this);
                //tv.setText(web.getSettings().getUserAgentString());
                startActivity(new Intent(MainActivity.this, BTest.class));
                //overridePendingTransition(R.anim.anim_in,R.anim.anim_in);
            }
        });
        Log.d("lgh", "run rx");

        Action1<String> onNext = new Action1<String>() {


            @Override
            public void call(String s) {
                if (s.equals("C")) {
                    throw new NullPointerException("hahah");
                }
                Log.d("lgh", " on next " + s);
            }
        };
        Action1<Throwable> onError = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.d("lgh", "on error " + throwable.getMessage());
            }
        };

        Action0 complete = new Action0() {
            @Override
            public void call() {
                Log.d("lgh", "on success");
            }
        };


        final String[] strs = new String[]{"A", "B", "C"};
        Observable.just("1", "2").map(new Func1<String, Integer>() {
            @Override
            public Integer call(String s) {
                if (s.equals("A")) {
                    return 1;
                }

                return 0;
            }
        }).flatMap(new Func1<Integer, Observable<String>>() {
            @Override
            public Observable<String> call(Integer integer) {
                return Observable.from(strs);
                //return null;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d("lgh", "flat map " + s);
            }
        });

        Observable
                .just("1 "+Thread.currentThread().getName())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s+Thread.currentThread().getName()+"  ";
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Log.d("lgh","current "+Thread.currentThread().getName());
                    }
                })

                .map(new Func1<String, String>() {

                    @Override
                    public String call(String s) {
                        return s+Thread.currentThread().getName()+"  ";
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Log.d("lgh","sec "+Thread.currentThread().getName());
                    }
                }).subscribe();


        PublishSubject<String> p = PublishSubject.create();
        p.onNext("a");
        p.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d("lgh",s);
            }
        });
        p.onNext("b");
        p.onNext("c");
        p.onNext("d");

        FutureTask<String> task = new FutureTask<String>(
                new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return "123";
                    }
                }
        );
        new Thread(task).start();
        Integer[] number = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        Observable.from(number).filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer %2 == 0;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("lgh"," result "+integer);
            }
        });

        AA a1 = new AA("a");
        AA a2 = new AA("b");
        AA a3 = new AA("a");
        AA a4 = new AA("c");

        Observable.just(a1,a2,a3,a4).distinct(new Func1<AA, String>() {
            @Override
            public String call(AA aa) {
                return aa.getA();
            }
        }).subscribe(new Action1<AA>() {
            @Override
            public void call(AA aa) {
                     Log.d("lgh","aas " +aa);
            }
        });

        Observable.just(0,2,3,1).takeWhile(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer < 2;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d("lgh","iiii " +integer);
            }
        });


        Observable.just(2,4,6,8).all(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer %2 == 0;
            }
        }).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                Log.d("lgh","all  " +aBoolean);
            }
        });

        Observable o1 = Observable.just(1,2,3);
        Observable o2 = Observable.just(1,2,3);

       // Observable.just(1).reduce()
    }

    public static class AA {
        public String a;

        public AA(String a) {
            this.a = a;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AA aa = (AA) o;

            return a.equals(aa.a);

        }

        @Override
        public int hashCode() {
            return a.hashCode();
        }

        @Override
        public String toString() {
            return "AA{" +
                    "a='" + a + '\'' +
                    '}';
        }

        public String getA() {
            return a;
        }


    }
    //var x : Int = -1;

}