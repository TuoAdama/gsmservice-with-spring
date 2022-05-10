package com.example.main.configurations;

import com.example.main.services.TransfertService;
import com.example.main.utils.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
public class JobCronConfigMakeTransfert implements SchedulingConfigurer {

    @Autowired
    private TransfertService transfertService;
    @Autowired
    private Config config;

    @Autowired
    private Executor executor;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(executor);
        taskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                transfertService.makeTransfert();
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                Optional<Date> lastCompletionTime = Optional.ofNullable(triggerContext.lastCompletionTime());
                Instant nextExecution = lastCompletionTime.orElseGet(Date::new).toInstant()
                        .plusMillis(Long.parseLong(config.getExecutionTimeMakeTransfert()));
                return Date.from(nextExecution);
            }
        });
    }
}
