package com.carrerai.astrology.calculator;

import org.springframework.stereotype.Component;
import swisseph.SweConst;
import swisseph.SweDate;
import swisseph.SwissEph;

@Component
public class AyanamsaCalculator {

    private final SwissEph swissEph;

    public AyanamsaCalculator() {
        this.swissEph = new SwissEph("./ephe");
    }




    public double calculateLahiriAyanamsa(
            int year,
            int month,
            int day,
            double hour
    ) {

        SweDate sweDate = new SweDate(
                year,
                month,
                day,
                hour,
                SweDate.SE_GREG_CAL
        );

        double julianDay = sweDate.getJulDay();

        swissEph.swe_set_sid_mode(
                SweConst.SE_SIDM_LAHIRI,
                0,
                0
        );

        return swissEph.swe_get_ayanamsa_ut(julianDay);
    }

}