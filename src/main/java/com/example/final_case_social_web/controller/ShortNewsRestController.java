package com.example.final_case_social_web.controller;

import com.example.final_case_social_web.common.Constants;
import com.example.final_case_social_web.dto.ShortNewsDTO;
import com.example.final_case_social_web.dto.UserDTO;
import com.example.final_case_social_web.model.ShortNews;
import com.example.final_case_social_web.model.User;
import com.example.final_case_social_web.notification.ResponseNotification;
import com.example.final_case_social_web.service.ShortNewsService;
import com.example.final_case_social_web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin("*")
@RequestMapping("/api/news")
@Slf4j
public class ShortNewsRestController {
    @Autowired
    private ShortNewsService shortNewsService;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    // Lưu ngày mới
    @GetMapping("/newDay")
    public ResponseEntity<Iterable<ShortNews>> newDay() {
        Iterable<ShortNews> shortNews = shortNewsService.findAll();
        List<ShortNews> shortNewsList;
        shortNewsList = (List<ShortNews>) shortNews;
        for (ShortNews news : shortNewsList) {
            news.setToDay(new Date());
        }
        shortNewsService.saveAll(shortNewsList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 5 tin mới nhất
    @GetMapping("/shortNewsLimit")
    public ResponseEntity<List<ShortNews>> shortNewsLimit() {
        List<ShortNews> shortNews = shortNewsService.findAllShortNews();
        if (CollectionUtils.isEmpty(shortNews)) {
            shortNews = new ArrayList<>();
        }
        return new ResponseEntity<>(shortNews, HttpStatus.OK);
    }

    // Tin của tôi
    @GetMapping("/myShortNews")
    public ResponseEntity<?> myShortNews(@RequestParam Long idUser) {
        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        List<ShortNews> shortNews = shortNewsService.myShortNew(idUser);
        if (CollectionUtils.isEmpty(shortNews)) {
            shortNews = new ArrayList<>();
        }
        return new ResponseEntity<>(shortNews, HttpStatus.OK);
    }

    // Tất cả tin công khai
    @GetMapping("/allShortNewPublic")
    public ResponseEntity<?> allShortNewPublic() {
        List<ShortNews> shortNews = shortNewsService.findAllShortNewsPublic();
        List<ShortNewsDTO> shortNewsDTOList = new ArrayList<>();
        if (shortNews != null) {
            for (ShortNews news : shortNews) {
                UserDTO userDTO = modelMapper.map(news.getUser(), UserDTO.class);
                ShortNewsDTO shortNewsDTO = modelMapper.map(news, ShortNewsDTO.class);
                shortNewsDTO.setUserDTO(userDTO);
                shortNewsDTOList.add(shortNewsDTO);
            }
        }
        return new ResponseEntity<>(shortNewsDTOList, HttpStatus.OK);
    }

    // Kiểm tra hạn sử dụng
    @GetMapping("/allShortNews")
    public ResponseEntity<Iterable<ShortNewsDTO>> allShortNews() {
        Iterable<ShortNews> shortNews = shortNewsService.findAll();
        List<ShortNewsDTO> shortNewsDTOList = new ArrayList<>();
        List<ShortNews> shortNewsList;
        shortNewsList = (List<ShortNews>) shortNews;
        if (shortNews != null) {
            for (int i = 0; i < shortNewsList.size(); i++) {

                int today = Integer.parseInt(shortNewsList.get(i).getToDay().toString().substring(8, 10));
                int createDay = Integer.parseInt(shortNewsList.get(i).getCreateAt().toString().substring(8, 10));

                int monthToday = Integer.parseInt(shortNewsList.get(i).getToDay().toString().substring(5, 7));
                int monthCreate = Integer.parseInt(shortNewsList.get(i).getCreateAt().toString().substring(5, 7));

                int yearToday = Integer.parseInt(shortNewsList.get(i).getToDay().toString().substring(0, 4));
                int yearCreate = Integer.parseInt(shortNewsList.get(i).getCreateAt().toString().substring(0, 4));

                int totalDay = 0;

                Instant instant = Instant.ofEpochMilli(shortNewsList.get(i).getCreateAt().getTime());
                LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                Instant instant1 = Instant.ofEpochMilli(shortNewsList.get(i).getToDay().getTime());
                LocalDateTime localDateTime1 = LocalDateTime.ofInstant(instant1, ZoneId.systemDefault());

                LocalDate localDate = LocalDate.from(localDateTime);
                LocalDate localDate1 = LocalDate.from(localDateTime1);

                Period period = Period.between(localDate, localDate1);

                int getDay = period.getDays();
                int getMonth = period.getMonths();
                int getYear = period.getYears();

                if (getMonth > 0) {
                    int[] getMonthArr = new int[getMonth];
                    if (getMonth == 1) {
                        getMonthArr[0] = 1;
                    }
                    if (getMonth >= 2) {
                        int value = 1;
                        for (int j = 0; j < getMonth; j++) {
                            getMonthArr[j] = value;
                            value++;
                        }
                    }

                    int[] month31Day = {1, 3, 5, 7, 8, 10, 12};
                    int[] month30Day = {4, 6, 9, 11};
                    int month28DayOr29Day = 2;

                    for (int k = 0; k < getMonthArr.length; k++) {
                        for (int j = 0; j < month31Day.length; j++) {
                            if (monthCreate + getMonthArr[k] == month31Day[j]) {
                                if (getMonthArr[k] >= 2) {
                                    getDay = 0;
                                }
                                getDay = getDay + 31;
                            }
                        }
                        for (int j = 0; j < month30Day.length; j++) {
                            if (monthCreate + getMonthArr[k] == month30Day[j]) {
                                if (getMonthArr[k] >= 2) {
                                    getDay = 0;
                                }
                                getDay = getDay + 30;
                            }
                        }
                        if (monthCreate + getMonthArr[k] == month28DayOr29Day) {
                            if (yearCreate % 400 == 0) {
                                if (getMonthArr[k] >= 2) {
                                    getDay = 0;
                                }
                                getDay = getDay + 29;
                            } else if (yearCreate % 100 == 0) {
                                if (getMonthArr[k] >= 2) {
                                    getDay = 0;
                                }
                                getDay = getDay + 28;
                            } else if (yearCreate % 4 == 0) {
                                if (getMonthArr[k] >= 2) {
                                    getDay = 0;
                                }
                                getDay = getDay + 29;
                            } else {
                                if (getMonthArr[k] >= 2) {
                                    getDay = 0;
                                }
                                getDay = getDay + 28;
                            }
                        }
                        totalDay = totalDay + getDay;
                    }
                }

                int dayOfYear = 0;
                int totalDayOfYear = 0;
                if (getYear > 0) {
                    int[] getYearArr = new int[getYear];
                    if (getYear == 1) {
                        getYearArr[0] = 1;
                        if (shortNewsService.checkYear(yearCreate)) {
                            dayOfYear = 366;
                        } else {
                            dayOfYear = 365;
                        }
                    }
                    if (getYear >= 2) {
                        int value = yearCreate;
                        for (int j = 0; j < getYear; j++) {
                            getYearArr[j] = value;
                            if (shortNewsService.checkYear(getYearArr[j])) {
                                dayOfYear = 366;
                            } else {
                                dayOfYear = 365;
                            }
                            value++;
                            totalDayOfYear = totalDayOfYear + dayOfYear;
                        }
                    }
                }

                if (getMonth == 0 && getYear == 0) {
                    shortNewsList.get(i).setRemaining(shortNewsList.get(i).getExpired() - getDay);
                }
                if (getMonth > 0 && getYear == 0) {
                    shortNewsList.get(i).setRemaining(shortNewsList.get(i).getExpired() - totalDay);
                }
                if (getMonth == 0 && getYear == 1) {
                    shortNewsList.get(i).setRemaining(shortNewsList.get(i).getExpired() - dayOfYear);
                }
                if (getMonth == 0 && getYear > 1) {
                    shortNewsList.get(i).setRemaining(shortNewsList.get(i).getExpired() - totalDayOfYear);
                }
                if (getMonth > 0 && getYear > 0) {
                    int totalDayOfYearAndMonth = totalDayOfYear + totalDay;
                    shortNewsList.get(i).setRemaining(shortNewsList.get(i).getExpired() - totalDayOfYearAndMonth);
                }
            }
            shortNewsService.saveAll(shortNewsList);
            for (ShortNews news : shortNewsList) {
                UserDTO userDTO = modelMapper.map(news.getUser(), UserDTO.class);
                ShortNewsDTO shortNewsDTO = modelMapper.map(news, ShortNewsDTO.class);
                shortNewsDTO.setUserDTO(userDTO);
                shortNewsDTOList.add(shortNewsDTO);
            }
        }
        return new ResponseEntity<>(shortNewsDTOList, HttpStatus.OK);
    }

    // Tạo tin
    @PostMapping("/createShortNews")
    public ResponseEntity<?> createShortNews(@RequestBody ShortNews shortNews,
                                             @RequestParam Long idUser) {

        if (userService.checkUser(idUser) == null) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        shortNewsService.createShortNews(shortNews);
        if (shortNews.getImage().equals(Constants.ImageDefault.DEFAULT_IMAGE_SHORT_NEW)) {
            if (shortNews.getContent() == null || shortNews.getContent().equals("")) {
                return new ResponseEntity<>(ResponseNotification.responseMessageDataField(Constants.DataField.CONTENT),
                        HttpStatus.BAD_REQUEST);
            }
        }
        if (shortNews.getStatus().equals("") || shortNews.getStatus() == null) {
            shortNews.setStatus(Constants.STATUS_PUBLIC);
        }
        shortNews.setDelete(false);
        shortNews.setUser(userService.checkUser(idUser));
        shortNewsService.save(shortNews);
        return new ResponseEntity<>(shortNews.getContent() + shortNews.getImage() + shortNews.getUser().getId(),
                HttpStatus.CREATED);
    }

    // Xóa tin nhưng vẫn còn trong thùng rác
    @DeleteMapping("/deleteShortNews")
    public ResponseEntity<?> deleteShortNews(@RequestParam Long idSortNew,
                                             @RequestParam Long idUser) {

        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        Optional<ShortNews> shortNewsOptional = shortNewsService.findById(idSortNew);
        if (!shortNewsOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_SORT_NEW, idSortNew), HttpStatus.NOT_FOUND);
        }
        if (userOptional.get().getId().equals(shortNewsOptional.get().getUser().getId())) {
            shortNewsOptional.get().setDelete(true);
            shortNewsService.save(shortNewsOptional.get());
            return new ResponseEntity<>(shortNewsOptional.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    // Xóa tin trong database
    @DeleteMapping("/deleteShortNews2")
    public ResponseEntity<?> deleteShortNews2(@RequestParam Long idSortNew,
                                              @RequestParam Long idUser) {

        Optional<User> userOptional = userService.findById(idUser);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_USER, idUser), HttpStatus.NOT_FOUND);
        }
        Optional<ShortNews> shortNewsOptional = shortNewsService.findById(idSortNew);
        if (!shortNewsOptional.isPresent()) {
            return new ResponseEntity<>(ResponseNotification.
                    responseMessage(Constants.IdCheck.ID_SORT_NEW, idSortNew), HttpStatus.NOT_FOUND);
        }
        if (userOptional.get().getId().equals(shortNewsOptional.get().getUser().getId())) {
            shortNewsService.delete(shortNewsOptional.get());
            return new ResponseEntity<>(shortNewsOptional.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
