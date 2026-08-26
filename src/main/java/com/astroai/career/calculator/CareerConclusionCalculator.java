package com.astroai.career.calculator;

import com.astroai.astrology.model.*;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CareerConclusionCalculator {

    public CareerConclusion calculate(
            CareerScore careerScore,
            List<JobTimingPrediction> bestPeriods,
            JobTimingPrediction currentDasha
    ) {

        // =====================================================
        // 1. Overall Score
        // =====================================================

        int overallScore =
                careerScore.overallScore();


        // =====================================================
        // 2. Job Opportunity
        // =====================================================

        String jobOpportunity;

        if (careerScore.jobScore() >= 80) {

            jobOpportunity =
                    "Very strong job opportunity";

        } else if (careerScore.jobScore() >= 65) {

            jobOpportunity =
                    "Good job opportunity";

        } else if (careerScore.jobScore() >= 50) {

            jobOpportunity =
                    "Moderate job opportunity";

        } else {

            jobOpportunity =
                    "Job opportunities may require patience";
        }


        // =====================================================
        // 3. Career Growth
        // =====================================================

        String careerGrowth;

        if (careerScore.growthScore() >= 80) {

            careerGrowth =
                    "Very strong career growth potential";

        } else if (careerScore.growthScore() >= 65) {

            careerGrowth =
                    "Good career growth potential";

        } else if (careerScore.growthScore() >= 50) {

            careerGrowth =
                    "Moderate career growth";

        } else {

            careerGrowth =
                    "Career growth may be gradual";
        }


        // =====================================================
        // 4. Income Outlook
        // =====================================================

        String incomeOutlook;

        if (careerScore.incomeScore() >= 80) {

            incomeOutlook =
                    "Very strong income potential";

        } else if (careerScore.incomeScore() >= 65) {

            incomeOutlook =
                    "Good income potential";

        } else if (careerScore.incomeScore() >= 50) {

            incomeOutlook =
                    "Moderate income potential";

        } else {

            incomeOutlook =
                    "Income growth may be gradual";
        }


        // =====================================================
        // 5. Best Period
        // =====================================================

        String bestPeriod;

        if (bestPeriods == null || bestPeriods.isEmpty()) {

            bestPeriod =
                    "No strong period identified";

        } else {

            JobTimingPrediction best =
                    bestPeriods.get(0);

            bestPeriod =
                    formatPeriod(best);
        }


        // =====================================================
        // 6. Current Period
        // =====================================================

        String currentPeriod;

        if (currentDasha == null) {

            currentPeriod =
                    "Current dasha unavailable";

        } else {

            currentPeriod =
                    currentDasha.mahadashaLord()
                            + " - "
                            + currentDasha.antardashaLord()
                            + " (Score "
                            + currentDasha.score()
                            + ")";
        }


        // =====================================================
        // 7. Key Reasons
        // =====================================================

        List<String> keyReasons =
                new ArrayList<>();

        if (careerScore.jobScore() >= 65) {

            keyReasons.add(
                    "Strong employment potential"
            );
        }

        if (careerScore.professionScore() >= 65) {

            keyReasons.add(
                    "Strong professional career indicators"
            );
        }

        if (careerScore.incomeScore() >= 65) {

            keyReasons.add(
                    "Positive income and financial potential"
            );
        }

        if (careerScore.growthScore() >= 65) {

            keyReasons.add(
                    "Positive long-term career growth"
            );
        }

        if (bestPeriods != null
                && !bestPeriods.isEmpty()) {

            keyReasons.add(
                    "Favorable dasha periods identified"
            );
        }


        // =====================================================
        // 8. Overall Conclusion
        // =====================================================

        String overallConclusion;

        if (overallScore >= 80) {

            overallConclusion =
                    "Overall career outlook is very strong with good opportunities for employment, growth and financial improvement.";

        } else if (overallScore >= 65) {

            overallConclusion =
                    "Overall career outlook is positive with good employment potential and favorable periods for career progress.";

        } else if (overallScore >= 50) {

            overallConclusion =
                    "Overall career outlook is moderate. Opportunities are available, but progress may require consistent effort and patience.";

        } else {

            overallConclusion =
                    "Career progress may be slower than expected and requires patience, planning and consistent effort.";
        }


        // =====================================================
        // 9. Final Career Conclusion
        // =====================================================

        return new CareerConclusion(
                overallConclusion,
                overallScore,
                jobOpportunity,
                careerGrowth,
                incomeOutlook,
                bestPeriod,
                currentPeriod,
                keyReasons
        );
    }


    // =========================================================
    // Format Best Period
    // =========================================================

    private String formatPeriod(
            JobTimingPrediction period
    ) {

        return period.mahadashaLord()
                + " - "
                + period.antardashaLord()
                + " : "
                + formatYear(period.startYear())
                + " to "
                + formatYear(period.endYear())
                + " (Score "
                + period.score()
                + ")";
    }


    // =========================================================
    // Format Year
    // =========================================================

    private String formatYear(
            double year
    ) {

        return String.format(
                "%.2f",
                year
        );
    }
}