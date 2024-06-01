package com.example.final_case_social_web.controllertest;

import com.example.final_case_social_web.component.ThreadPoolExecutorUtil;
import com.example.final_case_social_web.model.FollowWatching;
import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.repository.FollowWatchingRepository;
import com.example.final_case_social_web.repository.UserRepository;
import com.example.final_case_social_web.service.UserService;
import com.example.final_case_social_web.thread.TaskThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/async")
@Slf4j
public class AsyncController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FollowWatchingRepository followWatchingRepository;
    @Autowired
    private ThreadPoolExecutorUtil threadPoolExecutorUtil;

    @GetMapping(path = "/list/async")
    public List<User> getAllUsersAsync() {
        doTask();
        return new ArrayList<>();
    }

    public void doTask() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        CompletableFuture<List<User>> future1 = CompletableFuture.supplyAsync(() -> {
            log.info(Thread.currentThread().getName() + ">>>>>>>>>>>>>>>>>");
            return userRepository.findAll();
        }, executor);
        CompletableFuture<List<FollowWatching>> future2 = CompletableFuture.supplyAsync(() -> {
            log.info(Thread.currentThread().getName() + ">>>>>>>>>>>>>>>>>");
            return followWatchingRepository.findAll();
        }, executor).exceptionally(ex -> {
            // Xử lý lỗi ở đây
            return null;
        });
        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(future1, future2);
        combinedFuture.join();

        List<User> userList = future1.join();
        List<FollowWatching> followWatchingList = future2.join();

        executor.shutdown();
        if (!executor.isTerminated()) {
            log.warn("Executor did not terminate");
        }
    }

    private void reactiveProgrammingSpringWebFlux() {
        Flux<User> userFlux = Flux.defer(() -> Flux.fromIterable(userRepository.findAll()));
        Flux<FollowWatching> followWatchingFlux = Flux.defer(() -> Flux.fromIterable(followWatchingRepository.findAll()));
        Flux.zip(userFlux, followWatchingFlux)
                .collectList()
                .block();
    }

    public List<User> getUsersAsync() {
        for (int i = 0; i < 5; i++) {
            TaskThread taskThread = new TaskThread(userRepository);
            threadPoolExecutorUtil.executeTask(taskThread);
        }
        TaskThread taskThread = new TaskThread(userRepository);
        threadPoolExecutorUtil.executeTask(taskThread);
        taskThread.run();
        return taskThread.users;
    }
}
