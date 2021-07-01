package com.nazzd.common.security;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoadBalancedClientCredentialsResourceDetails extends ClientCredentialsResourceDetails {

    public static final String EXCEPTION_MESSAGE = "Returning an invalid URI: {}";

    private final LoadBalancerClient loadBalancerClient;

    public LoadBalancedClientCredentialsResourceDetails(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

    @Override
    public String getAccessTokenUri() {
        if (loadBalancerClient != null) {
            try {
                URI uri = new URI(super.getAccessTokenUri());
                return loadBalancerClient.reconstructURI(
                        loadBalancerClient.choose(uri.getHost()),
                        uri
                ).toString();
            } catch (URISyntaxException e) {
                log.error(EXCEPTION_MESSAGE, e.getMessage());
                return super.getAccessTokenUri();
            }
        } else {
            return super.getAccessTokenUri();
        }
    }

}

