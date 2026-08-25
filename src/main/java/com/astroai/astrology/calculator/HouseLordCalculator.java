package com.astroai.astrology.calculator;

import com.astroai.astrology.model.HouseLordPosition;
import com.astroai.astrology.model.HousePosition;
import com.astroai.astrology.model.Planet;
import com.astroai.astrology.model.PlanetHousePosition;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HouseLordCalculator {

    public Planet calculateHouseLord(String sign) {

        return switch (sign) {

            // Fire signs
            case "Aries" ->
                    Planet.MARS;

            case "Leo" ->
                    Planet.SUN;

            case "Sagittarius" ->
                    Planet.JUPITER;


            // Earth signs
            case "Taurus" ->
                    Planet.VENUS;

            case "Virgo" ->
                    Planet.MERCURY;

            case "Capricorn" ->
                    Planet.SATURN;


            // Air signs
            case "Gemini" ->
                    Planet.MERCURY;

            case "Libra" ->
                    Planet.VENUS;

            case "Aquarius" ->
                    Planet.SATURN;


            // Water signs
            case "Cancer" ->
                    Planet.MOON;

            case "Scorpio" ->
                    Planet.MARS;

            case "Pisces" ->
                    Planet.JUPITER;

            default ->
                    throw new IllegalArgumentException(
                            "Unknown zodiac sign: " + sign
                    );
        };
    }

    public List<HouseLordPosition> calculateHouseLordPositions(
            List<HousePosition> houses,
            List<PlanetHousePosition> planetHousePositions
    ) {

        List<HouseLordPosition> result =
                new ArrayList<>();

        for (HousePosition house : houses) {

            Planet lord =
                    calculateHouseLord(
                            house.sign()
                    );

            int lordHouse = 0;

            for (PlanetHousePosition planetPosition
                    : planetHousePositions) {

                if (planetPosition.planet() == lord) {

                    lordHouse =
                            planetPosition.house();

                    break;
                }
            }

            result.add(
                    new HouseLordPosition(
                            house.house(),
                            house.sign(),
                            lord,
                            lordHouse
                    )
            );
        }

        return result;
    }
}