package com.astroai.astrology.calculator;

import org.springframework.stereotype.Component;
import swisseph.SweConst;
import swisseph.SweDate;
import swisseph.SwissEph;

@Component
public class HouseCalculator {

    private final SwissEph swissEph;
    private final AyanamsaCalculator ayanamsaCalculator;

    public HouseCalculator(AyanamsaCalculator ayanamsaCalculator) {
        this.swissEph = new SwissEph("./ephe");
        this.ayanamsaCalculator = ayanamsaCalculator;
    }


    public double calculateSiderealAscendant(
            int year,
            int month,
            int day,
            double hour,
            double latitude,
            double longitude,
            double ayanamsa
    ) {

        double tropicalAscendant = calculateAscendant(
                year,
                month,
                day,
                hour,
                latitude,
                longitude
        );

        double siderealAscendant =
                tropicalAscendant - ayanamsa;

        if (siderealAscendant < 0) {
            siderealAscendant += 360.0;
        }

        return siderealAscendant;
    }

    public double[] calculateSiderealHouses(
            int year,
            int month,
            int day,
            double hour,
            double latitude,
            double longitude,
            double ayanamsa
    ) {

        double[] tropicalHouses = calculateHouses(
                year,
                month,
                day,
                hour,
                latitude,
                longitude
        );

        double[] siderealHouses = new double[13];

        for (int i = 1; i <= 12; i++) {

            double siderealLongitude =
                    tropicalHouses[i] - ayanamsa;

            if (siderealLongitude < 0) {
                siderealLongitude += 360.0;
            }

            siderealHouses[i] = siderealLongitude;
        }

        return siderealHouses;
    }

    public double[] calculateHouses(
            int year,
            int month,
            int day,
            double hour,
            double latitude,
            double longitude
    ) {

        SweDate sweDate = new SweDate(
                year,
                month,
                day,
                hour,
                SweDate.SE_GREG_CAL
        );

        double julianDay = sweDate.getJulDay();

        double[] cusps = new double[13];
        double[] ascmc = new double[10];

        int result = swissEph.swe_houses(
                julianDay,
                SweConst.SEFLG_SWIEPH,
                latitude,
                longitude,
                'P',
                cusps,
                ascmc
        );

        if (result < 0) {
            throw new RuntimeException(
                    "House calculation failed"
            );
        }

        // Calculate Lahiri Ayanamsa
        double ayanamsa =
                ayanamsaCalculator.calculateLahiriAyanamsa(
                        year,
                        month,
                        day,
                        hour
                );

        /*
         * Convert tropical house cusps
         * into sidereal house cusps.
         */
        double[] siderealCusps = new double[13];

        for (int i = 1; i <= 12; i++) {

            siderealCusps[i] =
                    normalizeLongitude(
                            cusps[i] - ayanamsa
                    );
        }

        return siderealCusps;
    }

    public double calculateAscendant(
            int year,
            int month,
            int day,
            double hour,
            double latitude,
            double longitude
    ) {

        SweDate sweDate = new SweDate(
                year,
                month,
                day,
                hour,
                SweDate.SE_GREG_CAL
        );

        double julianDay = sweDate.getJulDay();

        double[] cusps = new double[13];
        double[] ascmc = new double[10];

        int result = swissEph.swe_houses(
                julianDay,
                SweConst.SEFLG_SWIEPH,
                latitude,
                longitude,
                'P',
                cusps,
                ascmc
        );

        if (result < 0) {
            throw new RuntimeException(
                    "Ascendant calculation failed"
            );
        }

        double ayanamsa =
                ayanamsaCalculator.calculateLahiriAyanamsa(
                        year,
                        month,
                        day,
                        hour
                );

        // Tropical Ascendant
        double tropicalAscendant = ascmc[0];

        // Sidereal Ascendant
        return normalizeLongitude(
                tropicalAscendant - ayanamsa
        );
    }

    private double normalizeLongitude(double longitude) {

        longitude = longitude % 360.0;

        if (longitude < 0) {
            longitude += 360.0;
        }

        return longitude;
    }
}