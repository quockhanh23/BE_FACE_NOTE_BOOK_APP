package com.example.final_case_social_web.controllertest;

import com.example.final_case_social_web.component.ThreadPoolExecutorUtil;
import com.example.final_case_social_web.model.Post2;
import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.repository.PostRepository;
import com.example.final_case_social_web.repository.UserRepository;
import com.example.final_case_social_web.thread.TaskThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ThreadPoolExecutorUtil threadPoolExecutorUtil;

    @GetMapping(path = "/list")
    public ResponseEntity<?> usingCompletableFuture() {
        doTask();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/listFlux")
    public ResponseEntity<?> usingFlux() {
        reactiveProgrammingSpringWebFlux();
        return new ResponseEntity<>(HttpStatus.OK);
    }


    public void doTask() {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        CompletableFuture<List<User>> future1 = CompletableFuture.supplyAsync(() -> {
            log.info(Thread.currentThread().getName() + ">>>>>>>>>>>>>>>>>");
            return userRepository.findAll();
        }, executor).handle((result, ex) -> {
            // Được gọi cho cả trường hợp thành công và thất bại của CompletableFuture.
            // Xử lý lỗi và trả về Exception
            if (ex != null) {
                throw new RuntimeException("Error fetching posts", ex);
            } else {
                return result;
            }
        });
        CompletableFuture<List<Post2>> future2 = CompletableFuture.supplyAsync(() -> {
            log.info(Thread.currentThread().getName() + ">>>>>>>>>>>>>>>>>");
            return postRepository.findAll();
        }, executor).exceptionally(ex -> {
            // Chỉ được gọi khi có ngoại lệ xảy ra trong CompletableFuture
            // Trả về giá trị mặc định nếu bị lỗi
            return new ArrayList<>();
        });
        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(future1, future2);
        combinedFuture.join();

        List<User> userList = future1.join();
        List<Post2> post2List = future2.join();

        executor.shutdown();
        if (!executor.isTerminated()) {
            log.warn("Executor did not terminate");
        }
    }

    private void reactiveProgrammingSpringWebFlux() {
        Flux<User> userFlux = Flux.defer(() -> Flux.fromIterable(userRepository.findAll()))
                .doOnComplete(() -> System.out.println("userFlux completed"));
        Flux<Post2> post2Flux = Flux.defer(() -> Flux.fromIterable(postRepository.findAll()))
                .doOnComplete(() -> System.out.println("post2Flux completed"));
        // Gom 2 list trên thành 1 danh sách kết hợp
        List<?> list = Flux.zip(userFlux, post2Flux).collectList().block();
        Mono<List<User>> userList = userFlux.collectList();
        List<User> users = userList.block();
        Mono<List<Post2>> listMono = post2Flux.collectList();
        List<Post2> post2List = listMono.block();
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
