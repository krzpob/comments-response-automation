package com.automation.comments.adapter.inbound.web

import com.automation.comments.adapter.outbound.meta.InstagramOAuthClient
import com.automation.comments.adapter.outbound.persistence.jpa.AccessTokenJpaAdapter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.http.ResponseEntity
import org.springframework.web.servlet.view.RedirectView
import org.springframework.util.StringUtils
import org.slf4j.LoggerFactory


@RestController
@RequestMapping("/oauth/instagram")
class InstagramOauthController(val oauthClient: InstagramOAuthClient, val accessTokenAdapter: AccessTokenJpaAdapter) {

    private val log=LoggerFactory.getLogger(javaClass)
    
    @GetMapping(value=["/callback"])
    fun callback(@RequestParam(required=false)code:String?,
                @RequestParam(required=false)error:String?,
                @RequestParam(required=false)error_description:String?
        ):ResponseEntity<String> {
            if(StringUtils.hasText(error)){
                log.error("Błąd OAuth: {} : {}", error, error_description)
                return ResponseEntity.badRequest().body("Błąd autoryzacji")
            }
            
            if(code == null || !StringUtils.hasText(code)) {
                log.error("Brak parametru code w callbacku OAuth")
                return ResponseEntity.badRequest().body("Brak kodu autoryzacyjnego")
            }
            log.info("Wymiana kodu {} na token", code)
        
            val shortLived = oauthClient.exchangeCodeForShortLivedToken(code)
            val longlife = oauthClient.exchangeForLongLivedToken(shortLived)
            accessTokenAdapter.save(longlife)
            
            return ResponseEntity.ok("Autoryzacja zakończona sukcesem. Token ważny do: ${longlife.expiresAt}")
    }
    
    @GetMapping(value =["/authorize"])
    fun authorize():RedirectView {
        val url = oauthClient.buildAuthorizationUrl()
        log.info("Przekierowani do autoryzacji: {}", url)
        return RedirectView(url)
    }
}