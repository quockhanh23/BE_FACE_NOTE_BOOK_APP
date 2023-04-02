package com.example.final_case_social_web.controller;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.model.Post2;
import com.example.final_case_social_web.model.ReportViolations;
import com.example.final_case_social_web.model.TheGroup;
import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.notification.ResponseNotification;
import com.example.final_case_social_web.repository.ReportRepository;
import com.example.final_case_social_web.service.PostService;
import com.example.final_case_social_web.service.TheGroupService;
import com.example.final_case_social_web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/reposts")
@Slf4j
public class ReportViolationsController {

    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private TheGroupService theGroupService;
    @Autowired
    private ReportRepository reportRepository;

    @PostMapping("/createRepost")
    public ResponseEntity<?> createRepost(@RequestBody ReportViolations report,
                                          @RequestParam Long idUserRepost,
                                          @RequestParam Long idViolate,
                                          @RequestParam String type) {
        Optional<User> userRepost = userService.findById(idUserRepost);
        if (!userRepost.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUserRepost), HttpStatus.NOT_FOUND);
        }
        if (Constants.REPOST_TYPE_USER.equalsIgnoreCase(type)) {
            Optional<User> userViolate = userService.findById(idViolate);
            if (!userViolate.isPresent()) {
                return new ResponseEntity<>(ResponseNotification.
                        responseMessage(Constants.IdCheck.ID_USER, idViolate), HttpStatus.NOT_FOUND);
            }
            report.setIdViolate(userViolate.get().getId());
            report.setType(Constants.REPOST_TYPE_USER);
        }
        if (Constants.REPOST_TYPE_POST.equalsIgnoreCase(type)) {
            Optional<Post2> postOptional = postService.findById(idViolate);
            if (!postOptional.isPresent()) {
                return new ResponseEntity<>(ResponseNotification.
                        responseMessage(Constants.IdCheck.ID_POST, idViolate), HttpStatus.NOT_FOUND);
            }
            report.setIdViolate(postOptional.get().getId());
            report.setType(Constants.REPOST_TYPE_POST);
        }
        if (Constants.REPOST_TYPE_GROUP.equalsIgnoreCase(type)) {
            Optional<TheGroup> theGroupOptional = theGroupService.findById(idViolate);
            if (!theGroupOptional.isPresent()) {
                return new ResponseEntity<>(ResponseNotification.
                        responseMessage(Constants.IdCheck.ID_GROUP, idViolate), HttpStatus.NOT_FOUND);
            }
            report.setIdViolate(theGroupOptional.get().getId());
            report.setType(Constants.REPOST_TYPE_GROUP);
        }
        report.setCreateAt(new Date());
        report.setStatus("R");
        report.setIdUserReport(userRepost.get().getId());
        reportRepository.save(report);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/unRepost")
    public ResponseEntity<?> unRepost(@RequestParam Long idUser, @RequestParam Long idViolate) {
        List<ReportViolations> reportViolations = reportRepository.findAllByIdUserReportAndIdViolate(idUser, idViolate);
        if (!CollectionUtils.isEmpty(reportViolations)) {
            reportRepository.delete(reportViolations.get(0));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/editContent")
    public ResponseEntity<?> editContent(@RequestParam Long idUser,
                                         @RequestParam Long idViolate,
                                         @RequestBody ReportViolations report) {
        List<ReportViolations> reportViolations = reportRepository.findAllByIdUserReportAndIdViolate(idUser, idViolate);
        if (!CollectionUtils.isEmpty(reportViolations)) {
            ReportViolations reportViolation = reportViolations.get(0);
            reportViolation.setContent(report.getContent());
            reportViolation.setEditAt(new Date());
            reportRepository.save(reportViolation);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/findOneRepost")
    public ResponseEntity<?> findOneRepost(@RequestParam Long idUser, @RequestParam Long idViolate) {
        List<ReportViolations> reportViolations = reportRepository.findAllByIdUserReportAndIdViolate(idUser, idViolate);
        if (!CollectionUtils.isEmpty(reportViolations)) {
            ReportViolations reportViolation = reportViolations.get(0);
            return new ResponseEntity<>(reportViolation, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
