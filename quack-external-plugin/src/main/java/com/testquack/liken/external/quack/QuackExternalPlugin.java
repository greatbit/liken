package com.testquack.liken.external.quack;

import com.testquack.liken.beans.Launch;
import com.testquack.liken.beans.LaunchStatus;
import com.testquack.liken.external.LikenExternalServicePlugin;
import com.testquack.liken.external.error.ExternalPluginException;
import com.testquack.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

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
        logger.info("Sending request to QUACK: " + getClient(request).updateStatus(launch.getMetadata().get(PROJECT_ID_META_KEY).toString(),
                launch.getExternalId(), testcaseUUID, status.toString()).request().url());
        try {
            int code = getClient(request).updateStatus(launch.getMetadata().get(PROJECT_ID_META_KEY).toString(),
                    launch.getExternalId(), testcaseUUID, status.toString()).execute().code();
            if (code != 200){
                throw new ExternalPluginException("Unable to send testcase status to QuAck, got response code: " + code);
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
