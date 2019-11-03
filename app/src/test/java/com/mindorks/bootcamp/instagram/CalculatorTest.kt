package com.mindorks.bootcamp.instagram

import com.mindorks.bootcamp.instagram.calculator.Calculator
import com.mindorks.bootcamp.instagram.calculator.Operation
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CalculatorTest {

    @Mock
    private lateinit var operation: Operation

    private lateinit var calculator: Calculator

    @Before
    fun setup() {
        calculator = Calculator(operation)

    }

    @Test
    fun givenValidInput_whenAdd_shouldCallAddOperation() {
        val a = 5
        val b = 5
        calculator.add(a, b)
        verify(operation).add(a, b)
        verify(operation, never()).multiply(a, b)
    }

}