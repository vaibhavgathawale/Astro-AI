package com.carrerai.astrology.calculator;

import org.springframework.stereotype.Component;

@Component
public class SiderealCalculator {

    public double calculateSiderealLongitude(
            double tropicalLongitude,
            double ayanamsa
    ) {

        double siderealLongitude =
                tropicalLongitude - ayanamsa;

        if (siderealLongitude < 0) {
            siderealLongitude += 360;
        }

        if (siderealLongitude >= 360) {
            siderealLongitude -= 360;
        }

        return siderealLongitude;
    }

    public String calculateSign(double siderealLongitude) {

        String[] signs = {
                "Aries",
                "Taurus",
                "Gemini",
                "Cancer",
                "Leo",
                "Virgo",
                "Libra",
                "Scorpio",
                "Sagittarius",
                "Capricorn",
                "Aquarius",
                "Pisces"
        };

        int index = (int) (siderealLongitude / 30);

        return signs[index];
    }
}