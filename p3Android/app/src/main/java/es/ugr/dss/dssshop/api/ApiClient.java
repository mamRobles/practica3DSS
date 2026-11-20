package es.ugr.dss.dssshop.api;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiClient {
    //private static final String BASE_URL =
    //        "http://10.0.2.2:8080/"; // esto para el emulador de android de android studio
    // ~/Android/Sdk/platform-tools/adb reverse tcp:8080 tcp:8080
    // y lo siguiente para mi telf:
    private static final String BASE_URL = "http://localhost:8080/";

    private static final Retrofit retrofit =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
    public static ProductApi getProductApi() {
        return retrofit.create(ProductApi.class);
    }

}