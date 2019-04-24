package sk.fri.uniza.config;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Collections;
import java.util.Map;

public class WindFarmDemoConfiguration extends Configuration {
    @Valid
    @NotNull
    private OAuth2Configuration oAuth2Configuration = new OAuth2Configuration();

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();
    @NotNull
    private Map<String, Map<String, String>> viewRendererConfiguration = Collections.emptyMap();
    @NotEmpty
    private String template;
    @NotEmpty
    private String defaultName = "Stranger";

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
    }

    @JsonProperty("auth2")
    public OAuth2Configuration getoAuth2Configuration() {
        return oAuth2Configuration;
    }

    @JsonProperty("oauth2")
    public void setoAuth2Configuration(OAuth2Configuration oAuth2Configuration) {
        this.oAuth2Configuration = oAuth2Configuration;
    }

    @JsonProperty
    public String getTemplate() {
        return template;
    }

    @JsonProperty
    public void setTemplate(String template) {
        this.template = template;
    }

    @JsonProperty
    public String getDefaultName() {
        return defaultName;
    }

    @JsonProperty
    public void setDefaultName(String name) {
        this.defaultName = name;
    }

    @JsonProperty("viewRendererConfiguration")
    public Map<String, Map<String, String>> getViewRendererConfiguration() {
        return viewRendererConfiguration;
    }

    @JsonProperty("viewRendererConfiguration")
    public void setViewRendererConfiguration(Map<String, Map<String, String>> viewRendererConfiguration) {
        this.viewRendererConfiguration = viewRendererConfiguration;
    }
}
