package com.automation.comments.adapter.inbound.web.dto

import org.assertj.core.api.BDDAssertions
import org.junit.jupiter.api.Test
import tools.jackson.module.kotlin.jacksonObjectMapper
import tools.jackson.module.kotlin.readValue

class MetaWebhookPayloadTest {

    @Test
    fun shouldDeserializeObject(){
        val payloadString = """
           {
              "object": "instagram",
              "entry": [
                {
                  "id": "17841400008765432",
                  "time": 1689256213,
                  "changes": [
                    {
                      "field": "comments",
                      "value": {
                        "media_id": "18012345678901234",
                        "comment_id": "17901234567890123",
                        "text": "Super rolka! Gdzie kupiłeś te buty?",
                        "username": "uzytkownik_ig",
                        "from": {
                          "id": "1234567890",
                          "username": "uzytkownik_ig"
                        }
                      }
                    }
                  ]
                }
              ]
            }
        """
        val objectMapper = jacksonObjectMapper()
        val payload = objectMapper.readValue<MetaWebhookPayload>(payloadString)
        BDDAssertions.then(payload.`object`).isEqualTo("instagram")
        BDDAssertions.then(payload.entry).hasSize(1)
        BDDAssertions.then(payload.entry[0].changes).hasSize(1)
        BDDAssertions.then(payload.entry[0].changes[0].field).isEqualTo("comments")
    }
}