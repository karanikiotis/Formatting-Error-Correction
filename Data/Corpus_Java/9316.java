package com.app.mvc.common;

import com.app.mvc.schedule.AutoRegisterScheduleJob;
import com.app.mvc.schedule.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by jimin on 15/11/7.
 */
@Slf4j
@Component("applicationContextHelper")
public class ApplicationContextHelper implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringHelper.setApplicationContext(applicationContext);
        InitData.start();
        AutoRegisterScheduleJob.registerWhenStartUp();
        ScheduleService scheduleService = SpringHelper.popBean(ScheduleService.class);
        scheduleService.scheduleAll();
    }
}