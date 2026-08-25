package com.carrerai.astrology.calculator;

import org.springframework.stereotype.Component;
import swisseph.SweConst;
import swisseph.SweDate;
import swisseph.SwissEph;

@Component
public class SwissEphemerisCalculator {

    private final SwissEph swissEph;

    public SwissEphemerisCalculator() {
        this.swissEph = new SwissEph("./ephe");
    }

    public double[] calculatePlanet(
            int year,
            int month,
            int day,
            double hour,
            int planetId
    ) {

        SweDate sweDate = new SweDate(
                year,
                month,
                day,
                hour,
                SweDate.SE_GREG_CAL
        );

        double[] xx = new double[6];

        StringBuffer serr = new StringBuffer();

        int flags = SweConst.SEFLG_SWIEPH
                | SweConst.SEFLG_SPEED;

        int result = swissEph.swe_calc_ut(
                sweDate.getJulDay(),
                planetId,
                flags,
                xx,
                serr
        );

        if (result < 0) {
            throw new RuntimeException(
                    "Planet calculation failed: " + serr
            );
        }

        return xx;
    }
}