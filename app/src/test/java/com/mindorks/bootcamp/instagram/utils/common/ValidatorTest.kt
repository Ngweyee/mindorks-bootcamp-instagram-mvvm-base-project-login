package com.mindorks.bootcamp.instagram.utils.common

import com.mindorks.bootcamp.instagram.R
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.hasSize
import org.junit.Test


class ValidatorTest {


    @Test
    fun givenValidEmailAndValidPwd_whenValidate_shouldReturnSuccess() {
        val email = "test@gmail.com"
        val password = "password"
        val validation = Validator.validateLoginFields(email, password)
        assertThat(validation, hasSize(2))
        assertThat(
            validation,
            contains(
                Validation(Validation.Field.EMAIL, Resource.success()),
                Validation(
                    Validation.Field.PASSWORD, Resource.success()
                )
            )
        )
    }

    @Test
    fun givenInvalidEmailAndValidPwd_whenValidate_shouldReturnEmailError() {
        val email = "test"
        val password = "password"
        val validation = Validator.validateLoginFields(email, password)
        assertThat(validation, hasSize(2))
        assertThat(
            validation,
            contains(
                Validation(Validation.Field.EMAIL, Resource.error(R.string.email_field_invalid)),
                Validation(Validation.Field.PASSWORD, Resource.success())
            )
        )

    }

    @Test
    fun givenValidEmailAndInValidPwd_whenValidate_shouldReturnPwdError() {
        val email = "test@gmail.com"
        val password = "pwd"
        val validation = Validator.validateLoginFields(email, password)
        assertThat(validation, hasSize(2))
        assertThat(
            validation,
            contains(
                Validation(Validation.Field.EMAIL, Resource.success()),
                Validation(
                    Validation.Field.PASSWORD,
                    Resource.error(R.string.password_field_small_length)
                )
            )
        )

    }

    @Test
    fun givenInvalidEmailAndEmptydPwd_whenValidate_shouldReturnEmailAndPwdEmptyError() {
        val email = "test"
        val password = ""
        val validation = Validator.validateLoginFields(email, password)
        assertThat(validation, hasSize(2))
        assertThat(
            validation,
            contains(
                Validation(Validation.Field.EMAIL, Resource.error(R.string.email_field_invalid)),
                Validation(Validation.Field.PASSWORD, Resource.error(R.string.password_field_empty))
            )
        )

    }
}