package com.nwt.juber.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class TaskScheduling {

    private final TaskScheduler executor;

    @Autowired
    public TaskScheduling(TaskScheduler taskExecutor) {
        this.executor = taskExecutor;
    }

    public void scheduling(final Runnable task, LocalDateTime time) {
        executor.schedule(task, Date.from(time
                .atZone(ZoneId.systemDefault()).toInstant()));
    }

}