package com.automation.comments.adapter.outbound.meta

import org.springframework.context.annotation.Configuration
import org.springframework.boot.context.properties.ConfigurationProperties

@Configuration
@ConfigurationProperties(prefix="meta")
public class MetaProperties(
    var clientId:String="",
    var tokenBaseUrl: String = "https://api.instagram.com/oauth/access_token",
    var authBaseUrl: String = "https://api.instagram.com/oauth/authorize",
    var apiVersion: String = "v25.0",
    var clientSecret: String="",
    var redirectUrl: String="",
    var pageAccessToken: String=""
){
    val appToken: String
        get() = "$clientId|$clientSecret"
}