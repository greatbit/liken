package com.greatbit.liken.external;

import com.greatbit.liken.beans.Launch;
import com.greatbit.liken.beans.LaunchStatus;

import javax.servlet.http.HttpServletRequest;

public interface LikenExternalServicePlugin {
    public void updateExternalTestcaseStatus(HttpServletRequest request, Launch launch, String testcaseUUID, LaunchStatus status);
    public void deleteExternal(HttpServletRequest request, String launchId);
}
