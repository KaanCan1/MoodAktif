package com.kaancankurt.moodaktif_1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecommendationEngine {

    public List<Recommendation> generateRecommendations(Map<String, String> userInputs) {
        List<Recommendation> recommendations = new ArrayList<>();

        String mood = userInputs.get("mood");
        String age = userInputs.get("age");
        String medication = userInputs.get("medication");
        String health = userInputs.get("health");
        String living = userInputs.get("living");
        String socialInteraction = userInputs.get("social_interaction");
        String familyVisits = userInputs.get("family_visits");
        String freeTime = userInputs.get("free_time");

        // Python kodundaki öneri mantığını Android'e uyarlıyoruz

        // Mood-based complex recommendations
        if (isMoodLow(mood) && "yalniz".equals(living) && "1_saatten_az".equals(freeTime)) {
            recommendations.add(new Recommendation(
                    "Kısa süreli meditasyon veya nefes egzersizleri yapın",
                    "Yalnız yaşayan ve az boş zamanı olan kişiler için hızlı stres azaltma teknikleri"
            ));
        } else if (isMoodLow(mood) && isLivingWithOthers(living) && isHighFreeTime(freeTime)) {
            recommendations.add(new Recommendation(
                    "Sosyal aktivitelere katılın ve yeni hobiler edinin",
                    "Sosyal desteğiniz var ve zamanınız müsait, bu fırsatı değerlendirin"
            ));
        } else if ("stresli".equals(mood) && "25_ve_uzeri".equals(age) && "1_saatten_az".equals(freeTime)) {
            recommendations.add(new Recommendation(
                    "İş-yaşam dengesini gözden geçirin ve önceliklerinizi belirleyin",
                    "Yetişkin yaşta stres ve zaman sıkıntısı yaşayanlar için özel öneri"
            ));
        } else if ("mutsuz".equals(mood) && "aileyle".equals(living) && "ice_donuk".equals(socialInteraction)) {
            recommendations.add(new Recommendation(
                    "Ailenizle kaliteli zaman geçirmeye odaklanın",
                    "İçe dönük kişiliğiniz için aile ortamında rahat edebileceğiniz aktiviteler"
            ));
        } else if ("endiseli".equals(mood) && "evet".equals(health) && "evet".equals(medication)) {
            recommendations.add(new Recommendation(
                    "Doktorunuzla düzenli iletişim halinde olun ve tedaviye uyum sağlayın",
                    "Sağlık sorunu ve ilaç kullanımı olan endişeli kişiler için tıbbi destek"
            ));
        }

        // Age-based recommendations
        if ("18-25".equals(age) && "ice_donuk".equals(socialInteraction)) {
            recommendations.add(new Recommendation(
                    "Sosyal becerilerinizi geliştirmek için küçük adımlar atın",
                    "Genç yaşta sosyal becerileri geliştirmek gelecekte fayda sağlar"
            ));
        } else if ("25_ve_uzeri".equals(age) && "yalniz".equals(living)) {
            recommendations.add(new Recommendation(
                    "Sosyal çevre kurmaya odaklanın ve toplumsal aktivitelere katılın",
                    "Yetişkin yaşta yalnız yaşamanın zorluklarını sosyal bağlarla dengeleyebilirsiniz"
            ));
        }

        // Health-based recommendations
        if ("evet".equals(health) && "yalniz".equals(living)) {
            recommendations.add(new Recommendation(
                    "Sağlık durumunuzu takip edecek bir destek sistemi kurun",
                    "Sağlık sorunu olan ve yalnız yaşayan kişiler için güvenlik ağı"
            ));
        } else if ("hayir".equals(health) && isLivingWithOthers(living)) {
            recommendations.add(new Recommendation(
                    "Sağlıklı yaşam tarzınızı sürdürün ve çevrenizle paylaşın",
                    "Sağlıklı olmanın avantajını sosyal çevrenizle birlikte değerlendirin"
            ));
        }

        // Social interaction recommendations
        if ("ice_donuk".equals(socialInteraction) && "hic".equals(familyVisits)) {
            recommendations.add(new Recommendation(
                    "Ailenizle iletişimi yeniden kurmak için küçük adımlar atın",
                    "İçe dönük kişilik ve aile ile iletişimsizlik birleştiğinde izolasyon riski artar"
            ));
        } else if ("disa_donuk".equals(socialInteraction) && isHighFreeTime(freeTime)) {
            recommendations.add(new Recommendation(
                    "Sosyal enerjinizi kullanarak yeni gruplar ve aktiviteler keşfedin",
                    "Dışa dönük kişiliğiniz ve boş zamanınız yeni fırsatlar için ideal"
            ));
        }

        // Free time recommendations
        if ("1_saatten_az".equals(freeTime)) {
            recommendations.add(new Recommendation(
                    "Günlük rutininizi gözden geçirin ve kısa molalar alın",
                    "Az boş zamanı olan kişiler için etkili dinlenme teknikleri"
            ));
        } else if (isHighFreeTime(freeTime)) {
            recommendations.add(new Recommendation(
                    "Zamanınızı verimli kullanmak için yeni beceriler öğrenin",
                    "Bol boş zamanı olan kişiler için kişisel gelişim önerileri"
            ));
        }

        // General mood recommendations if no specific match
        if (recommendations.isEmpty()) {
            if ("mutlu".equals(mood)) {
                recommendations.add(new Recommendation(
                        "Mutluluğunuzu sürdürmek için düzenli egzersiz ve sosyal aktiviteler yapın",
                        "Pozitif ruh halinizi korumak için öneriler"
                ));
            } else if (isMoodLow(mood)) {
                recommendations.add(new Recommendation(
                        "Profesyonel destek almayı düşünün ve kendinize zaman ayırın",
                        "Düşük ruh hali için genel destek önerileri"
                ));
            } else {
                recommendations.add(new Recommendation(
                        "Kendinizi tanımaya devam edin ve sağlıklı yaşam alışkanlıkları edinin",
                        "Genel sağlık ve mutluluk için öneriler"
                ));
            }
        }

        return recommendations;
    }

    private boolean isMoodLow(String mood) {
        return "mutsuz".equals(mood) || "endiseli".equals(mood) || "stresli".equals(mood);
    }

    private boolean isLivingWithOthers(String living) {
        return "arkadaslarla".equals(living) || "aileyle".equals(living);
    }

    private boolean isHighFreeTime(String freeTime) {
        return "2-4_saat".equals(freeTime) || "1-2_saat".equals(freeTime);
    }
}