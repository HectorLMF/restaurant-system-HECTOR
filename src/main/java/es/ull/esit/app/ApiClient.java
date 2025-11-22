package es.ull.esit.app;

// Wrapper deprecated que delega en el nuevo middleware.ApiClient
@Deprecated
public class ApiClient {
    private final es.ull.esit.app.middleware.ApiClient delegate;

    public ApiClient(String baseUrl) {
        this.delegate = new es.ull.esit.app.middleware.ApiClient(baseUrl);
    }

    public String getInfo() throws Exception {
        return delegate.getInfo();
    }

    public String getMenu() throws Exception {
        return delegate.getMenu();
    }

    public String login(String name) throws Exception {
        return delegate.login(name);
    }
}
