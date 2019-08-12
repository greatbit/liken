package com.testquack.liken.external;

import com.testquack.liken.beans.Launch;
import com.testquack.liken.beans.LaunchStatus;

import javax.servlet.http.HttpServletRequest;

public class DummyExternalPlugin implements LikenExternalServicePlugin {
    @Override
    public void updateExternalTestcaseStatus(HttpServletRequest request, Launch launch, String testcaseUUID, LaunchStatus status) {

    }

    @Override
    public void deleteExternal(HttpServletRequest request, String launchId) {

    }
}
