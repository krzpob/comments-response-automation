package com.automation.comments.adapter.outbound.meta

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.client.RestClient
import org.springframework.http.MediaType
import org.springframework.util.MultiValueMap
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.util.UriComponentsBuilder
import org.springframework.stereotype.Component
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.UUID
import com.automation.comments.domain.model.AccessToken
import com.automation.comments.domain.model.TokenType
import com.automation.comments.adapter.outbound.meta.dto.InstagramTokenResponse
import com.automation.comments.adapter.outbound.meta.dto.InstagramUserResponse

@Component
class InstagramOAuthClient(
    val metaProperties: MetaProperties
    ) {
        companion object {
           private val GRAPH_URL="https://graph.instagram.com/"
        }
        private val restClient = RestClient.builder().build()
        private val AUTH_SCOPE = "instagram_business_basic,instagram_business_manage_messages,instagram_business_manage_comments,instagram_business_content_publish,instagram_business_manage_insights"
        
        private val log = LoggerFactory.getLogger(javaClass)


        
        fun buildAuthorizationUrl():String {
            log.info("Properties: {}", metaProperties)
            return UriComponentsBuilder.fromUriString(metaProperties.authBaseUrl)
                .queryParam("client_id", metaProperties.clientId)
                .queryParam("redirect_uri", metaProperties.redirectUrl)
                .queryParam("scope", AUTH_SCOPE)
                .queryParam("response_type", "code")
                .queryParam("state", UUID.randomUUID().toString())
                .build()
                .toUriString()
        }
        
        fun exchangeCodeForShortLivedToken(code: String):AccessToken{
            log.info("Wymieniam kod na token ...")
            val response = restClient.post()
                .uri(metaProperties.tokenBaseUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(buildCodeExchangeBody(code))
                .retrieve()
                .body(InstagramTokenResponse::class.java)
                
            if(response?.accessToken == null) {
                throw RuntimeException("Brak tokenu")
            }    
            log.info("Wymieniłem kod na token {} typu {} wygasający {}", response.accessToken, response.tokenType,response.expiresIn)
            
            val me = fetchMe(response?.accessToken?:throw RuntimeException("Brak tokenu"))
                    
            return AccessToken(
                    ownerIgId = me?.id?:"",
                    ownerUsername = me?.username?:"",
                    token = response.accessToken,
                    expiresAt = Instant.now().plusSeconds(3_600),
                    tokenType = TokenType.SHORT_LIVED    
            )
        }
        
        fun exchangeForLongLivedToken(shortLived: AccessToken): AccessToken {
            val uri = UriComponentsBuilder.fromUriString(GRAPH_URL)
            .path("access_token")
            .queryParam("client_secret", metaProperties.clientSecret)
            .queryParam("client_id", metaProperties.clientId)
            .queryParam("grant_type","ig_exchange_token")
            .queryParam("access_token",shortLived.token).build().toUri()
            
              
            val response = restClient.get()
                .uri(uri)
                .retrieve()
                .body(InstagramTokenResponse::class.java)
            
                if(response?.accessToken == null) {
                throw RuntimeException("Brak tokenu")
            }    
            return AccessToken(
                ownerIgId=shortLived.ownerIgId,
                ownerUsername=shortLived.ownerUsername,
                token=response.accessToken,
                expiresAt=Instant.now().plusSeconds(response.expiresIn),
                tokenType=TokenType.LONG_LIVED    
            )   
            
        }
        
        fun refreshToken(oldToken: AccessToken): AccessToken {
            val uri = UriComponentsBuilder.fromUriString(GRAPH_URL)
                .path("refresh_access_token")
                .queryParam("grant_type","ig_refresh_token")
                .queryParam("access_token",oldToken.token)
                .build().toUri()
             val response = restClient.get()
                .uri(uri)
                .retrieve()   
                .body(InstagramTokenResponse::class.java)
                
             return AccessToken(
                ownerIgId=oldToken.ownerIgId,
                ownerUsername=oldToken.ownerUsername,
                token=response?.accessToken?:throw RuntimeException("Brak tokena"),
                expiresAt=Instant.now().plusSeconds(response.expiresIn),
                tokenType=TokenType.LONG_LIVED    
            )    
        }
        
        // -- HELPERS
        
        private fun buildCodeExchangeBody(code:String):MultiValueMap<String,String>{
            val map:MultiValueMap<String,String> = LinkedMultiValueMap<String,String>()
            map.add("code", code)
            map.add("client_id", metaProperties.clientId)
            map.add("client_secret",metaProperties.clientSecret)
            map.add("grant_type","authorization_code")
            map.add("redirect_uri",metaProperties.redirectUrl)
            return map
        }
        
        private fun fetchMe(accessToken: String):InstagramUserResponse?{
            val uri = UriComponentsBuilder.fromUriString( GRAPH_URL)
                .path("${metaProperties.apiVersion}/me")
                .queryParam("fields","id,username")
                .queryParam("access_token", accessToken)
                .build().toUri()
                
                return restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(InstagramUserResponse::class.java)
                
        }
    }