package com.example.scraperGUI;

import backendd.appPrograms;
import javafx.concurrent.Task;

public class ProgramsScraper extends Task<Integer> {

    String filterPhrase;
    Boolean filterStat;
    Boolean filterNone;
    String filterType;

    public ProgramsScraper(String filterPhrase, Boolean filterStat, Boolean filterNone, String filterType){
        this.filterPhrase = filterPhrase;
        this.filterStat = filterStat;
        this.filterNone = filterNone;
        this.filterType = filterType;
    }

    @Override
    protected Integer call() throws Exception {
        appPrograms appPrograms = new appPrograms();

        int scrapedDataAmount = appPrograms.run(this.filterPhrase, this.filterStat, this.filterNone, this.filterType);
        updateValue(scrapedDataAmount);
        return scrapedDataAmount;
    }
}
