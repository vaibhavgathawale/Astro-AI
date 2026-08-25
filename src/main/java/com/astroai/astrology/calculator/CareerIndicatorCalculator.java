package com.astroai.astrology.calculator;

import com.astroai.astrology.model.CareerIndicator;
import com.astroai.astrology.model.HouseLordPosition;
import com.astroai.astrology.model.Planet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CareerIndicatorCalculator {

    public List<CareerIndicator> calculateCareerIndicators(
            List<HouseLordPosition> houseLordPositions
    ) {

        List<CareerIndicator> result =
                new ArrayList<>();

        // =====================================================
        // 1. 2nd House - Wealth / Family Income
        // =====================================================

        addIndicator(
                result,
                houseLordPositions,
                2
        );


        // =====================================================
        // 2. 6th House - Job / Service
        // =====================================================

        addIndicator(
                result,
                houseLordPositions,
                6
        );


        // =====================================================
        // 3. 10th House - Career / Profession
        // =====================================================

        addIndicator(
                result,
                houseLordPositions,
                10
        );


        // =====================================================
        // 4. 11th House - Income / Gains
        // =====================================================

        addIndicator(
                result,
                houseLordPositions,
                11
        );

        return result;
    }


    // =========================================================
    // ADD HOUSE CAREER INDICATOR
    // =========================================================

    private void addIndicator(
            List<CareerIndicator> result,
            List<HouseLordPosition> houseLordPositions,
            int houseNumber
    ) {

        HouseLordPosition position =
                houseLordPositions.stream()
                        .filter(
                                h -> h.house() == houseNumber
                        )
                        .findFirst()
                        .orElse(null);

        if (position == null) {
            return;
        }


        Planet lord =
                position.lord();

        int lordHouse =
                position.lordHouse();


        // =====================================================
        // Calculate score
        // =====================================================

        int score =
                calculateScore(
                        houseNumber,
                        lord,
                        lordHouse
                );


        // =====================================================
        // Career field
        // =====================================================

        String field =
                determineField(
                        houseNumber,
                        lord
                );


        // =====================================================
        // Reason
        // =====================================================

        String reason =
                buildReason(
                        houseNumber,
                        lord,
                        lordHouse
                );


        result.add(
                new CareerIndicator(
                        houseNumber,
                        lord,
                        lordHouse,
                        field,
                        score,
                        reason
                )
        );
    }


    // =========================================================
    // SCORE
    // =========================================================

    private int calculateScore(
            int house,
            Planet lord,
            int lordHouse
    ) {

        int score = 50;


        // -----------------------------------------------------
        // Important career houses
        // -----------------------------------------------------

        if (house == 10) {
            score += 20;
        }

        if (house == 6) {
            score += 15;
        }

        if (house == 11) {
            score += 15;
        }

        if (house == 2) {
            score += 10;
        }


        // -----------------------------------------------------
        // Lord placement
        // -----------------------------------------------------

        if (lordHouse == 10) {
            score += 15;
        }

        if (lordHouse == 11) {
            score += 15;
        }

        if (lordHouse == 6) {
            score += 10;
        }

        if (lordHouse == 2) {
            score += 10;
        }


        // -----------------------------------------------------
        // Cap score
        // -----------------------------------------------------

        return Math.min(score, 100);
    }


    // =========================================================
    // CAREER FIELD
    // =========================================================

    private String determineField(
            int house,
            Planet lord
    ) {

        // -----------------------------------------------------
        // 10th House
        // -----------------------------------------------------

        if (house == 10) {

            return switch (lord) {

                case MERCURY ->
                        "Technology / IT / Communication";

                case SATURN ->
                        "Engineering / Technology / Administration";

                case MARS ->
                        "Engineering / Technical";

                case JUPITER ->
                        "Finance / Advisory / Management";

                case SUN ->
                        "Government / Leadership";

                case VENUS ->
                        "Business / Finance / Creative";

                case MOON ->
                        "Public Service / Healthcare / Hospitality";

                case RAHU ->
                        "Technology / Foreign / MNC";

                case KETU ->
                        "Research / Analysis";
            };
        }


        // -----------------------------------------------------
        // 6th House
        // -----------------------------------------------------

        if (house == 6) {

            return switch (lord) {

                case MERCURY ->
                        "IT / Service / Analytics";

                case SATURN ->
                        "Engineering / Operations / Administration";

                case MARS ->
                        "Technical / Engineering";

                case JUPITER ->
                        "Advisory / Education / Finance";

                case SUN ->
                        "Government / Administration";

                case VENUS ->
                        "Finance / HR / Service";

                case MOON ->
                        "Healthcare / Public Service";

                case RAHU ->
                        "Technology / MNC / Foreign Service";

                case KETU ->
                        "Research / Analysis";
            };
        }


        // -----------------------------------------------------
        // 11th House
        // -----------------------------------------------------

        if (house == 11) {

            return switch (lord) {

                case MERCURY ->
                        "Technology / Business / Networking";

                case SATURN ->
                        "Large Organization / Engineering";

                case MARS ->
                        "Technical Business / Engineering";

                case JUPITER ->
                        "Finance / Management / Advisory";

                case SUN ->
                        "Leadership / Government";

                case VENUS ->
                        "Business / Finance / Creative";

                case MOON ->
                        "Public / Customer-oriented Business";

                case RAHU ->
                        "Foreign / MNC / Technology";

                case KETU ->
                        "Research / Specialized Field";
            };
        }


        // -----------------------------------------------------
        // 2nd House
        // -----------------------------------------------------

        if (house == 2) {

            return switch (lord) {

                case MERCURY ->
                        "IT / Communication / Finance";

                case SATURN ->
                        "Engineering / Administration";

                case MARS ->
                        "Technical / Engineering";

                case JUPITER ->
                        "Finance / Education / Advisory";

                case SUN ->
                        "Leadership / Government";

                case VENUS ->
                        "Finance / Business / Creative";

                case MOON ->
                        "Public / Customer-oriented Work";

                case RAHU ->
                        "Foreign / Technology / MNC";

                case KETU ->
                        "Research / Analysis";
            };
        }


        return "General Career";
    }


    // =========================================================
    // REASON
    // =========================================================

    private String buildReason(
            int house,
            Planet lord,
            int lordHouse
    ) {

        return house
                + "th house lord is "
                + lord
                + " and is placed in house "
                + lordHouse;
    }
}