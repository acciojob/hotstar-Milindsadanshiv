package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo
        WebSeries webSeries=new WebSeries();

        webSeries=webSeriesRepository.findBySeriesName(webSeries.getSeriesName());
        if(webSeries!=null)
        {
            throw new Exception("Series is already present");
        }
        webSeries.setSeriesName(webSeries.getSeriesName());
        webSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webSeries.setRating(webSeriesEntryDto.getRating());
        webSeries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());


        //find production house
        ProductionHouse productionHouse=productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId()).get();

        List<WebSeries> webSeriesList=productionHouse.getWebSeriesList();
        webSeriesList.add(webSeries);


        double ratings=0;
        double sum=0;
        double count=0;
        for (WebSeries webSeries1:webSeriesList)
        {
            sum+=webSeries1.getRating();
            count++;
        }

        ratings=sum/count;
        productionHouse.setRatings(ratings);
        webSeries.setProductionHouse(productionHouse);
        productionHouse.setWebSeriesList(webSeriesList);

        productionHouseRepository.save(productionHouse);
        webSeriesRepository.save(webSeries);
        return null;
    }

}
