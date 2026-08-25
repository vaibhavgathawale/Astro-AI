package com.astroai.astrology.calculator;

import org.springframework.stereotype.Component;

@Component
public class RashiCalculator {

    private static final String[] RASHIS = {
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

    public String calculateRashi(double siderealLongitude) {

        int index = (int) (siderealLongitude / 30);

        return RASHIS[index];
    }
}