package org.umn.gearhub.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class AddGearValidatorTest {
    @Test
    fun `price rejects empty, non-numeric and out of range`() {
        assertNotNull(AddGearValidator.validatePrice(""))
        assertNotNull(AddGearValidator.validatePrice("75.000"))
        assertNotNull(AddGearValidator.validatePrice("5000"))
        assertNotNull(AddGearValidator.validatePrice("2000000"))
        assertNull(AddGearValidator.validatePrice("75000"))
    }

    @Test
    fun `required fields block empty input`() {
        assertNotNull(AddGearValidator.validateName("  "))
        assertNotNull(AddGearValidator.validateCategory(null))
        assertNotNull(AddGearValidator.validateLocation(null))
        assertNotNull(AddGearValidator.validateDescription(""))
        assertNotNull(AddGearValidator.validateIncluded(" , "))
        assertNull(AddGearValidator.validateName("Sony A6400"))
    }

    @Test
    fun `included items are split by comma and trimmed`() {
        assertEquals(listOf("Unit", "Baterai"), AddGearValidator.splitItems(" Unit , Baterai,, "))
    }
}
