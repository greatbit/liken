package com.testquack.liken.external.quack;

import com.testquack.liken.beans.Launch;
import com.testquack.liken.beans.LaunchStatus;
import com.testquack.liken.external.LikenExternalServicePlugin;
import com.testquack.liken.external.error.ExternalPluginException;
import com.testquack.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import retrofit2.Call;
import retrofit2.Response;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import static java.lang.String.format;
import static org.springframework.util.StringUtils.isEmpty;

public class QuackExternalPlugin implements LikenExternalServicePlugin {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${quack.api.endpoint}")
    private String quackApiEndpoint;

    @Value("${quack.api.timeout.ms}")
    private int quackApiTimeoutMs;

    private final String PROJECT_ID_META_KEY = "projectId";

    @Override
    public void updateExternalTestcaseStatus(HttpServletRequest request, Launch launch, String testcaseUUID, LaunchStatus status) {
        if (isEmpty(launch.getMetadata().get(PROJECT_ID_META_KEY))){
            throw new ExternalPluginException("Project meta property is not set in Launch");
        }
        Call<Launch> call = getClient(request).updateStatus(launch.getMetadata().get(PROJECT_ID_META_KEY).toString(),
                launch.getExternalId(), testcaseUUID, status.toString());
        try {
            Response<Launch> response = call.execute();
            if (!response.isSuccessful()){
                logger.warn(format("Unable so update testcase status in QuAck. " +
                                "URL: %s \n Response code: %s \n Error: %s \n Message: %s",
                        call.request().url(),
                        response.code(),
                        response.errorBody() != null ? response.errorBody().string() : null,
                        response.message()));
                throw new ExternalPluginException("Unable to send testcase status to QuAck, got response code: " + response.code());
            }
        } catch (IOException e) {
            throw new ExternalPluginException("Unable to send testcase status to QuAck", e);
        }
    }

    @Override
    public void deleteExternal(HttpServletRequest request, String launchId) {
        throw new UnsupportedOperationException();
    }

    private QuackClient getClient(HttpServletRequest request) {
        return HttpClientBuilder.builder(quackApiEndpoint, quackApiTimeoutMs, request).build().
                create(QuackClient.class);
    }
}
