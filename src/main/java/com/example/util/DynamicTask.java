package com.example.util;

import com.example.config.ManageSpringBean;
import com.example.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * @description:
 * @author: zp
 * @create: 2022-07-20 21:23
 **/
@Component
public class DynamicTask {

    @Autowired
    private ManageSpringBean manageSpringBean;

    private ThreadPoolTaskScheduler threadPoolTaskScheduler = manageSpringBean.getBean(ThreadPoolTaskScheduler.class);;

    public Map<Integer, ScheduledFuture<?>> taskMap = new HashMap<>();

    /**
     * 添加任务
     *
     * @param task
     * @return
     */
    public boolean add(Task task) {
        if (null != taskMap.get(task) || !CronExpression.isValidExpression(task.getCron())) {
            return false;
        }
        ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(getRunnable(task), new CronTrigger(task.getCron()));
        taskMap.put(task.getId(), schedule);
        return true;
    }

    /**
     * 修改任务
     * 如果没有任务则新增，如果有则取消后在新增。
     * @param task
     * @return
     */
    public boolean modify(Task task) {
        if (null == taskMap.get(task.getId())) {
            add(task);
        }else {
            stop(task);
            add(task);
        }
        return true;
    }

    /**
     * 停止任务
     *
     * @param task
     * @return
     */
    public boolean  stop(Task task) {
        if (null == taskMap.get(task.getId())) {
            return false;
        }
        ScheduledFuture<?> scheduledFuture = taskMap.get(task.getId());
        scheduledFuture.cancel(false);
        taskMap.remove(task.getId());
        return true;
    }


    public Runnable getRunnable(Task task) {
        return new Runnable() {
            @Override
            public void run() {
                System.out.println(task + "---动态定时任务运行完毕---" + new Date());
            }
        };
    }


}
