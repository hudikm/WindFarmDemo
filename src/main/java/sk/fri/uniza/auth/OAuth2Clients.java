package sk.fri.uniza.auth;

import com.google.common.collect.ImmutableMap;
import sk.fri.uniza.api.Paged;
import sk.fri.uniza.db.BasicDao;

import java.util.*;

import java.util.concurrent.ConcurrentHashMap;

public class OAuth2Clients implements BasicDao<OAuth2Client, String> {
    private static OAuth2Clients instance = null;
    private static final Map<String, OAuth2Client> clientsDB;

    static {
        OAuth2Client client1 = new OAuth2ClientBuilder()
                .setClientId("client1")
                .setRedirectUri("")
                .setClientSecrete("123456")
                .createOAuth2Client();

        OAuth2Client client2 = new OAuth2ClientBuilder()
                .setClientId("client2")
                .setClientSecrete("123456")
                .setRedirectUri("").createOAuth2Client();

        clientsDB = ImmutableMap.of(
                client1.getClientId(), client1,
                client2.getClientId(), client2
        );

    }


    public static OAuth2Clients getInstance() {
        if (instance == null) instance = new OAuth2Clients();
        return instance;
    }


    @Override
    public Optional<OAuth2Client> findById(String id) {
        OAuth2Client oAuth2Client = clientsDB.get(id);
        return Optional.ofNullable(oAuth2Client);
    }

    @Override
    public List<OAuth2Client> getAll() {
        return new ArrayList<OAuth2Client>(clientsDB.values());
    }

    @Override
    public Paged<List<OAuth2Client>> getAll(int limit, int page) {
        return null;
    }

    @Override
    public String save(OAuth2Client oAuth2Client) {
        return null;
    }

    @Override
    public String update(OAuth2Client oAuth2Client, String[] params) {
        return null;
    }

    @Override
    public void delete(OAuth2Client oAuth2Client) {
    }


}
