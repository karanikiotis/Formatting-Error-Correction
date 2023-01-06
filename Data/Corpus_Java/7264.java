package com.shizhefei.task.tasks;


import com.shizhefei.mvc.IAsyncDataSource;
import com.shizhefei.mvc.IDataSource;
import com.shizhefei.task.IAsyncTask;
import com.shizhefei.task.ITask;
import com.shizhefei.task.function.Func1;
import com.shizhefei.task.function.Func2;

/**
 * Created by luckyjayce on 2017/4/16.
 */

public class Tasks {

    /**
     * 将一个同步task转化为异步task
     * @param task
     * @param <DATA>
     * @return
     */
    public static <DATA> LinkTask<DATA> async(ITask<DATA> task) {
        return new AsyncLinkTask<>(task, true);
    }

    public static <DATA> LinkTask<DATA> just(DATA data) {
        return new DataLinkTask<>(data);
    }

    /**
     * 通过同步task创建一个链式task
     * @param task
     * @param <DATA>
     * @return 一个链式task
     */
    public static <DATA> LinkTask<DATA> create(ITask<DATA> task) {
        return new AsyncLinkTask<>(task, true);
    }

    /**
     * 通过异步task创建一个链式task
     * @param task
     * @param <DATA>
     * @return 一个链式task
     */
    public static <DATA> LinkTask<DATA> create(IAsyncTask<DATA> task) {
        if (task instanceof LinkTask) {
            return (LinkTask<DATA>) task;
        }
        return new LinkProxyTask<>(task);
    }

    /**
     * 通过一个同步dataSource创建一个链式task
     * @param dataSource
     * @param isExeRefresh true：dataSource的refresh转成task，false：dataSource的loadMore转成task
     * @param <DATA>
     * @return 一个链式task
     */
    public static <DATA> LinkTask<DATA> create(IDataSource<DATA> dataSource, boolean isExeRefresh) {
        return new AsyncLinkTask<>(dataSource, isExeRefresh);
    }

    /**
     * 通过一个异步dataSource创建一个链式task
     * @param dataSource
     * @param isExeRefresh true：dataSource的refresh转成task，false：dataSource的loadMore转成task
     * @param <DATA>
     * @return 一个链式task
     */
    public static <DATA> LinkTask<DATA> create(IAsyncDataSource<DATA> dataSource, boolean isExeRefresh) {
        return new AsyncLinkTask<>(dataSource, isExeRefresh);
    }

    /**
     * 按两个task先后顺序执行，task1的结果可以作为task的参数
     * @param task task1
     * @param func 将task的结果传给task2，返回一个可执行task2
     * @param <D> 数据1
     * @param <DATA> 结果数据
     * @return
     */
    public static <D, DATA> LinkTask<DATA> concatMap(IAsyncTask<D> task, Func1<D, IAsyncTask<DATA>> func) {
        return new ConcatLinkTask<>(task, func);
    }

    /**
     * 按两个task先后顺序执行
     *
     * @param task
     * @param task2
     * @param <D>
     * @param <DATA>
     * @return
     */
    public static <D, DATA> LinkTask<DATA> concatWith(IAsyncTask<D> task, final IAsyncTask<DATA> task2) {
        return new ConcatLinkTask<>(task, new Func1<D, IAsyncTask<DATA>>() {
            @Override
            public IAsyncTask<DATA> call(D data) throws Exception {
                return task2;
            }
        });
    }

    /**
     * 合并两个task，两个task一起执行，其中一个报错就停止执行，func将两个d1和d2的结果转化成最终的data
     * @param task1
     * @param task2
     * @param func
     * @param <D1>
     * @param <D2>
     * @param <DATA>
     * @return
     */
    public static <D1, D2, DATA> LinkTask<DATA> combine(IAsyncTask<D1> task1, IAsyncTask<D2> task2, Func2<D1, D2, DATA> func) {
        return new CombineTask<>(task1, task2, func);
    }
}
