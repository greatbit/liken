package com.testquack.liken.external.quack;

import com.testquack.liken.beans.Launch;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface QuackClient {

    @POST("{projectId}/launch/{launchId}/{testcaseUUID}/status/{status}")
    Call<Launch> updateStatus(@Path("projectId") String projectId,
                              @Path("launchId") String launchId,
                              @Path("testcaseUUID") String testcaseUUID,
                              @Path("status") String status);
}
