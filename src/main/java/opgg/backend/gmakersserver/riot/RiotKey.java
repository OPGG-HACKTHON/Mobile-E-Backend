package opgg.backend.gmakersserver.riot;

import static com.merakianalytics.orianna.Orianna.*;

public class RiotKey {

    private static String key = "";

    public static String getRiotKey() {
        return key;
    }

    public static void setRiotKey(String updateKey) {
        key = updateKey;
        setRiotAPIKey(key);
    }

}
