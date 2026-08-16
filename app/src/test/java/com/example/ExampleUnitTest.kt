package com.example

import com.example.data.DefaultData
import org.junit.Assert.*
import org.junit.Test

/**
 * Local unit tests for business club testimonials and default configurations.
 */
class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testBusinessClubTestimonials_isNotEmpty() {
    val testimonials = DefaultData.businessClubTestimonials
    assertTrue(testimonials.isNotEmpty())
    assertEquals(5, testimonials.size)
    testimonials.forEach { testimonial ->
      assertTrue(testimonial.clientName.isNotBlank())
      assertTrue(testimonial.businessName.isNotBlank())
      assertTrue(testimonial.feedback.isNotBlank())
      assertEquals(5, testimonial.rating)
      assertTrue(testimonial.verifiedMember)
    }
  }
}

