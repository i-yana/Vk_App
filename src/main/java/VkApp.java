import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.CodeSource;
import java.util.Properties;

/**
 * Created by yana0 on 28.12.16.
 */
public class VkApp {
    public static void main(String[] args) {

        CodeSource src = VkApp.class.getProtectionDomain().getCodeSource();
        String configFileName = src.getLocation().getPath() + "config.properties";

        try (FileInputStream fileInputStream = new FileInputStream(configFileName)) {

            Properties properties = new Properties();
            properties.load(fileInputStream);

            String login = properties.getProperty("login");
            String pass = properties.getProperty("pass");
            String app_id = properties.getProperty("app_id");

            GoogleChromeImitator browser = new GoogleChromeImitator();
            GoogleChromeImitator.AccessParameters access = browser.executeAuthorizeVkQuery(app_id, login, pass);

            String token = access.getToken();
            Integer usrId= access.getUserId();

            TransportClient transportClient = HttpTransportClient.getInstance();
            VkApiClient vk = new VkApiClient(transportClient);
            UserActor userActor = new UserActor(usrId, token);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
