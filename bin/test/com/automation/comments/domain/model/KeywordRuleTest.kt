package com.automation.comments.domain.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class KeywordRuleTest {

    @Test
    fun `valid rule is created without exception`() {
        KeywordRule(keyword = "promo", dmTemplate = "Napisz PROMO by otrzymać link", commentReplyTemplate = "Sprawdź DM!")
    }

    @Test
    fun `rejects dm template with http url`() {
        val ex = assertThrows<IllegalArgumentException> {
            KeywordRule(keyword = "promo", dmTemplate = "Kliknij http://example.com", commentReplyTemplate = "Sprawdź DM!")
        }
        assertEquals(true, ex.message?.contains("Meta policy"))
    }

    @Test
    fun `rejects dm template with https url`() {
        assertThrows<IllegalArgumentException> {
            KeywordRule(keyword = "promo", dmTemplate = "Tutaj: https://sklep.pl/oferta", commentReplyTemplate = "Sprawdź DM!")
        }
    }

    @Test
    fun `rejects dm template with www url`() {
        assertThrows<IllegalArgumentException> {
            KeywordRule(keyword = "promo", dmTemplate = "Wejdź na www.sklep.pl", commentReplyTemplate = "Sprawdź DM!")
        }
    }

    @Test
    fun `rejects blank keyword`() {
        assertThrows<IllegalArgumentException> {
            KeywordRule(keyword = "  ", dmTemplate = "Cześć!", commentReplyTemplate = "Sprawdź DM!")
        }
    }
}
