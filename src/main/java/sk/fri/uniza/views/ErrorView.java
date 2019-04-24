package sk.fri.uniza.views;

import io.dropwizard.jersey.errors.ErrorMessage;
import io.dropwizard.views.View;

public class ErrorView extends View {


    private ErrorMessage errorMessage;

    public ErrorView(ErrorMessage errorMessage) {
        super("error_page.ftl");
        this.errorMessage = errorMessage;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }


}
