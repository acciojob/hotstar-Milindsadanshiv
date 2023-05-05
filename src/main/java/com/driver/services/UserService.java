package com.driver.services;


import com.driver.model.*;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository
        userRepository.save(user);
        return user.getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository
         User user=userRepository.findById(userId).get();
         Subscription subscription=user.getSubscription();
         int ageLimit=user.getAge();

        ProductionHouse productionHouse=new ProductionHouse();
        List<WebSeries> webSeriesList=productionHouse.getWebSeriesList();

        Integer count=0;
        for (WebSeries webSeries:webSeriesList) {
            if (webSeries.getAgeLimit() <= ageLimit) {
                count++;
            }
        }
        return count;
    }


}
