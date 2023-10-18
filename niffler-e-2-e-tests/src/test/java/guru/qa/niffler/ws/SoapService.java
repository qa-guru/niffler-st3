package guru.qa.niffler.ws;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.userdata.wsdl.AcceptInvitationRequest;
import guru.qa.niffler.userdata.wsdl.AcceptInvitationResponse;
import guru.qa.niffler.userdata.wsdl.AddFriendRequest;
import guru.qa.niffler.userdata.wsdl.AddFriendResponse;
import guru.qa.niffler.userdata.wsdl.AllUsersRequest;
import guru.qa.niffler.userdata.wsdl.AllUsersResponse;
import guru.qa.niffler.userdata.wsdl.Currency;
import guru.qa.niffler.userdata.wsdl.CurrentUserRequest;
import guru.qa.niffler.userdata.wsdl.CurrentUserResponse;
import guru.qa.niffler.userdata.wsdl.DeclineInvitationRequest;
import guru.qa.niffler.userdata.wsdl.DeclineInvitationResponse;
import guru.qa.niffler.userdata.wsdl.FriendState;
import guru.qa.niffler.userdata.wsdl.FriendsRequest;
import guru.qa.niffler.userdata.wsdl.FriendsResponse;
import guru.qa.niffler.userdata.wsdl.InvitationsRequest;
import guru.qa.niffler.userdata.wsdl.InvitationsResponse;
import guru.qa.niffler.userdata.wsdl.RemoveFriendRequest;
import guru.qa.niffler.userdata.wsdl.RemoveFriendResponse;
import guru.qa.niffler.userdata.wsdl.UpdateUserInfoRequest;
import guru.qa.niffler.userdata.wsdl.UpdateUserInfoResponse;
import guru.qa.niffler.ws.converter.JaxbConverterFactory;
import io.qameta.allure.okhttp3.AllureOkHttp3;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public abstract class SoapService {

    protected static final Config CFG = Config.getInstance();
    protected final OkHttpClient httpClient;

    protected final Retrofit retrofit;

    public SoapService(String baseUrl) {
        this(baseUrl, false, null);
    }

    public SoapService(String baseUrl, boolean followRedirect) {
        this(baseUrl, followRedirect, null);
    }

    public SoapService(String baseUrl, boolean followRedirect, Interceptor... interceptors) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .followRedirects(followRedirect)
                .addInterceptor(new AllureOkHttp3());

        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                builder.addNetworkInterceptor(interceptor);
            }
        }
        builder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

        this.httpClient = builder.build();
        try {
            this.retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(httpClient)
                    .addConverterFactory(
                            JaxbConverterFactory.create(
                                    JAXBContext.newInstance(
                                            CurrentUserRequest.class,
                                            CurrentUserResponse.class,
                                            UpdateUserInfoRequest.class,
                                            UpdateUserInfoResponse.class,
                                            AllUsersRequest.class,
                                            AllUsersResponse.class,
                                            FriendsRequest.class,
                                            FriendsResponse.class,
                                            InvitationsRequest.class,
                                            InvitationsResponse.class,
                                            AcceptInvitationRequest.class,
                                            AcceptInvitationResponse.class,
                                            DeclineInvitationRequest.class,
                                            DeclineInvitationResponse.class,
                                            AddFriendRequest.class,
                                            AddFriendResponse.class,
                                            RemoveFriendRequest.class,
                                            RemoveFriendResponse.class,
                                            Currency.class,
                                            FriendState.class
                                    )
                            )
                    )
                    .build();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
