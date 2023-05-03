package com.example.scraperGUI;

import backendd.appDormitory;
import javafx.concurrent.Task;

public class DormitoryScraper extends Task<Integer> {
    String filter;

    public DormitoryScraper(String filter){
        this.filter = filter;
    }


    @Override
    protected Integer call() throws Exception {
        appDormitory appDormitory = new appDormitory();

        int scrapedDataAmount = appDormitory.run(this.filter);
        updateValue(scrapedDataAmount);
        return scrapedDataAmount;
    }
}
