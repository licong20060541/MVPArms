/**
  * Copyright 2017 JessYan
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *      http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package com.jess.arms.utils;

import com.jess.arms.integration.lifecycle.ActivityLifecycleable;
import com.jess.arms.integration.lifecycle.FragmentLifecycleable;
import com.jess.arms.integration.lifecycle.Lifecycleable;
import com.jess.arms.mvp.IView;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import io.reactivex.annotations.NonNull;

/**
 * ================================================
 * 使用此类操作 RxLifecycle 的特性
 * <p>
 * Created by JessYan on 26/08/2017 17:52
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 *
 * Rxlifecycle使用

 Activity/Fragment需继承RxAppCompatActivity/RxFragment，目前支持的有RxAppCompatActivity、
 RxFragment、RxDialogFragment、RxFragmentActivity。

 一、bindToLifecycle()方法

 在子类使用Observable中的compose操作符，调用，完成Observable发布的事件和当前的组件绑定，实现生命周期同步。
 从而实现当前组件生命周期结束时，自动取消对Observable订阅。

         Observable.interval(1, TimeUnit.SECONDS)

         .compose(this.bindToLifecycle())

         .subscribe(new Action1() {

        @Override

        public void call(Long num) {

        Log.i(TAG, "  " + num);

        }

        });

 二、bindUntilEvent()方法

 使用ActivityEvent类，其中的CREATE、START、 RESUME、PAUSE、STOP、 DESTROY分别对应生命周期内的方法。使用bindUntilEvent指定在哪个生命周期方法调用时取消订阅。

         Observable.interval(1,TimeUnit.SECONDS)

         .compose(this.bindUntilEvent(ActivityEvent.PAUSE))

         .subscribe(mSub);

 作者：sword_
 链接：http://www.jianshu.com/p/a3ad9dd20655
 來源：简书
 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 *
 */

public class RxLifecycleUtils {

    private RxLifecycleUtils() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    /**
     * 绑定 Activity 的指定生命周期
     *
     * @param view
     * @param event
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindUntilEvent(@NonNull final IView view,
                                                             final ActivityEvent event) {
        Preconditions.checkNotNull(view, "view == null");
        if (view instanceof ActivityLifecycleable) {
            return bindUntilEvent((ActivityLifecycleable) view, event);
        } else {
            throw new IllegalArgumentException("view isn't ActivityLifecycleable");
        }
    }

    /**
     * 绑定 Fragment 的指定生命周期
     *
     * @param view
     * @param event
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindUntilEvent(@NonNull final IView view,
                                                             final FragmentEvent event) {
        Preconditions.checkNotNull(view, "view == null");
        if (view instanceof FragmentLifecycleable) {
            return bindUntilEvent((FragmentLifecycleable) view, event);
        } else {
            throw new IllegalArgumentException("view isn't FragmentLifecycleable");
        }
    }

    public static <T, R> LifecycleTransformer<T> bindUntilEvent(@NonNull final Lifecycleable<R> lifecycleable,
                                                                final R event) {
        Preconditions.checkNotNull(lifecycleable, "lifecycleable == null");
        return RxLifecycle.bindUntilEvent(lifecycleable.provideLifecycleSubject(), event);
    }


    /**
     * 绑定 Activity/Fragment 的生命周期
     *
     * ！！！
     * 什么时候生效呢？ UserPresenter中会调用 compose(RxLifecycleUtils.bindToLifecycle(mRootView))进行
     * Activity的绑定，然后ActivityLifecycleForRxLifecycle会进行事件的通知分发，然后RxLifecycleAndroid
     * 会收到这些通知。
     *
     * @param view
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindToLifecycle(@NonNull IView view) {
        Preconditions.checkNotNull(view, "view == null");
        if (view instanceof Lifecycleable) {
            return bindToLifecycle((Lifecycleable) view);
        } else {
            throw new IllegalArgumentException("view isn't Lifecycleable");
        }
    }

    public static <T> LifecycleTransformer<T> bindToLifecycle(@NonNull Lifecycleable lifecycleable) {
        Preconditions.checkNotNull(lifecycleable, "lifecycleable == null");
        if (lifecycleable instanceof ActivityLifecycleable) {
            return RxLifecycleAndroid.bindActivity(((ActivityLifecycleable) lifecycleable).provideLifecycleSubject());
        } else if (lifecycleable instanceof FragmentLifecycleable) {
            return RxLifecycleAndroid.bindFragment(((FragmentLifecycleable) lifecycleable).provideLifecycleSubject());
        } else {
            throw new IllegalArgumentException("Lifecycleable not match");
        }
    }

}
