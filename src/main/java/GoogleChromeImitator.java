import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.logging.Level;

/**
 * Created by yana0 on 28.12.16.
 */
public class GoogleChromeImitator {
    private static final String REPLACEMENT_APP_ID = "{APP_ID}";
    private static final String REDIRECT_URI = "https://oauth.vk.com/blank.html";
    private static final String DISPLAY = "page";
    private static final String RESPONSE_TYPE = "token";
    private static final String SCOPE = "photos,messages";
    private static final Double API_VERSION = 5.60;

    private static String oauthUrl = "https://oauth.vk.com/authorize?" +
            "client_id="+REPLACEMENT_APP_ID+
            "&scope="+SCOPE+
            "&redirect_uri="+ REDIRECT_URI+
            "&display="+DISPLAY+
            "&v=" + API_VERSION +
            "&response_type="+RESPONSE_TYPE;

    static {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
    }
    private static String getToken(String url) {
        return url.split("#")[1].split("&")[0].split("=")[1];
    }

    private static Integer getUserId(String url){
        return Integer.parseInt(url.substring(url.lastIndexOf("=")+1));
    }

    public AccessParameters executeAuthorizeVkQuery(String appId, String login, String pass) throws IOException {

        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setThrowExceptionOnScriptError(false);

        HtmlPage oathPage = webClient.getPage(oauthUrl.replace(REPLACEMENT_APP_ID, appId));
        HtmlForm form = oathPage.getFormByName("");

        form.getInputByName("email").setValueAttribute(login);
        form.getInputByName("pass").setValueAttribute(pass);

        HtmlButton button = (HtmlButton) form.getElementsByAttribute("button", "id", "install_allow").get(0);
        HtmlPage tokenPage = button.click();

        AccessParameters accessParameters = new AccessParameters();

        accessParameters.token = getToken(tokenPage.getUrl().toString());
        accessParameters.userId = getUserId(tokenPage.getUrl().toString());

        return accessParameters;
    }


    class AccessParameters {

        private String token;
        private Integer userId;

        private AccessParameters(){};

        String getToken() {return token;}
        Integer getUserId() {return userId;}
    }
}
