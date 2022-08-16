package com.example.controller;

import com.example.entity.Task;
import com.example.mapper.TaskMapper;
import com.example.util.DynamicTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @description:
 * @author: zp
 * @create: 2022-07-20 21:24
 **/
@RestController
public class TaskController {

    @Autowired
    private DynamicTask dynamicTask;

    @Autowired
    private TaskMapper taskMapper;

    //存放元素
    private Set<Task> taskSet = new HashSet<>();

    public String dynamicTask(List<Task> tasks) {

        if (!CollectionUtils.isEmpty(tasks)) {
            tasks.forEach(task -> {
                //先判断状态，失效直接停止
                if (task.getStatus() == -1) {
                    dynamicTask.stop(task);
                } else {
                    dynamicTask.modify(task);
                }
            });
        }

        return "ok";
    }

    /**
     * 项目启动，读取任务列表，只执行一次。
     * @param tasks
     * @return
     */
//    @PostConstruct
//    public void init() {
//        System.out.println("初始化");
//        List<Task> list = taskMapper.getList();
//        list.forEach(task -> {
////            taskSet.add(task);
//            dynamicTask.add(task);
//        });
//    }

    /**
     * 项目启动，每隔一段时间读取一次任务列表。
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void Refresh() {
        System.out.println("定时任务刷新" + new Date());
//        List<Task> list = taskMapper.getList();
//        dynamicTask(list);
    }
}
