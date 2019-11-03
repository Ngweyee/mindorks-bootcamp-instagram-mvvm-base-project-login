package com.mindorks.bootcamp.instagram.calculator

class Calculator(val operation: Operation) {

    fun add(a : Int,b : Int) = operation.add(a,b)

    fun multiply(a : Int,b : Int) = operation.multiply(a,b)

}