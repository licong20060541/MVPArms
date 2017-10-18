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
package me.jessyan.mvparms.demo.mvp.model.api.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.LifeCache;
import io.rx_cache2.Reply;
import io.rx_cache2.internal.RxCache;
import me.jessyan.mvparms.demo.mvp.model.entity.User;

/**
 * ================================================
 * 展示 {@link RxCache#using(Class)} 中需要传入的 Providers 的使用方式
 * <p>
 * Created by JessYan on 08/30/2016 13:53
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface CommonCache {

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<Reply<List<User>>> getUsers(Observable<List<User>> users, DynamicKey idLastUserQueried, EvictProvider evictProvider);
}


/**
 RxCache

 是使用注解为Retrofit加入二级缓存(内存,磁盘)的缓存库。 开头膜拜大神
 项目地址 : RxCache

 RxCache使用方法
 定义接口

 public interface CacheProviders {

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<Reply<List<Repo>>> getRepos(Observable<List<Repo>> oRepos, DynamicKey userName, EvictDynamicKey evictDynamicKey);

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<Reply<List<User>>> getUsers(Observable<List<User>> oUsers, DynamicKey idLastUserQueried, EvictProvider evictProvider);

    Observable<Reply<User>> getCurrentUser(Observable<User> oUser, EvictProvider evictProvider);
}

 十分简洁，使用了注解@LifeCache声明了缓存的超时时间（duration长度,timeUnit时间单位），接口定义了之后，如何实例化这个接口，看下面。
 创建CacheProviders对象

 providers = new RxCache.Builder()
 .persistence(cacheDir, new GsonSpeaker())
 .using(Providers.class);

 之后的使用和Retrofit无异

 实例化的过程是比较常见的Builder模式，和Retrofit的API的实例化的方式很像，调用using()就创建了接口的实例，
 和Retrofit的create()方法也十分相似，当然内部实现也很相似（都是使用了动态代理）。

 作者：srtianxia
 链接：http://www.jianshu.com/p/5d73909c7068
 來源：简书
 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
