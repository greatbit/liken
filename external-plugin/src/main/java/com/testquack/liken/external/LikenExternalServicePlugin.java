package com.testquack.liken.external;

import com.testquack.liken.beans.Launch;
import com.testquack.liken.beans.LaunchStatus;

import javax.servlet.http.HttpServletRequest;

public interface LikenExternalServicePlugin {
    public void updateExternalTestcaseStatus(HttpServletRequest request, Launch launch, String testcaseUUID, LaunchStatus status);
    public void deleteExternal(HttpServletRequest request, String launchId);
}
