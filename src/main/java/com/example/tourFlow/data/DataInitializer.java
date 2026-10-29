package com.example.tourFlow.data;

import com.example.tourFlow.model.Destination;
import com.example.tourFlow.model.LocalAlert;
import com.example.tourFlow.model.LocalBusiness;
import com.example.tourFlow.repository.DestinationRepository;
import com.example.tourFlow.repository.LocalAlertRepository;
import com.example.tourFlow.repository.LocalBusinessRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final DestinationRepository destinationRepo;
    private final LocalAlertRepository alertRepo;
    private final LocalBusinessRepository businessRepo;

    public DataInitializer(DestinationRepository destinationRepo,
                           LocalAlertRepository alertRepo,
                           LocalBusinessRepository businessRepo) {
        this.destinationRepo = destinationRepo;
        this.alertRepo = alertRepo;
        this.businessRepo = businessRepo;
    }

    @Override
    public void run(String... args) {
        seedDestinations();
        seedAlerts();
        seedBusinesses();
        System.out.println("✅ TourFlow: Database seeded with " + destinationRepo.count() + " destinations, "
                + alertRepo.count() + " alerts, " + businessRepo.count() + " local businesses.");
    }

    private void seedDestinations() {
        // === POPULAR (HIGH CROWD) DESTINATIONS ===

        Destination manali = new Destination("Manali", "Himachal Pradesh", "hill_station", 32.2396, 77.1887);
        manali.setDistrict("Kullu");
        manali.setAltitude(2050);
        manali.setMaxCapacity(15000);
        manali.setDescription("A popular hill station in the mountains of Himachal Pradesh, known for its stunning landscapes, adventure sports, and proximity to the Rohtang Pass. A major tourist hub with snow-capped peaks and lush green valleys.");
        manali.setImageUrl("/images/manali.jpg");
        manali.setDiscoveryScore(15);
        manali.setHiddenGem(false);
        manali.setAdventureFriendly(true);
        manali.setNatureFriendly(true);
        manali.setFamilyFriendly(true);
        manali.setPeacefulFriendly(false);
        manali.setBudgetFriendly(false);
        manali.setPhotographyFriendly(true);
        manali.setSnowFriendly(true);
        manali.setTrekkingFriendly(true);
        manali.setCurrentCrowdPercentage(92);
        manali.setCurrentTrafficPercentage(88);
        manali.setCurrentParkingPercentage(64);
        manali.setHotelAvailabilityPercentage(18);
        manali.setTourismPressure(91);
        manali.setEnvironmentalLoad(88);
        manali.setWeatherCondition("Partly Cloudy");
        manali.setTemperature(18.5);
        manali.setHumidity(72);
        manali.setNearbyAttractions("Rohtang Pass, Solang Valley, Hadimba Temple, Old Manali, Jogini Falls");
        manali.setLocalFood("Siddu, Dham, Thukpa, Momos");
        manali.setTransportOptions("Bus from Delhi (12hr), Taxi, Local Bus");
        destinationRepo.save(manali);

        Destination shimla = new Destination("Shimla", "Himachal Pradesh", "hill_station", 31.1048, 77.1734);
        shimla.setDistrict("Shimla");
        shimla.setAltitude(2276);
        shimla.setMaxCapacity(20000);
        shimla.setDescription("The capital city of Himachal Pradesh, known as the 'Queen of Hills'. Famous for its colonial architecture, Mall Road, and panoramic mountain views. A year-round tourist destination with rich heritage.");
        shimla.setImageUrl("/images/shimla.jpg");
        shimla.setDiscoveryScore(10);
        shimla.setHiddenGem(false);
        shimla.setAdventureFriendly(false);
        shimla.setNatureFriendly(true);
        shimla.setFamilyFriendly(true);
        shimla.setPeacefulFriendly(false);
        shimla.setBudgetFriendly(false);
        shimla.setPhotographyFriendly(true);
        shimla.setSnowFriendly(true);
        shimla.setTrekkingFriendly(false);
        shimla.setCurrentCrowdPercentage(87);
        shimla.setCurrentTrafficPercentage(85);
        shimla.setCurrentParkingPercentage(55);
        shimla.setHotelAvailabilityPercentage(22);
        shimla.setTourismPressure(87);
        shimla.setEnvironmentalLoad(82);
        shimla.setWeatherCondition("Clear");
        shimla.setTemperature(21.0);
        shimla.setHumidity(65);
        shimla.setNearbyAttractions("Mall Road, Ridge, Jakhu Temple, Christ Church, Kufri");
        shimla.setLocalFood("Chana Madra, Babru, Aktori, Siddu");
        shimla.setTransportOptions("Train (Kalka-Shimla), Bus from Delhi (8hr), Taxi");
        destinationRepo.save(shimla);

        Destination dharamshala = new Destination("Dharamshala", "Himachal Pradesh", "hill_station", 32.2190, 76.3234);
        dharamshala.setDistrict("Kangra");
        dharamshala.setAltitude(1457);
        dharamshala.setMaxCapacity(12000);
        dharamshala.setDescription("Home to the Dalai Lama and the Tibetan government-in-exile. Known for its Tibetan culture, cricket stadium surrounded by mountains, and spiritual atmosphere. Upper Dharamshala (McLeodGanj) is a backpacker favorite.");
        dharamshala.setImageUrl("/images/dharamshala.jpg");
        dharamshala.setDiscoveryScore(25);
        dharamshala.setHiddenGem(false);
        dharamshala.setAdventureFriendly(true);
        dharamshala.setNatureFriendly(true);
        dharamshala.setFamilyFriendly(true);
        dharamshala.setPeacefulFriendly(true);
        dharamshala.setBudgetFriendly(true);
        dharamshala.setPhotographyFriendly(true);
        dharamshala.setSnowFriendly(false);
        dharamshala.setTrekkingFriendly(true);
        dharamshala.setCurrentCrowdPercentage(72);
        dharamshala.setCurrentTrafficPercentage(65);
        dharamshala.setCurrentParkingPercentage(50);
        dharamshala.setHotelAvailabilityPercentage(35);
        dharamshala.setTourismPressure(68);
        dharamshala.setEnvironmentalLoad(55);
        dharamshala.setWeatherCondition("Cloudy");
        dharamshala.setTemperature(22.0);
        dharamshala.setHumidity(78);
        dharamshala.setNearbyAttractions("McLeodGanj, Triund Trek, Bhagsu Waterfall, Namgyal Monastery, Dal Lake");
        dharamshala.setLocalFood("Tibetan Thukpa, Momos, Thenthuk, Tingmo");
        dharamshala.setTransportOptions("Bus from Delhi (10hr), Gaggal Airport, Taxi");
        destinationRepo.save(dharamshala);

        Destination kasol = new Destination("Kasol", "Himachal Pradesh", "village", 32.0100, 77.3147);
        kasol.setDistrict("Kullu");
        kasol.setAltitude(1640);
        kasol.setMaxCapacity(5000);
        kasol.setDescription("A small village in the Parvati Valley known for its hippie culture, Israeli influence, and stunning Parvati River views. Gateway to Kheerganga trek and Malana village.");
        kasol.setImageUrl("/images/kasol.jpg");
        kasol.setDiscoveryScore(30);
        kasol.setHiddenGem(false);
        kasol.setAdventureFriendly(true);
        kasol.setNatureFriendly(true);
        kasol.setFamilyFriendly(false);
        kasol.setPeacefulFriendly(true);
        kasol.setBudgetFriendly(true);
        kasol.setPhotographyFriendly(true);
        kasol.setSnowFriendly(false);
        kasol.setTrekkingFriendly(true);
        kasol.setCurrentCrowdPercentage(78);
        kasol.setCurrentTrafficPercentage(70);
        kasol.setCurrentParkingPercentage(40);
        kasol.setHotelAvailabilityPercentage(30);
        kasol.setTourismPressure(75);
        kasol.setEnvironmentalLoad(70);
        kasol.setWeatherCondition("Rainy");
        kasol.setTemperature(19.0);
        kasol.setHumidity(85);
        kasol.setNearbyAttractions("Kheerganga Trek, Malana, Tosh, Parvati River, Manikaran Sahib");
        kasol.setLocalFood("Israeli Food, Pancakes, River Trout, Momos");
        kasol.setTransportOptions("Bus from Bhuntar (1.5hr), Taxi from Manali");
        destinationRepo.save(kasol);

        Destination mussoorie = new Destination("Mussoorie", "Uttarakhand", "hill_station", 30.4598, 78.0644);
        mussoorie.setDistrict("Dehradun");
        mussoorie.setAltitude(1880);
        mussoorie.setMaxCapacity(18000);
        mussoorie.setDescription("Known as the 'Queen of the Hills', Mussoorie is a popular hill station in Uttarakhand with colonial-era charm, lush green hills, and stunning views of the Doon Valley and Shivalik ranges.");
        mussoorie.setImageUrl("/images/mussoorie.jpg");
        mussoorie.setDiscoveryScore(12);
        mussoorie.setHiddenGem(false);
        mussoorie.setAdventureFriendly(false);
        mussoorie.setNatureFriendly(true);
        mussoorie.setFamilyFriendly(true);
        mussoorie.setPeacefulFriendly(false);
        mussoorie.setBudgetFriendly(false);
        mussoorie.setPhotographyFriendly(true);
        mussoorie.setSnowFriendly(true);
        mussoorie.setTrekkingFriendly(false);
        mussoorie.setCurrentCrowdPercentage(65);
        mussoorie.setCurrentTrafficPercentage(60);
        mussoorie.setCurrentParkingPercentage(45);
        mussoorie.setHotelAvailabilityPercentage(40);
        mussoorie.setTourismPressure(65);
        mussoorie.setEnvironmentalLoad(60);
        mussoorie.setWeatherCondition("Pleasant");
        mussoorie.setTemperature(20.0);
        mussoorie.setHumidity(70);
        mussoorie.setNearbyAttractions("Kempty Falls, Gun Hill, Mall Road, Lal Tibba, Cloud's End");
        mussoorie.setLocalFood("Maggi, Chai, Pahadi cuisine, Local sweets");
        mussoorie.setTransportOptions("Bus from Delhi (6hr), Train to Dehradun, Taxi");
        destinationRepo.save(mussoorie);

        // === HIDDEN GEMS (LOW CROWD) DESTINATIONS ===

        Destination jibhi = new Destination("Jibhi", "Himachal Pradesh", "village", 31.6210, 77.3437);
        jibhi.setDistrict("Kullu");
        jibhi.setAltitude(1800);
        jibhi.setMaxCapacity(3000);
        jibhi.setDescription("A serene hamlet in the Tirthan Valley, known for its pristine beauty, wooden houses, ancient temples, and untouched forests. Perfect for those seeking an offbeat mountain experience away from crowds.");
        jibhi.setImageUrl("/images/jibhi.jpg");
        jibhi.setDiscoveryScore(91);
        jibhi.setHiddenGem(true);
        jibhi.setAdventureFriendly(true);
        jibhi.setNatureFriendly(true);
        jibhi.setFamilyFriendly(true);
        jibhi.setPeacefulFriendly(true);
        jibhi.setBudgetFriendly(true);
        jibhi.setPhotographyFriendly(true);
        jibhi.setSnowFriendly(false);
        jibhi.setTrekkingFriendly(true);
        jibhi.setCurrentCrowdPercentage(24);
        jibhi.setCurrentTrafficPercentage(18);
        jibhi.setCurrentParkingPercentage(35);
        jibhi.setHotelAvailabilityPercentage(78);
        jibhi.setTourismPressure(25);
        jibhi.setEnvironmentalLoad(22);
        jibhi.setWeatherCondition("Clear");
        jibhi.setTemperature(17.0);
        jibhi.setHumidity(60);
        jibhi.setNearbyAttractions("Jibhi Waterfall, Serolsar Lake, Jalori Pass, Raghupur Fort, Chehni Kothi");
        jibhi.setLocalFood("Trout Fish, Siddu, Dham, Local Honey");
        jibhi.setTransportOptions("Bus from Aut (2hr), Taxi from Manali (3hr)");
        destinationRepo.save(jibhi);

        Destination tirthanValley = new Destination("Tirthan Valley", "Himachal Pradesh", "valley", 31.6380, 77.4520);
        tirthanValley.setDistrict("Kullu");
        tirthanValley.setAltitude(1600);
        tirthanValley.setMaxCapacity(2500);
        tirthanValley.setDescription("A stunning valley named after the Tirthan River, part of the Great Himalayan National Park (UNESCO World Heritage Site). Known for trout fishing, nature walks, and complete tranquility.");
        tirthanValley.setImageUrl("/images/tirthan.jpg");
        tirthanValley.setDiscoveryScore(89);
        tirthanValley.setHiddenGem(true);
        tirthanValley.setAdventureFriendly(true);
        tirthanValley.setNatureFriendly(true);
        tirthanValley.setFamilyFriendly(true);
        tirthanValley.setPeacefulFriendly(true);
        tirthanValley.setBudgetFriendly(true);
        tirthanValley.setPhotographyFriendly(true);
        tirthanValley.setSnowFriendly(false);
        tirthanValley.setTrekkingFriendly(true);
        tirthanValley.setCurrentCrowdPercentage(21);
        tirthanValley.setCurrentTrafficPercentage(15);
        tirthanValley.setCurrentParkingPercentage(30);
        tirthanValley.setHotelAvailabilityPercentage(82);
        tirthanValley.setTourismPressure(22);
        tirthanValley.setEnvironmentalLoad(18);
        tirthanValley.setWeatherCondition("Clear");
        tirthanValley.setTemperature(19.0);
        tirthanValley.setHumidity(55);
        tirthanValley.setNearbyAttractions("Great Himalayan National Park, Tirthan River, Trout Fishing, Gushaini, Rolla");
        tirthanValley.setLocalFood("River Trout, Siddu, Pahadi Rajma, Wild Honey");
        tirthanValley.setTransportOptions("Bus from Aut (1.5hr), Taxi from Kullu");
        destinationRepo.save(tirthanValley);

        Destination shoja = new Destination("Shoja", "Himachal Pradesh", "village", 31.5655, 77.3690);
        shoja.setDistrict("Kullu");
        shoja.setAltitude(2692);
        shoja.setMaxCapacity(2000);
        shoja.setDescription("A tiny village near Jalori Pass, offering breathtaking views of the snow-covered Himalayan ranges. Known for its dense forests of oak and cedar, and the medieval Chaini Kothi (tower).");
        shoja.setImageUrl("/images/shoja.jpg");
        shoja.setDiscoveryScore(93);
        shoja.setHiddenGem(true);
        shoja.setAdventureFriendly(true);
        shoja.setNatureFriendly(true);
        shoja.setFamilyFriendly(true);
        shoja.setPeacefulFriendly(true);
        shoja.setBudgetFriendly(true);
        shoja.setPhotographyFriendly(true);
        shoja.setSnowFriendly(true);
        shoja.setTrekkingFriendly(true);
        shoja.setCurrentCrowdPercentage(18);
        shoja.setCurrentTrafficPercentage(12);
        shoja.setCurrentParkingPercentage(25);
        shoja.setHotelAvailabilityPercentage(85);
        shoja.setTourismPressure(19);
        shoja.setEnvironmentalLoad(15);
        shoja.setWeatherCondition("Clear");
        shoja.setTemperature(14.0);
        shoja.setHumidity(50);
        shoja.setNearbyAttractions("Jalori Pass, Serolsar Lake, Waterfall Point, Chaini Kothi");
        shoja.setLocalFood("Siddu, Dham, Trout, Local Honey");
        shoja.setTransportOptions("Taxi from Aut (2.5hr), Limited bus service");
        destinationRepo.save(shoja);

        Destination birBilling = new Destination("Bir Billing", "Himachal Pradesh", "village", 31.8767, 76.7174);
        birBilling.setDistrict("Kangra");
        birBilling.setAltitude(1525);
        birBilling.setMaxCapacity(4000);
        birBilling.setDescription("Asia's best paragliding site. A charming Tibetan colony known for monasteries, tea gardens, and the world-famous paragliding take-off point at Billing. Perfect blend of adventure and spirituality.");
        birBilling.setImageUrl("/images/bir.jpg");
        birBilling.setDiscoveryScore(72);
        birBilling.setHiddenGem(true);
        birBilling.setAdventureFriendly(true);
        birBilling.setNatureFriendly(true);
        birBilling.setFamilyFriendly(false);
        birBilling.setPeacefulFriendly(true);
        birBilling.setBudgetFriendly(true);
        birBilling.setPhotographyFriendly(true);
        birBilling.setSnowFriendly(false);
        birBilling.setTrekkingFriendly(true);
        birBilling.setCurrentCrowdPercentage(35);
        birBilling.setCurrentTrafficPercentage(25);
        birBilling.setCurrentParkingPercentage(40);
        birBilling.setHotelAvailabilityPercentage(70);
        birBilling.setTourismPressure(32);
        birBilling.setEnvironmentalLoad(28);
        birBilling.setWeatherCondition("Clear");
        birBilling.setTemperature(23.0);
        birBilling.setHumidity(55);
        birBilling.setNearbyAttractions("Paragliding, Chokling Monastery, Deer Park Institute, Tea Gardens, Baijnath Temple");
        birBilling.setLocalFood("Tibetan Food, Organic Cafe, Local Tea");
        birBilling.setTransportOptions("Bus from Dharamshala (3hr), Taxi, Pathankot Rail");
        destinationRepo.save(birBilling);

        Destination barot = new Destination("Barot", "Himachal Pradesh", "village", 31.8420, 76.9250);
        barot.setDistrict("Mandi");
        barot.setAltitude(1820);
        barot.setMaxCapacity(2000);
        barot.setDescription("A hidden paradise along the Uhl River, known for excellent trout fishing, the Barot dam, and untouched natural beauty. One of Himachal's best-kept secrets with minimal commercialization.");
        barot.setImageUrl("/images/barot.jpg");
        barot.setDiscoveryScore(88);
        barot.setHiddenGem(true);
        barot.setAdventureFriendly(true);
        barot.setNatureFriendly(true);
        barot.setFamilyFriendly(true);
        barot.setPeacefulFriendly(true);
        barot.setBudgetFriendly(true);
        barot.setPhotographyFriendly(true);
        barot.setSnowFriendly(false);
        barot.setTrekkingFriendly(true);
        barot.setCurrentCrowdPercentage(15);
        barot.setCurrentTrafficPercentage(10);
        barot.setCurrentParkingPercentage(20);
        barot.setHotelAvailabilityPercentage(90);
        barot.setTourismPressure(15);
        barot.setEnvironmentalLoad(12);
        barot.setWeatherCondition("Clear");
        barot.setTemperature(18.0);
        barot.setHumidity(58);
        barot.setNearbyAttractions("Uhl River, Barot Dam, Nargu Wildlife Sanctuary, Chhota Bhangal Trek");
        barot.setLocalFood("Trout Fish, Siddu, Local Dham");
        barot.setTransportOptions("Bus from Joginder Nagar (1.5hr), Taxi from Mandi");
        destinationRepo.save(barot);

        Destination chitkul = new Destination("Chitkul", "Himachal Pradesh", "village", 31.3519, 78.4361);
        chitkul.setDistrict("Kinnaur");
        chitkul.setAltitude(3450);
        chitkul.setMaxCapacity(1500);
        chitkul.setDescription("The last inhabited village near the Indo-Tibetan border. Known for its stark beauty, apple orchards, ancient wooden temple, and dramatic views of the Baspa River gorge. Minimal tourist infrastructure preserves its authenticity.");
        chitkul.setImageUrl("/images/chitkul.jpg");
        chitkul.setDiscoveryScore(95);
        chitkul.setHiddenGem(true);
        chitkul.setAdventureFriendly(true);
        chitkul.setNatureFriendly(true);
        chitkul.setFamilyFriendly(false);
        chitkul.setPeacefulFriendly(true);
        chitkul.setBudgetFriendly(true);
        chitkul.setPhotographyFriendly(true);
        chitkul.setSnowFriendly(true);
        chitkul.setTrekkingFriendly(true);
        chitkul.setCurrentCrowdPercentage(12);
        chitkul.setCurrentTrafficPercentage(8);
        chitkul.setCurrentParkingPercentage(15);
        chitkul.setHotelAvailabilityPercentage(88);
        chitkul.setTourismPressure(12);
        chitkul.setEnvironmentalLoad(10);
        chitkul.setWeatherCondition("Clear");
        chitkul.setTemperature(12.0);
        chitkul.setHumidity(40);
        chitkul.setNearbyAttractions("Baspa River, Chitkul Temple, Nagasti Village, Apple Orchards");
        chitkul.setLocalFood("Rajma Rice, Chhang (local drink), Dry Fruit");
        chitkul.setTransportOptions("Taxi from Sangla (2hr), Bus from Reckong Peo");
        destinationRepo.save(chitkul);

        Destination spiti = new Destination("Spiti Valley", "Himachal Pradesh", "valley", 32.2459, 78.0347);
        spiti.setDistrict("Lahaul and Spiti");
        spiti.setAltitude(3800);
        spiti.setMaxCapacity(3000);
        spiti.setDescription("A cold desert mountain valley known as 'Little Tibet'. Home to ancient monasteries, fossil-rich landscapes, and some of India's most dramatic mountain scenery. Key Monastery and Chandratal Lake are iconic landmarks.");
        spiti.setImageUrl("/images/spiti.jpg");
        spiti.setDiscoveryScore(78);
        spiti.setHiddenGem(true);
        spiti.setAdventureFriendly(true);
        spiti.setNatureFriendly(true);
        spiti.setFamilyFriendly(false);
        spiti.setPeacefulFriendly(true);
        spiti.setBudgetFriendly(true);
        spiti.setPhotographyFriendly(true);
        spiti.setSnowFriendly(true);
        spiti.setTrekkingFriendly(true);
        spiti.setCurrentCrowdPercentage(30);
        spiti.setCurrentTrafficPercentage(22);
        spiti.setCurrentParkingPercentage(30);
        spiti.setHotelAvailabilityPercentage(65);
        spiti.setTourismPressure(28);
        spiti.setEnvironmentalLoad(25);
        spiti.setWeatherCondition("Clear");
        spiti.setTemperature(10.0);
        spiti.setHumidity(30);
        spiti.setNearbyAttractions("Key Monastery, Chandratal Lake, Kunzum Pass, Tabo Monastery, Pin Valley");
        spiti.setLocalFood("Thukpa, Momos, Butter Tea, Chhang");
        spiti.setTransportOptions("Bus from Manali (10hr), Taxi, Self-drive");
        destinationRepo.save(spiti);

        Destination dalhousie = new Destination("Dalhousie", "Himachal Pradesh", "hill_station", 32.5373, 75.9928);
        dalhousie.setDistrict("Chamba");
        dalhousie.setAltitude(1970);
        dalhousie.setMaxCapacity(8000);
        dalhousie.setDescription("A charming colonial-era hill station spread across five hills, offering stunning views of the Dhauladhar ranges. Known for its Scottish and Victorian architecture, churches, and pine-clad valleys.");
        dalhousie.setImageUrl("/images/dalhousie.jpg");
        dalhousie.setDiscoveryScore(45);
        dalhousie.setHiddenGem(false);
        dalhousie.setAdventureFriendly(false);
        dalhousie.setNatureFriendly(true);
        dalhousie.setFamilyFriendly(true);
        dalhousie.setPeacefulFriendly(true);
        dalhousie.setBudgetFriendly(true);
        dalhousie.setPhotographyFriendly(true);
        dalhousie.setSnowFriendly(true);
        dalhousie.setTrekkingFriendly(false);
        dalhousie.setCurrentCrowdPercentage(48);
        dalhousie.setCurrentTrafficPercentage(42);
        dalhousie.setCurrentParkingPercentage(50);
        dalhousie.setHotelAvailabilityPercentage(55);
        dalhousie.setTourismPressure(48);
        dalhousie.setEnvironmentalLoad(40);
        dalhousie.setWeatherCondition("Pleasant");
        dalhousie.setTemperature(19.0);
        dalhousie.setHumidity(65);
        dalhousie.setNearbyAttractions("Kalatop Wildlife Sanctuary, Khajjiar (Mini Switzerland), Dainkund Peak, St. Francis Church");
        dalhousie.setLocalFood("Chamba Chukh, Madra, Dham, Local Tea");
        dalhousie.setTransportOptions("Bus from Pathankot (3hr), Taxi from Chandigarh (7hr)");
        destinationRepo.save(dalhousie);

        Destination chamba = new Destination("Chamba", "Himachal Pradesh", "heritage", 32.5534, 76.1258);
        chamba.setDistrict("Chamba");
        chamba.setAltitude(996);
        chamba.setMaxCapacity(4000);
        chamba.setDescription("An ancient town on the banks of the Ravi River, known for its Lakshmi Narayan Temple complex, Chamba Rumal embroidery, and the famous Minjar Fair. A living museum of Pahari art and architecture.");
        chamba.setImageUrl("/images/chamba.jpg");
        chamba.setDiscoveryScore(80);
        chamba.setHiddenGem(true);
        chamba.setAdventureFriendly(false);
        chamba.setNatureFriendly(true);
        chamba.setFamilyFriendly(true);
        chamba.setPeacefulFriendly(true);
        chamba.setBudgetFriendly(true);
        chamba.setPhotographyFriendly(true);
        chamba.setSnowFriendly(false);
        chamba.setTrekkingFriendly(false);
        chamba.setCurrentCrowdPercentage(20);
        chamba.setCurrentTrafficPercentage(15);
        chamba.setCurrentParkingPercentage(25);
        chamba.setHotelAvailabilityPercentage(80);
        chamba.setTourismPressure(20);
        chamba.setEnvironmentalLoad(18);
        chamba.setWeatherCondition("Warm");
        chamba.setTemperature(28.0);
        chamba.setHumidity(60);
        chamba.setNearbyAttractions("Lakshmi Narayan Temple, Chaugan, Bhuri Singh Museum, Chamunda Devi Temple");
        chamba.setLocalFood("Chamba Chukh, Madra, Patande");
        chamba.setTransportOptions("Bus from Dalhousie (2hr), Bus from Pathankot (5hr)");
        destinationRepo.save(chamba);

        Destination kalpa = new Destination("Kalpa", "Himachal Pradesh", "village", 31.5344, 78.2549);
        kalpa.setDistrict("Kinnaur");
        kalpa.setAltitude(2759);
        kalpa.setMaxCapacity(2000);
        kalpa.setDescription("A small town offering mesmerizing views of the Kinnaur Kailash range. Known for its apple orchards, ancient Narayan-Nagini Temple, and dramatic sunrise views over the sacred Kinner Kailash peak.");
        kalpa.setImageUrl("/images/kalpa.jpg");
        kalpa.setDiscoveryScore(85);
        kalpa.setHiddenGem(true);
        kalpa.setAdventureFriendly(false);
        kalpa.setNatureFriendly(true);
        kalpa.setFamilyFriendly(true);
        kalpa.setPeacefulFriendly(true);
        kalpa.setBudgetFriendly(true);
        kalpa.setPhotographyFriendly(true);
        kalpa.setSnowFriendly(true);
        kalpa.setTrekkingFriendly(true);
        kalpa.setCurrentCrowdPercentage(16);
        kalpa.setCurrentTrafficPercentage(12);
        kalpa.setCurrentParkingPercentage(20);
        kalpa.setHotelAvailabilityPercentage(85);
        kalpa.setTourismPressure(16);
        kalpa.setEnvironmentalLoad(14);
        kalpa.setWeatherCondition("Clear");
        kalpa.setTemperature(15.0);
        kalpa.setHumidity(45);
        kalpa.setNearbyAttractions("Kinnaur Kailash View, Reckong Peo, Narayan-Nagini Temple, Apple Orchards");
        kalpa.setLocalFood("Kinnauri Rajma, Apple Products, Local Dry Fruit");
        kalpa.setTransportOptions("Bus from Shimla (10hr), Taxi from Reckong Peo (15min)");
        destinationRepo.save(kalpa);

        Destination sangla = new Destination("Sangla Valley", "Himachal Pradesh", "valley", 31.4279, 78.2632);
        sangla.setDistrict("Kinnaur");
        sangla.setAltitude(2621);
        sangla.setMaxCapacity(2500);
        sangla.setDescription("Often called the most beautiful valley in Himachal Pradesh. Surrounded by towering peaks, lush meadows, and the pristine Baspa River. The Kamru Fort and its ancient temple are must-visits.");
        sangla.setImageUrl("/images/sangla.jpg");
        sangla.setDiscoveryScore(87);
        sangla.setHiddenGem(true);
        sangla.setAdventureFriendly(true);
        sangla.setNatureFriendly(true);
        sangla.setFamilyFriendly(true);
        sangla.setPeacefulFriendly(true);
        sangla.setBudgetFriendly(true);
        sangla.setPhotographyFriendly(true);
        sangla.setSnowFriendly(false);
        sangla.setTrekkingFriendly(true);
        sangla.setCurrentCrowdPercentage(19);
        sangla.setCurrentTrafficPercentage(14);
        sangla.setCurrentParkingPercentage(22);
        sangla.setHotelAvailabilityPercentage(80);
        sangla.setTourismPressure(18);
        sangla.setEnvironmentalLoad(16);
        sangla.setWeatherCondition("Clear");
        sangla.setTemperature(16.0);
        sangla.setHumidity(48);
        sangla.setNearbyAttractions("Kamru Fort, Bering Nag Temple, Baspa River, Chitkul");
        sangla.setLocalFood("Kinnauri Cuisine, Apple Products, Rajma Rice");
        sangla.setTransportOptions("Bus from Reckong Peo (2hr), Taxi from Shimla");
        destinationRepo.save(sangla);

        Destination mcleodganj = new Destination("McLeodGanj", "Himachal Pradesh", "hill_station", 32.2427, 76.3213);
        mcleodganj.setDistrict("Kangra");
        mcleodganj.setAltitude(1767);
        mcleodganj.setMaxCapacity(8000);
        mcleodganj.setDescription("The residence of the Dalai Lama and home to a vibrant Tibetan community. Known as 'Little Lhasa', it's a hub for Buddhist culture, meditation, and the famous Triund trek.");
        mcleodganj.setImageUrl("/images/mcleodganj.jpg");
        mcleodganj.setDiscoveryScore(22);
        mcleodganj.setHiddenGem(false);
        mcleodganj.setAdventureFriendly(true);
        mcleodganj.setNatureFriendly(true);
        mcleodganj.setFamilyFriendly(true);
        mcleodganj.setPeacefulFriendly(true);
        mcleodganj.setBudgetFriendly(true);
        mcleodganj.setPhotographyFriendly(true);
        mcleodganj.setSnowFriendly(false);
        mcleodganj.setTrekkingFriendly(true);
        mcleodganj.setCurrentCrowdPercentage(68);
        mcleodganj.setCurrentTrafficPercentage(62);
        mcleodganj.setCurrentParkingPercentage(45);
        mcleodganj.setHotelAvailabilityPercentage(38);
        mcleodganj.setTourismPressure(66);
        mcleodganj.setEnvironmentalLoad(58);
        mcleodganj.setWeatherCondition("Cloudy");
        mcleodganj.setTemperature(20.0);
        mcleodganj.setHumidity(75);
        mcleodganj.setNearbyAttractions("Dalai Lama Temple, Triund Trek, Bhagsu Nag Waterfall, Dharamkot, Naddi Viewpoint");
        mcleodganj.setLocalFood("Tibetan Momos, Thukpa, Butter Tea, Israeli Food");
        mcleodganj.setTransportOptions("Bus from Dharamshala (30min), Taxi, Auto");
        destinationRepo.save(mcleodganj);

        Destination kufri = new Destination("Kufri", "Himachal Pradesh", "hill_station", 31.0980, 77.2674);
        kufri.setDistrict("Shimla");
        kufri.setAltitude(2622);
        kufri.setMaxCapacity(5000);
        kufri.setDescription("A small hill station near Shimla known for adventure activities, potato festival, and the Himalayan Nature Park. Offers stunning views and winter snowfall.");
        kufri.setImageUrl("/images/kufri.jpg");
        kufri.setDiscoveryScore(35);
        kufri.setHiddenGem(false);
        kufri.setAdventureFriendly(true);
        kufri.setNatureFriendly(true);
        kufri.setFamilyFriendly(true);
        kufri.setPeacefulFriendly(false);
        kufri.setBudgetFriendly(false);
        kufri.setPhotographyFriendly(true);
        kufri.setSnowFriendly(true);
        kufri.setTrekkingFriendly(true);
        kufri.setCurrentCrowdPercentage(55);
        kufri.setCurrentTrafficPercentage(50);
        kufri.setCurrentParkingPercentage(40);
        kufri.setHotelAvailabilityPercentage(50);
        kufri.setTourismPressure(55);
        kufri.setEnvironmentalLoad(48);
        kufri.setWeatherCondition("Clear");
        kufri.setTemperature(16.0);
        kufri.setHumidity(60);
        kufri.setNearbyAttractions("Himalayan Nature Park, Fun World, Green Valley, Mahasu Peak");
        kufri.setLocalFood("Shimla Mirch, Madra, Local Chai");
        kufri.setTransportOptions("Taxi from Shimla (30min), Bus from Shimla");
        destinationRepo.save(kufri);

        Destination narkanda = new Destination("Narkanda", "Himachal Pradesh", "hill_station", 31.2584, 77.4590);
        narkanda.setDistrict("Shimla");
        narkanda.setAltitude(2708);
        narkanda.setMaxCapacity(3000);
        narkanda.setDescription("A quiet hill town famous for its apple orchards, skiing slopes, and the sacred Hatu Peak temple. An ideal alternative to crowded Shimla with similar mountain charm and better solitude.");
        narkanda.setImageUrl("/images/narkanda.jpg");
        narkanda.setDiscoveryScore(82);
        narkanda.setHiddenGem(true);
        narkanda.setAdventureFriendly(true);
        narkanda.setNatureFriendly(true);
        narkanda.setFamilyFriendly(true);
        narkanda.setPeacefulFriendly(true);
        narkanda.setBudgetFriendly(true);
        narkanda.setPhotographyFriendly(true);
        narkanda.setSnowFriendly(true);
        narkanda.setTrekkingFriendly(true);
        narkanda.setCurrentCrowdPercentage(14);
        narkanda.setCurrentTrafficPercentage(10);
        narkanda.setCurrentParkingPercentage(18);
        narkanda.setHotelAvailabilityPercentage(90);
        narkanda.setTourismPressure(14);
        narkanda.setEnvironmentalLoad(12);
        narkanda.setWeatherCondition("Clear");
        narkanda.setTemperature(13.0);
        narkanda.setHumidity(45);
        narkanda.setNearbyAttractions("Hatu Peak, Apple Orchards, Skiing (winter), Tannu Jubbar Lake");
        narkanda.setLocalFood("Apple Products, Pahadi Food, Local Rajma");
        narkanda.setTransportOptions("Bus from Shimla (2.5hr), Taxi, On NH-5");
        destinationRepo.save(narkanda);
    }

    private void seedAlerts() {
        // Active alerts for demo
        LocalAlert alert1 = new LocalAlert(1L, "Manali", LocalAlert.AlertType.WATER_SHORTAGE,
                LocalAlert.AlertSeverity.HIGH, "Water supply disrupted in Old Manali area due to pipeline damage. Tourists advised to carry water bottles.");
        alert1.setReportedBy("Manali Municipal Council");
        alert1.setVerified(true);
        alert1.setVerifiedAt(LocalDateTime.now().minusHours(2));
        alert1.setImpactScore(65);
        alertRepo.save(alert1);

        LocalAlert alert2 = new LocalAlert(1L, "Manali", LocalAlert.AlertType.OVERCROWDING,
                LocalAlert.AlertSeverity.CRITICAL, "Mall Road and Hadimba Temple area severely overcrowded. Tourist footfall exceeds safe capacity by 40%.");
        alert2.setReportedBy("Kullu Police");
        alert2.setVerified(true);
        alert2.setVerifiedAt(LocalDateTime.now().minusHours(1));
        alert2.setImpactScore(90);
        alertRepo.save(alert2);

        LocalAlert alert3 = new LocalAlert(2L, "Shimla", LocalAlert.AlertType.TRANSPORT,
                LocalAlert.AlertSeverity.MEDIUM, "Heavy traffic congestion on Shimla-Chandigarh highway. Expected delays of 2-3 hours.");
        alert3.setReportedBy("HP Traffic Police");
        alert3.setVerified(true);
        alert3.setVerifiedAt(LocalDateTime.now().minusHours(3));
        alert3.setImpactScore(55);
        alertRepo.save(alert3);

        LocalAlert alert4 = new LocalAlert(2L, "Shimla", LocalAlert.AlertType.ROAD_BLOCKAGE,
                LocalAlert.AlertSeverity.HIGH, "Landslide reported on Shimla-Kalka road near Shoghi. One lane operational. Avoid if possible.");
        alert4.setReportedBy("Local Resident");
        alert4.setVerified(false);
        alert4.setImpactScore(75);
        alertRepo.save(alert4);

        LocalAlert alert5 = new LocalAlert(4L, "Kasol", LocalAlert.AlertType.WASTE_PROBLEM,
                LocalAlert.AlertSeverity.MEDIUM, "Excessive littering reported along Parvati River banks. Clean-up drive in progress.");
        alert5.setReportedBy("Kasol Eco Group");
        alert5.setVerified(true);
        alert5.setVerifiedAt(LocalDateTime.now().minusHours(5));
        alert5.setImpactScore(40);
        alertRepo.save(alert5);

        LocalAlert alert6 = new LocalAlert(1L, "Manali", LocalAlert.AlertType.STRIKE_PROTEST,
                LocalAlert.AlertSeverity.HIGH, "Local taxi union strike in progress. Tourist transport disrupted. Use HRTC buses as alternative.");
        alert6.setReportedBy("Local Taxi Association");
        alert6.setVerified(true);
        alert6.setVerifiedAt(LocalDateTime.now().minusMinutes(45));
        alert6.setImpactScore(70);
        alertRepo.save(alert6);
    }

    private void seedBusinesses() {
        // Jibhi businesses (promoted — diverted destination)
        businessRepo.save(createBusiness(6L, "Himalayan Trout House", LocalBusiness.BusinessType.HOMESTAY,
                "Cozy wooden homestay with river views and home-cooked Pahadi meals. Run by a local family for 15+ years.",
                4.8, 1, "eco-friendly,riverside,traditional"));
        businessRepo.save(createBusiness(6L, "Jibhi Adventure Camp", LocalBusiness.BusinessType.ADVENTURE,
                "Trekking, rock climbing, and river crossing activities guided by certified local mountaineers.",
                4.5, 1, "adventure,guided,safe"));
        businessRepo.save(createBusiness(6L, "Tirthan Crafts", LocalBusiness.BusinessType.HANDICRAFT,
                "Handmade Kullu shawls, wooden carvings, and local honey. Supports 12 local artisan families.",
                4.7, 1, "handmade,traditional,sustainable"));

        // Tirthan Valley businesses
        businessRepo.save(createBusiness(7L, "River Valley Homestay", LocalBusiness.BusinessType.HOMESTAY,
                "Riverside homestay with organic garden, trout fishing spot, and stargazing deck.",
                4.9, 2, "organic,riverside,stargazing"));
        businessRepo.save(createBusiness(7L, "Tirthan Trout Farm", LocalBusiness.BusinessType.RESTAURANT,
                "Fresh river trout prepared in traditional Pahadi style. Catch-and-cook experience available.",
                4.6, 1, "fresh,traditional,experience"));
        businessRepo.save(createBusiness(7L, "GHNP Nature Guide", LocalBusiness.BusinessType.GUIDE,
                "Certified guide for Great Himalayan National Park treks. 20+ years experience with local wildlife.",
                4.8, 2, "certified,wildlife,nature"));

        // Shoja businesses
        businessRepo.save(createBusiness(8L, "Banjara Retreat Shoja", LocalBusiness.BusinessType.HOMESTAY,
                "Mountain-view cottage with traditional wooden architecture. Organic breakfast included.",
                4.7, 2, "mountain-view,organic,traditional"));

        // Bir Billing businesses
        businessRepo.save(createBusiness(9L, "Billing Paragliding Co.", LocalBusiness.BusinessType.ADVENTURE,
                "Professional tandem paragliding from Billing takeoff point. Certified pilots with 10+ years experience.",
                4.6, 2, "paragliding,certified,adventure"));
        businessRepo.save(createBusiness(9L, "Tibetan Kitchen Bir", LocalBusiness.BusinessType.RESTAURANT,
                "Authentic Tibetan cuisine in a monastery-adjacent cafe. Momos, Thukpa, and butter tea specialties.",
                4.5, 1, "tibetan,authentic,vegetarian-friendly"));

        // Chitkul businesses
        businessRepo.save(createBusiness(11L, "Chitkul Homestay", LocalBusiness.BusinessType.HOMESTAY,
                "Simple, warm homestay in India's last village. Traditional Kinnauri meals and stories by the fireplace.",
                4.4, 1, "authentic,kinnauri,traditional"));

        // Barot businesses
        businessRepo.save(createBusiness(10L, "Uhl River Camp", LocalBusiness.BusinessType.ADVENTURE,
                "Camping, fishing, and nature walks along the Uhl River. Equipment provided.",
                4.3, 1, "camping,fishing,nature"));
    }

    private LocalBusiness createBusiness(Long destId, String name, LocalBusiness.BusinessType type,
                                          String desc, double rating, int price, String tags) {
        LocalBusiness b = new LocalBusiness(destId, name, type, desc);
        b.setRating(rating);
        b.setPriceRange(price);
        b.setTags(tags);
        b.setPromoted(true);
        return b;
    }
}
