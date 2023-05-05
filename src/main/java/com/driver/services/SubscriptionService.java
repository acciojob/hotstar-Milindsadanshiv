package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        Subscription subscription=new Subscription();
        User user=userRepository.findById(subscriptionEntryDto.getUserId()).get();

        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        subscription.setStartSubscriptionDate(new Date());

        //for total amount paid
        Integer totalAmount;
        String subscriptionType1=String.valueOf(subscription.getSubscriptionType());

        if (subscriptionType1.equals("BASIC"))
        {
            totalAmount=500+(subscription.getNoOfScreensSubscribed()*200);
        }
        else if(subscriptionType1.equals("PRO"))
        {
            totalAmount=800+(subscription.getNoOfScreensSubscribed()*250);
        }
        else
        {
            totalAmount=1000+(subscription.getNoOfScreensSubscribed()*350);
        }
        subscription.setTotalAmountPaid(totalAmount);
        user.setSubscription(subscription);
        subscription.setUser(user);
        subscriptionRepository.save(subscription);
        userRepository.save(user);


        return totalAmount;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user=userRepository.findById(userId).get();
        Subscription subscription=user.getSubscription();

        String subscriptionType1=String.valueOf(subscription.getSubscriptionType());

        //conditions apply
        if (subscriptionType1.equals("ELITE"))
        {
            throw new Exception("Already the best Subscription");
        }
        Integer diffAmount;
        if(subscriptionType1.equals("BASIC"))
        {
            diffAmount=(800+(subscription.getNoOfScreensSubscribed()*250))-(subscription.getTotalAmountPaid());
            subscription.setSubscriptionType(SubscriptionType.PRO);
            subscription.setTotalAmountPaid(800+(subscription.getNoOfScreensSubscribed()*250));
        }
        else {
            diffAmount=(1000+(subscription.getNoOfScreensSubscribed()*350))-(subscription.getTotalAmountPaid());
            subscription.setSubscriptionType(SubscriptionType.ELITE);
            subscription.setTotalAmountPaid(1000+(subscription.getNoOfScreensSubscribed()*350));
        }
        user.setSubscription(subscription);
        subscription.setUser(user);
        subscriptionRepository.save(subscription);
        userRepository.save(user);
        return diffAmount;

    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        List<User> userList=userRepository.findAll();
        Integer totalRevenue=0;

        for (User user:userList)
        {
            totalRevenue+=user.getSubscription().getTotalAmountPaid();
        }


        return totalRevenue;
    }

}
